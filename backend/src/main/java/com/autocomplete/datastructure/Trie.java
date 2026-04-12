package com.autocomplete.datastructure;

import com.autocomplete.dto.SuggestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@Component
public class Trie {
    private static final Logger log = LoggerFactory.getLogger(Trie.class);
    private final TrieNode root;
    
    public Trie() {
        this.root = new TrieNode();
    }
    
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
    
    public void insert(String word) {
        insert(word, 1L);
    }
    
    public long incrementFrequency(String word) {
        if (word == null || word.isEmpty()) {
            return 0L;
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
        return current.getFrequency();
    }
    
    public List<SuggestionDTO> getSuggestions(String prefix, int limit) {
        if (prefix == null || prefix.isEmpty()) {
            return List.of();
        }
        
        String normalizedPrefix = prefix.toLowerCase().trim();
        TrieNode current = root;
        
        for (char c : normalizedPrefix.toCharArray()) {
            if (!current.hasChild(c)) {
                log.debug("No se encontraron sugerencias para el prefijo: {}", normalizedPrefix);
                return List.of();
            }
            current = current.getChild(c);
        }
        
        PriorityQueue<SuggestionDTO> topSuggestions = new PriorityQueue<>(
            limit, (a, b) -> Long.compare(a.getFrequency(), b.getFrequency())
        );
        collectTopWords(current, topSuggestions, limit);
        
        List<SuggestionDTO> result = new ArrayList<>(topSuggestions);
        result.sort((a, b) -> Long.compare(b.getFrequency(), a.getFrequency()));
        
        log.info("Encontradas {} sugerencias para el prefijo: {}", result.size(), normalizedPrefix);
        return result;
    }
    
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

    private void collectTopWords(TrieNode node, PriorityQueue<SuggestionDTO> heap, int limit) {
        if (node == null) {
            return;
        }

        if (node.isEndOfWord()) {
            if (heap.size() < limit) {
                heap.offer(new SuggestionDTO(node.getWord(), node.getFrequency()));
            } else if (node.getFrequency() > heap.peek().getFrequency()) {
                heap.poll();
                heap.offer(new SuggestionDTO(node.getWord(), node.getFrequency()));
            }
        }

        for (TrieNode child : node.getChildren().values()) {
            collectTopWords(child, heap, limit);
        }
    }
    
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
    
    public List<SuggestionDTO> getAllWords() {
        List<SuggestionDTO> allWords = new ArrayList<>();
        collectAllWords(root, allWords);
        
        allWords.sort((a, b) -> Long.compare(b.getFrequency(), a.getFrequency()));
        
        return allWords;
    }
}
