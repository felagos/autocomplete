package com.autocomplete.service;

import com.autocomplete.dto.SuggestionDTO;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de autocompletado que implementa conceptos de System Design Interview:
 * - Búsqueda eficiente usando índices de base de datos
 * - Frequency-based ranking
 * - Caching para optimizar búsquedas repetidas
 * - Write-through cache pattern
 */
@Service
public class AutocompleteService {
    private static final Logger log = LoggerFactory.getLogger(AutocompleteService.class);
    
    private final FrequencyTermRepository frequencyTermRepository;
    
    @Value("${autocomplete.max-suggestions:10}")
    private int maxSuggestions;
    
    public AutocompleteService(FrequencyTermRepository frequencyTermRepository) {
        this.frequencyTermRepository = frequencyTermRepository;
    }
    
    /**
     * Obtiene sugerencias basadas en el prefijo
     * Cachea resultados para mejorar performance
     */
    @Cacheable(value = "autocomplete", key = "#prefix + '_' + #limit")
    public List<SuggestionDTO> getSuggestions(String prefix, int limit) {
        log.info("Buscando sugerencias para prefijo: {}", prefix);
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        List<FrequencyTerm> terms = frequencyTermRepository
            .findByTermStartingWithOrderByFrequencyDesc(prefix.trim().toLowerCase());
        
        return terms.stream()
            .limit(Math.min(limit, maxSuggestions))
            .map(term -> new SuggestionDTO(term.getTerm(), term.getFrequency()))
            .collect(Collectors.toList());
    }
    
    /**
     * Guarda o actualiza un término incrementando su frecuencia
     * Invalida cache para reflejar cambios
     */
    @Transactional
    @CacheEvict(value = "autocomplete", allEntries = true)
    public FrequencyTerm saveTerm(String term) {
        log.info("Guardando término: {}", term);
        
        String normalizedTerm = term.trim().toLowerCase();
        
        return frequencyTermRepository.findByTerm(normalizedTerm)
            .map(existingTerm -> {
                existingTerm.incrementFrequency();
                return frequencyTermRepository.save(existingTerm);
            })
            .orElseGet(() -> {
                FrequencyTerm newTerm = new FrequencyTerm();
                newTerm.setTerm(normalizedTerm);
                newTerm.setFrequency(1L);
                return frequencyTermRepository.save(newTerm);
            });
    }
    
    /**
     * Obtiene los términos más populares
     */
    @Cacheable(value = "topTerms")
    public List<SuggestionDTO> getTopTerms(int limit) {
        log.info("Obteniendo top {} términos", limit);
        
        return frequencyTermRepository.findTopByFrequency().stream()
            .limit(limit)
            .map(term -> new SuggestionDTO(term.getTerm(), term.getFrequency()))
            .collect(Collectors.toList());
    }
    
    /**
     * Inicializa datos de ejemplo
     */
    @Transactional
    public void initializeSampleData() {
        if (frequencyTermRepository.count() == 0) {
            log.info("Inicializando datos de ejemplo");
            
            String[] sampleTerms = {
                "javascript", "java", "python", "react", "angular", "vue",
                "typescript", "spring", "django", "flask", "nodejs", "express",
                "mongodb", "postgresql", "mysql", "redis", "docker", "kubernetes",
                "aws", "azure", "google cloud", "spring boot", "react native"
            };
            
            for (int i = 0; i < sampleTerms.length; i++) {
                FrequencyTerm term = new FrequencyTerm();
                term.setTerm(sampleTerms[i]);
                term.setFrequency((long) (sampleTerms.length - i) * 100);
                frequencyTermRepository.save(term);
            }
            
            log.info("Datos de ejemplo inicializados");
        }
    }
}
