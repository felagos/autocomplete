package com.autocomplete.service;

import com.autocomplete.datastructure.Trie;
import com.autocomplete.dto.SuggestionDTO;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.event.TrieUpdateEvent;
import com.autocomplete.repository.FrequencyTermRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de autocompletado que implementa conceptos de System Design Interview:
 * - Trie Tree para búsqueda eficiente O(m + k)
 * - Frequency-based ranking
 * - Estructura de datos en memoria para máxima performance
 * - Persistencia en base de datos para durabilidad
 * - Sincronización distribuida via Redis Pub/Sub
 */
@Service
public class AutocompleteService {
    private static final Logger log = LoggerFactory.getLogger(AutocompleteService.class);
    
    private final Trie trie;
    private final FrequencyTermRepository frequencyTermRepository;
    private final RedisTemplate<String, TrieUpdateEvent> redisTemplate;
    private final ChannelTopic trieUpdateTopic;
    
    @Value("${autocomplete.max-suggestions:10}")
    private int maxSuggestions;
    
    public AutocompleteService(Trie trie, 
                              FrequencyTermRepository frequencyTermRepository,
                              RedisTemplate<String, TrieUpdateEvent> redisTemplate,
                              ChannelTopic trieUpdateTopic) {
        this.trie = trie;
        this.frequencyTermRepository = frequencyTermRepository;
        this.redisTemplate = redisTemplate;
        this.trieUpdateTopic = trieUpdateTopic;
    }
    
    /**
     * Inicializa el Trie con datos existentes en la base de datos
     */
    @PostConstruct
    public void initializeTrie() {
        log.info("Inicializando Trie con datos de la base de datos");
        List<FrequencyTerm> terms = frequencyTermRepository.findAll();
        
        for (FrequencyTerm term : terms) {
            trie.insert(term.getTerm(), term.getFrequency());
        }
        
        log.info("Trie inicializado con {} términos", terms.size());
    }
    
    /**
     * Obtiene sugerencias basadas en el prefijo usando Trie
     * Complejidad: O(m + k) donde m es la longitud del prefijo y k el número de sugerencias
     */
    public List<SuggestionDTO> getSuggestions(String prefix, int limit) {
        log.info("Buscando sugerencias para prefijo: {}", prefix);
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        int effectiveLimit = Math.min(limit, maxSuggestions);
        return trie.getSuggestions(prefix.trim(), effectiveLimit);
    }
    
    /**
     * Guarda o actualiza un término incrementando su frecuencia
     * Actualiza tanto el Trie como la base de datos para persistencia
     * Publica evento a Redis para sincronizar otras instancias
     */
    @Transactional
    public FrequencyTerm saveTerm(String term) {
        log.info("Guardando término: {}", term);
        
        String normalizedTerm = term.trim().toLowerCase();
        
        // Actualizar el Trie local
        trie.incrementFrequency(normalizedTerm);
        
        // Persistir en base de datos
        FrequencyTerm savedTerm = frequencyTermRepository.findByTerm(normalizedTerm)
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
        
        // Publicar evento a Redis para sincronizar otras instancias
        try {
            TrieUpdateEvent event = new TrieUpdateEvent(savedTerm.getTerm(), savedTerm.getFrequency());
            redisTemplate.convertAndSend(trieUpdateTopic.getTopic(), event);
            log.debug("Evento de actualización publicado a Redis: {}", event);
        } catch (Exception e) {
            log.warn("Error publicando evento a Redis (la instancia local ya está actualizada): {}", e.getMessage());
        }
        
        return savedTerm;
    }
    
    /**
     * Obtiene los términos más populares del Trie
     */
    public List<SuggestionDTO> getTopTerms(int limit) {
        log.info("Obteniendo top {} términos", limit);
        
        List<SuggestionDTO> allWords = trie.getAllWords();
        return allWords.stream()
            .limit(limit)
            .toList();
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
