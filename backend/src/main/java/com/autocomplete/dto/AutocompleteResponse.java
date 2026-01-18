package com.autocomplete.dto;
import java.util.List;
public class AutocompleteResponse {
    private String prefix;
    private List<SuggestionDTO> suggestions;
    private long executionTimeMs;
    public AutocompleteResponse() {
    }
    public AutocompleteResponse(String prefix, List<SuggestionDTO> suggestions, long executionTimeMs) {
        this.prefix = prefix;
        this.suggestions = suggestions;
        this.executionTimeMs = executionTimeMs;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public List<SuggestionDTO> getSuggestions() {
        return suggestions;
    }
    public void setSuggestions(List<SuggestionDTO> suggestions) {
        this.suggestions = suggestions;
    }
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutocompleteResponse that = (AutocompleteResponse) o;
        return executionTimeMs == that.executionTimeMs &&
                java.util.Objects.equals(prefix, that.prefix) &&
                java.util.Objects.equals(suggestions, that.suggestions);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(prefix, suggestions, executionTimeMs);
    }
    @Override
    public String toString() {
        return "AutocompleteResponse{" +
                "prefix='" + prefix + "'" +
                ", suggestions=" + suggestions +
                ", executionTimeMs=" + executionTimeMs +
                "}";
    }
}