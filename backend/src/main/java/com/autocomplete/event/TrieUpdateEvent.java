package com.autocomplete.event;

import java.io.Serializable;

public class TrieUpdateEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String term;
    private Long frequency;
    
    public TrieUpdateEvent() {
    }
    
    public TrieUpdateEvent(String term, Long frequency) {
        this.term = term;
        this.frequency = frequency;
    }
    
    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    public Long getFrequency() {
        return frequency;
    }
    
    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }
    
    @Override
    public String toString() {
        return "TrieUpdateEvent{" +
                "term='" + term + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
