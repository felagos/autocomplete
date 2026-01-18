package com.autocomplete.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutocompleteResponse {
    
    private String prefix;
    private List<SuggestionDTO> suggestions;
    private long executionTimeMs;
}
