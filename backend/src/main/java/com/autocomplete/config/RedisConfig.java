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

@Configuration
public class RedisConfig {
    
    public static final String TRIE_UPDATE_TOPIC = "trie-updates";
    
    @Bean
    public RedisTemplate<String, TrieUpdateEvent> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, TrieUpdateEvent> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        template.setKeySerializer(new StringRedisSerializer());
        
        Jackson2JsonRedisSerializer<TrieUpdateEvent> serializer = 
            new Jackson2JsonRedisSerializer<>(TrieUpdateEvent.class);
        template.setValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean
    public ChannelTopic trieUpdateTopic() {
        return new ChannelTopic(TRIE_UPDATE_TOPIC);
    }
    
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
    
    @Bean
    public MessageListenerAdapter listenerAdapter(TrieUpdateListener listener) {
        return new MessageListenerAdapter(listener, "onMessage");
    }
}
