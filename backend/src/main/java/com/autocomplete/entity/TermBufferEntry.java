package com.autocomplete.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "term_buffer")
public class TermBufferEntry {

    @Id
    @Column(nullable = false, unique = true, length = 255)
    private String term;

    @Column(nullable = false)
    private Long count;

    public TermBufferEntry() {
    }

    public TermBufferEntry(String term, Long count) {
        this.term = term;
        this.count = count;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
