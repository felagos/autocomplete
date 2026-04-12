package com.autocomplete.service;

import com.autocomplete.datastructure.Trie;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Periodic Trie rebuild job.
 *
 * Implements the two-phase update strategy from
 * "System Design Interview — Design Search Autocomplete System":
 *
 *   Phase 1 — Flush: drains the TermBuffer → upserts frequencies into DB.
 *   Phase 2 — Rebuild: reads all terms from DB → builds a brand-new Trie
 *              → atomically swaps the live reference via AutocompleteService.
 *
 * The running Trie is never mutated; it keeps serving reads while the new
 * one is being constructed in the background (double-buffer pattern).
 */
@Component
public class TrieRebuildScheduler {

    private static final Logger log = LoggerFactory.getLogger(TrieRebuildScheduler.class);

    private final TermBuffer termBuffer;
    private final FrequencyTermRepository frequencyTermRepository;
    private final AutocompleteService autocompleteService;

    public TrieRebuildScheduler(TermBuffer termBuffer,
                                FrequencyTermRepository frequencyTermRepository,
                                AutocompleteService autocompleteService) {
        this.termBuffer = termBuffer;
        this.frequencyTermRepository = frequencyTermRepository;
        this.autocompleteService = autocompleteService;
    }

    @Scheduled(fixedDelayString = "${autocomplete.rebuild-interval-ms:300000}")
    public void rebuildTrie() {
        Map<String, Long> updates = termBuffer.drainAndReset();

        if (updates.isEmpty()) {
            log.debug("Sin actualizaciones pendientes — reconstrucción omitida");
            return;
        }

        log.info("Iniciando reconstrucción del Trie. Términos a persistir: {}", updates.size());
        long start = System.currentTimeMillis();

        // Phase 1: flush buffer to DB
        for (Map.Entry<String, Long> entry : updates.entrySet()) {
            String term = entry.getKey();
            long delta = entry.getValue();

            frequencyTermRepository.findByTerm(term).ifPresentOrElse(
                existing -> {
                    existing.setFrequency(existing.getFrequency() + delta);
                    existing.setLastUsed(LocalDateTime.now());
                    frequencyTermRepository.save(existing);
                },
                () -> {
                    FrequencyTerm newTerm = new FrequencyTerm();
                    newTerm.setTerm(term);
                    newTerm.setFrequency(delta);
                    newTerm.setCreatedAt(LocalDateTime.now());
                    newTerm.setLastUsed(LocalDateTime.now());
                    frequencyTermRepository.save(newTerm);
                }
            );
        }

        // Phase 2: build new Trie from DB and swap atomically
        Trie newTrie = new Trie();
        List<FrequencyTerm> allTerms = frequencyTermRepository.findAll();
        allTerms.forEach(ft -> newTrie.insert(ft.getTerm(), ft.getFrequency()));

        autocompleteService.swapTrie(newTrie);

        log.info("Trie reconstruido en {}ms con {} términos totales",
                System.currentTimeMillis() - start, allTerms.size());
    }
}
