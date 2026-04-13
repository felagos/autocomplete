package com.autocomplete.repository;

import com.autocomplete.entity.TermBufferEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TermBufferRepository extends JpaRepository<TermBufferEntry, String> {

    /**
     * Thread-safe upsert: inserts the term with count=1 or increments the
     * existing count by 1. Works on both H2 2.x and PostgreSQL.
     */
    @Modifying
    @Query(value = "INSERT INTO term_buffer (term, count) VALUES (:term, 1) " +
                   "ON CONFLICT (term) DO UPDATE SET count = term_buffer.count + 1",
           nativeQuery = true)
    void upsertTerm(@Param("term") String term);
}
