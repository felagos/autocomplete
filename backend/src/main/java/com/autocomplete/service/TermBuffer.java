package com.autocomplete.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory accumulator for search terms.
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

    private final ConcurrentHashMap<String, AtomicLong> buffer = new ConcurrentHashMap<>();

    /**
     * Thread-safe increment — never blocks the request thread.
     */
    public void record(String term) {
        buffer.computeIfAbsent(term, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * Atomically snapshots and resets all counters.
     * Called exclusively by the rebuild scheduler.
     *
     * @return map of term → accumulated count since last drain
     */
    public Map<String, Long> drainAndReset() {
        Map<String, Long> snapshot = new HashMap<>();
        buffer.forEach((term, counter) -> {
            long count = counter.getAndSet(0);
            if (count > 0) {
                snapshot.put(term, count);
            }
        });
        buffer.entrySet().removeIf(e -> e.getValue().get() == 0);
        log.debug("Buffer drenado: {} términos acumulados", snapshot.size());
        return snapshot;
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }
}
