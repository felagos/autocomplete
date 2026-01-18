package com.autocomplete.config;

import com.autocomplete.datastructure.Trie;
import com.autocomplete.event.TrieUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Listener que escucha eventos de actualización del Trie desde Redis
 * Mantiene sincronizado el Trie local cuando otras instancias actualizan datos
 */
@Component
public class TrieUpdateListener implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(TrieUpdateListener.class);
    
    private final Trie trie;
    private final Jackson2JsonRedisSerializer<TrieUpdateEvent> serializer;
    
    public TrieUpdateListener(Trie trie) {
        this.trie = trie;
        this.serializer = new Jackson2JsonRedisSerializer<>(TrieUpdateEvent.class);
    }
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            TrieUpdateEvent event = serializer.deserialize(message.getBody());
            
            if (event != null) {
                log.info("Recibido evento de actualización del Trie: {}", event);
                trie.insert(event.getTerm(), event.getFrequency());
                log.debug("Trie local actualizado con término: {} - frecuencia: {}", 
                         event.getTerm(), event.getFrequency());
            }
        } catch (Exception e) {
            log.error("Error procesando evento de actualización del Trie", e);
        }
    }
}
