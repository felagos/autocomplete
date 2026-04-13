package com.autocomplete.service;

import com.autocomplete.repository.TermBufferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * PostgreSQL-backed accumulator for search terms.
 *
 * Sits between the write path (saveTerm) and the Trie:
 * every search is recorded here without touching the Trie at all.
 * A scheduled job drains this buffer, persists to DB, and triggers
 * a full Trie rebuild — exactly the "Data Collection Service" pattern
 * described in System Design Interview ch. "Design Search Autocomplete".
 */
@Component
public class TermBuffer {

    private static final Logger log = LoggerFactory.getLogger(TermBuffer.class);

    private final TermBufferRepository termBufferRepository;

    public TermBuffer(TermBufferRepository termBufferRepository) {
        this.termBufferRepository = termBufferRepository;
    }

    /**
     * Thread-safe upsert — delegates to a native INSERT … ON CONFLICT DO UPDATE.
     */
    @Transactional
    public void record(String term) {
        termBufferRepository.upsertTerm(term);
    }

    /**
     * Snapshots all buffered counts and clears the table.
     * Called exclusively by the rebuild scheduler.
     *
     * @return map of term → accumulated count since last drain
     */
    @Transactional
    public Map<String, Long> drainAndReset() {
        Map<String, Long> snapshot = termBufferRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getTerm(),
                        e -> e.getCount()
                ));
        termBufferRepository.deleteAllInBatch();
        log.debug("Buffer drenado: {} términos acumulados", snapshot.size());
        return snapshot;
    }

    public boolean isEmpty() {
        return termBufferRepository.count() == 0;
    }
}
