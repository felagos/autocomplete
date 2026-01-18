package com.autocomplete.dto;

public class SuggestionDTO {
    private String term;
    private Long frequency;

    public SuggestionDTO() {
    }

    public SuggestionDTO(String term, Long frequency) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuggestionDTO that = (SuggestionDTO) o;
        return java.util.Objects.equals(term, that.term)
                && java.util.Objects.equals(frequency, that.frequency);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(term, frequency);
    }

    @Override
    public String toString() {
        return "SuggestionDTO{"
                + "term='" + term + "'"
                + ", frequency=" + frequency
                + "}";
    }
}