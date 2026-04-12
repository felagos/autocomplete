package com.autocomplete.service;

import com.autocomplete.datastructure.Trie;
import com.autocomplete.dto.FrequencySavedDto;
import com.autocomplete.dto.SuggestionDTO;
import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AutocompleteService {
    private static final Logger log = LoggerFactory.getLogger(AutocompleteService.class);
    
    private final AtomicReference<Trie> trieRef;
    private final FrequencyTermRepository frequencyTermRepository;
    private final TermBuffer termBuffer;

    @Value("${autocomplete.max-suggestions:10}")
    private int maxSuggestions;

    public AutocompleteService(Trie trie,
                               FrequencyTermRepository frequencyTermRepository,
                               TermBuffer termBuffer) {
        this.trieRef = new AtomicReference<>(trie);
        this.frequencyTermRepository = frequencyTermRepository;
        this.termBuffer = termBuffer;
    }
    
    public List<SuggestionDTO> getSuggestions(String prefix, int limit) {
        log.info("Buscando sugerencias para prefijo: {}", prefix);
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        int effectiveLimit = Math.min(limit, maxSuggestions);
        return trieRef.get().getSuggestions(prefix.trim(), effectiveLimit);
    }
    
    public FrequencySavedDto saveTerm(String term) {
        log.info("Registrando término en buffer: {}", term);

        String normalizedTerm = term.trim().toLowerCase();

        termBuffer.record(normalizedTerm);

        long currentFrequency = trieRef.get().search(normalizedTerm)
                ? frequencyTermRepository.findByTerm(normalizedTerm)
                        .map(ft -> ft.getFrequency() + 1)
                        .orElse(1L)
                : 1L;

        return new FrequencySavedDto(normalizedTerm, currentFrequency);
    }
    
    public List<SuggestionDTO> getTopTerms(int limit) {
        log.info("Obteniendo top {} términos", limit);
        
        List<SuggestionDTO> allWords = trieRef.get().getAllWords();
        return allWords.stream()
            .limit(limit)
            .toList();
    }

    /**
     * Atomically replaces the active Trie with a freshly built one.
     * Called exclusively by TrieRebuildScheduler after a full rebuild.
     */
    void swapTrie(Trie newTrie) {
        trieRef.set(newTrie);
        log.info("Trie reemplazado atómicamente");
    }

}
