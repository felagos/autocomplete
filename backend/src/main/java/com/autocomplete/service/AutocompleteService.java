package com.autocomplete.service;

import com.autocomplete.datastructure.Trie;
import com.autocomplete.dto.FrequencySavedDto;
import com.autocomplete.dto.SuggestionDTO;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.event.TermSavedEvent;
import com.autocomplete.repository.FrequencyTermRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutocompleteService {
    private static final Logger log = LoggerFactory.getLogger(AutocompleteService.class);
    
    private final Trie trie;
    private final FrequencyTermRepository frequencyTermRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${autocomplete.max-suggestions:10}")
    private int maxSuggestions;

    public AutocompleteService(Trie trie,
                               FrequencyTermRepository frequencyTermRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.trie = trie;
        this.frequencyTermRepository = frequencyTermRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @PostConstruct
    public void initializeTrie() {
        log.info("Inicializando Trie con datos de la base de datos");
        List<FrequencyTerm> terms = frequencyTermRepository.findAll();
        
        for (FrequencyTerm term : terms) {
            trie.insert(term.getTerm(), term.getFrequency());
        }
        
        log.info("Trie inicializado con {} términos", terms.size());
    }
    
    public List<SuggestionDTO> getSuggestions(String prefix, int limit) {
        log.info("Buscando sugerencias para prefijo: {}", prefix);
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        int effectiveLimit = Math.min(limit, maxSuggestions);
        return trie.getSuggestions(prefix.trim(), effectiveLimit);
    }
    
    public FrequencySavedDto saveTerm(String term) {
        log.info("Guardando término: {}", term);

        String normalizedTerm = term.trim().toLowerCase();
        long frequency = trie.incrementFrequency(normalizedTerm);

        eventPublisher.publishEvent(new TermSavedEvent(this, normalizedTerm));

        return new FrequencySavedDto(normalizedTerm, frequency);
    }
    
    public List<SuggestionDTO> getTopTerms(int limit) {
        log.info("Obteniendo top {} términos", limit);
        
        List<SuggestionDTO> allWords = trie.getAllWords();
        return allWords.stream()
            .limit(limit)
            .toList();
    }
    

}
