package com.autocomplete.datastructure;

import com.autocomplete.dto.SuggestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementación de Trie Tree para autocompletado eficiente
 * Complejidad temporal:
 * - Insert: O(m) donde m es la longitud de la palabra
 * - Search: O(m + k) donde k es el número de sugerencias
 * - Space: O(n * m) donde n es el número de palabras
 */
@Component
public class Trie {
    private static final Logger log = LoggerFactory.getLogger(Trie.class);
    private final TrieNode root;
    
    public Trie() {
        this.root = new TrieNode();
    }
    
    /**
     * Inserta una palabra en el Trie
     * Si la palabra ya existe, incrementa su frecuencia
     */
    public void insert(String word, long frequency) {
        if (word == null || word.isEmpty()) {
            return;
        }
        
        String normalizedWord = word.toLowerCase().trim();
        TrieNode current = root;
        
        for (char c : normalizedWord.toCharArray()) {
            current = current.addChild(c);
        }
        
        current.setEndOfWord(true);
        current.setWord(normalizedWord);
        current.setFrequency(frequency);
        
        log.debug("Palabra insertada: {} con frecuencia: {}", normalizedWord, frequency);
    }
    
    /**
     * Inserta una palabra con frecuencia inicial de 1
     */
    public void insert(String word) {
        insert(word, 1L);
    }
    
    /**
     * Incrementa la frecuencia de una palabra existente o la inserta si no existe
     */
    public void incrementFrequency(String word) {
        if (word == null || word.isEmpty()) {
            return;
        }
        
        String normalizedWord = word.toLowerCase().trim();
        TrieNode current = root;
        
        for (char c : normalizedWord.toCharArray()) {
            current = current.addChild(c);
        }
        
        if (current.isEndOfWord()) {
            current.incrementFrequency();
        } else {
            current.setEndOfWord(true);
            current.setWord(normalizedWord);
            current.setFrequency(1L);
        }
        
        log.debug("Frecuencia actualizada para: {} a {}", normalizedWord, current.getFrequency());
    }
    
    /**
     * Busca sugerencias basadas en el prefijo
     * Retorna las top N sugerencias ordenadas por frecuencia (mayor a menor)
     */
    public List<SuggestionDTO> getSuggestions(String prefix, int limit) {
        if (prefix == null || prefix.isEmpty()) {
            return List.of();
        }
        
        String normalizedPrefix = prefix.toLowerCase().trim();
        TrieNode current = root;
        
        // Navegar hasta el final del prefijo
        for (char c : normalizedPrefix.toCharArray()) {
            if (!current.hasChild(c)) {
                log.debug("No se encontraron sugerencias para el prefijo: {}", normalizedPrefix);
                return List.of();
            }
            current = current.getChild(c);
        }
        
        // Recolectar todas las palabras que empiezan con el prefijo
        List<SuggestionDTO> suggestions = new ArrayList<>();
        collectAllWords(current, suggestions);
        
        // Usar PriorityQueue para obtener las top N sugerencias por frecuencia
        PriorityQueue<SuggestionDTO> topSuggestions = new PriorityQueue<>(
            (a, b) -> Long.compare(b.getFrequency(), a.getFrequency())
        );
        
        topSuggestions.addAll(suggestions);
        
        List<SuggestionDTO> result = new ArrayList<>();
        int count = 0;
        while (!topSuggestions.isEmpty() && count < limit) {
            result.add(topSuggestions.poll());
            count++;
        }
        
        log.info("Encontradas {} sugerencias para el prefijo: {}", result.size(), normalizedPrefix);
        return result;
    }
    
    /**
     * Recolecta recursivamente todas las palabras desde un nodo dado
     */
    private void collectAllWords(TrieNode node, List<SuggestionDTO> words) {
        if (node == null) {
            return;
        }
        
        if (node.isEndOfWord()) {
            words.add(new SuggestionDTO(node.getWord(), node.getFrequency()));
        }
        
        for (TrieNode child : node.getChildren().values()) {
            collectAllWords(child, words);
        }
    }
    
    /**
     * Verifica si una palabra existe en el Trie
     */
    public boolean search(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        
        String normalizedWord = word.toLowerCase().trim();
        TrieNode current = root;
        
        for (char c : normalizedWord.toCharArray()) {
            if (!current.hasChild(c)) {
                return false;
            }
            current = current.getChild(c);
        }
        
        return current.isEndOfWord();
    }
    
    /**
     * Verifica si existe alguna palabra con el prefijo dado
     */
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }
        
        String normalizedPrefix = prefix.toLowerCase().trim();
        TrieNode current = root;
        
        for (char c : normalizedPrefix.toCharArray()) {
            if (!current.hasChild(c)) {
                return false;
            }
            current = current.getChild(c);
        }
        
        return true;
    }
    
    /**
     * Obtiene todas las palabras almacenadas en el Trie
     * ordenadas por frecuencia (mayor a menor)
     */
    public List<SuggestionDTO> getAllWords() {
        List<SuggestionDTO> allWords = new ArrayList<>();
        collectAllWords(root, allWords);
        
        // Ordenar por frecuencia descendente
        allWords.sort((a, b) -> Long.compare(b.getFrequency(), a.getFrequency()));
        
        return allWords;
    }
    
    /**
     * Limpia todo el contenido del Trie
     */
    public void clear() {
        root.getChildren().clear();
        log.info("Trie limpiado");
    }
}
