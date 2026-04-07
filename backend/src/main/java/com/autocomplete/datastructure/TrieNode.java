package com.autocomplete.datastructure;

import java.util.HashMap;
import java.util.Map;

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
    
    public boolean hasChild(char c) {
        return children.containsKey(c);
    }
    
    public TrieNode getChild(char c) {
        return children.get(c);
    }
    
    public TrieNode addChild(char c) {
        children.putIfAbsent(c, new TrieNode());
        return children.get(c);
    }
    
    public void incrementFrequency() {
        this.frequency++;
    }
}
