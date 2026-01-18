package com.autocomplete.controller;
import com.autocomplete.dto.*;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.service.AutocompleteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/autocomplete")
public class AutocompleteController {
    
    private final AutocompleteService autocompleteService;

    public AutocompleteController(AutocompleteService autocompleteService) {
        this.autocompleteService = autocompleteService;
    }

    @GetMapping("/suggest")
    public ResponseEntity<AutocompleteResponse> getSuggestions(
            @RequestParam String prefix,
            @RequestParam(defaultValue = "10") int limit) {
        long startTime = System.currentTimeMillis();
        List<SuggestionDTO> suggestions = autocompleteService.getSuggestions(prefix, limit);
        long executionTime = System.currentTimeMillis() - startTime;
        AutocompleteResponse response = new AutocompleteResponse(
            prefix,
            suggestions,
            executionTime
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submit")
    public ResponseEntity<FrequencyTerm> submitTerm(@Valid @RequestBody TermSubmitRequest request) {
        FrequencyTerm savedTerm = autocompleteService.saveTerm(request.getTerm());
        return ResponseEntity.ok(savedTerm);
    }

    @GetMapping("/top")
    public ResponseEntity<List<SuggestionDTO>> getTopTerms(
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionDTO> topTerms = autocompleteService.getTopTerms(limit);
        return ResponseEntity.ok(topTerms);
    }

    @PostMapping("/init")
    public ResponseEntity<String> initializeData() {
        autocompleteService.initializeSampleData();
        return ResponseEntity.ok("Datos de ejemplo inicializados correctamente");
    }
}