package com.autocomplete.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Nodo del Trie Tree
 * Cada nodo representa un carácter en el árbol
 */
public class TrieNode {
    private Map<Character, TrieNode> children;
    private boolean isEndOfWord;
    private String word;
    private long frequency;
    
    public TrieNode() {
        this.children = new HashMap<>();
        this.isEndOfWord = false;
        this.frequency = 0L;
    }
    
    public Map<Character, TrieNode> getChildren() {
        return children;
    }
    
    public void setChildren(Map<Character, TrieNode> children) {
        this.children = children;
    }
    
    public boolean isEndOfWord() {
        return isEndOfWord;
    }
    
    public void setEndOfWord(boolean endOfWord) {
        isEndOfWord = endOfWord;
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String word) {
        this.word = word;
    }
    
    public long getFrequency() {
        return frequency;
    }
    
    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
    
    /**
     * Verifica si el nodo tiene un hijo con el carácter dado
     */
    public boolean hasChild(char c) {
        return children.containsKey(c);
    }
    
    /**
     * Obtiene el nodo hijo correspondiente al carácter
     */
    public TrieNode getChild(char c) {
        return children.get(c);
    }
    
    /**
     * Agrega un nodo hijo con el carácter dado
     */
    public TrieNode addChild(char c) {
        children.putIfAbsent(c, new TrieNode());
        return children.get(c);
    }
    
    /**
     * Incrementa la frecuencia de la palabra
     */
    public void incrementFrequency() {
        this.frequency++;
    }
}
