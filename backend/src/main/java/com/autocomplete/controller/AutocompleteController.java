package com.autocomplete.controller;

import com.autocomplete.dto.*;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.service.AutocompleteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para el sistema de autocompletado
 * Endpoints:
 * - GET /api/autocomplete/suggest - Obtener sugerencias
 * - POST /api/autocomplete/submit - Guardar término
 * - GET /api/autocomplete/top - Obtener términos más populares
 */
@RestController
@RequestMapping("/api/autocomplete")
@RequiredArgsConstructor
public class AutocompleteController {
    
    private final AutocompleteService autocompleteService;
    
    /**
     * Endpoint para obtener sugerencias basadas en un prefijo
     */
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
    
    /**
     * Endpoint para guardar un término seleccionado
     * Incrementa la frecuencia si ya existe
     */
    @PostMapping("/submit")
    public ResponseEntity<FrequencyTerm> submitTerm(@Valid @RequestBody TermSubmitRequest request) {
        FrequencyTerm savedTerm = autocompleteService.saveTerm(request.getTerm());
        return ResponseEntity.ok(savedTerm);
    }
    
    /**
     * Endpoint para obtener los términos más populares
     */
    @GetMapping("/top")
    public ResponseEntity<List<SuggestionDTO>> getTopTerms(
            @RequestParam(defaultValue = "10") int limit) {
        List<SuggestionDTO> topTerms = autocompleteService.getTopTerms(limit);
        return ResponseEntity.ok(topTerms);
    }
    
    /**
     * Endpoint para inicializar datos de ejemplo
     */
    @PostMapping("/init")
    public ResponseEntity<String> initializeData() {
        autocompleteService.initializeSampleData();
        return ResponseEntity.ok("Datos de ejemplo inicializados correctamente");
    }
}
