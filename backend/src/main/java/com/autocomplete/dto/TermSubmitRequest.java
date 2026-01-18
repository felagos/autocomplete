package com.autocomplete.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TermSubmitRequest {
    
    @NotBlank(message = "El término no puede estar vacío")
    @Size(min = 1, max = 255, message = "El término debe tener entre 1 y 255 caracteres")
    private String term;
    
    public TermSubmitRequest() {
    }
    
    public TermSubmitRequest(String term) {
        this.term = term;
    }
    
    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TermSubmitRequest that = (TermSubmitRequest) o;
        return java.util.Objects.equals(term, that.term);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(term);
    }
    
    @Override
    public String toString() {
        return "TermSubmitRequest{" +
                "term='" + term + "'" +
                "}";
    }
}
