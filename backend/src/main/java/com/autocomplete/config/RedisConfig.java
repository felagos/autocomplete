package com.autocomplete.config;

import com.autocomplete.event.TrieUpdateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Configuración de Redis para sincronización distribuida del Trie
 * Implementa patrón Pub/Sub para mantener consistencia entre instancias
 */
@Configuration
public class RedisConfig {
    
    public static final String TRIE_UPDATE_TOPIC = "trie-updates";
    
    /**
     * Template de Redis configurado para publicar eventos
     */
    @Bean
    public RedisTemplate<String, TrieUpdateEvent> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TrieUpdateEvent> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Serialización de claves como String
        template.setKeySerializer(new StringRedisSerializer());
        
        // Serialización de valores como JSON
        Jackson2JsonRedisSerializer<TrieUpdateEvent> serializer = 
            new Jackson2JsonRedisSerializer<>(TrieUpdateEvent.class);
        template.setValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
    
    /**
     * Tópico de Redis para eventos de actualización del Trie
     */
    @Bean
    public ChannelTopic trieUpdateTopic() {
        return new ChannelTopic(TRIE_UPDATE_TOPIC);
    }
    
    /**
     * Contenedor de listeners de mensajes de Redis
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic trieUpdateTopic) {
        
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, trieUpdateTopic);
        return container;
    }
    
    /**
     * Adaptador para el listener de mensajes
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(TrieUpdateListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}
