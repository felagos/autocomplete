package com.autocomplete.dto;

public class FrequencySavedDto {

    private String term;
    private long frequency;

    public FrequencySavedDto() {
    }

    public FrequencySavedDto(String term, long frequency) {
        this.term = term;
        this.frequency = frequency;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "FrequencySavedDto{" +
                "term='" + term + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
