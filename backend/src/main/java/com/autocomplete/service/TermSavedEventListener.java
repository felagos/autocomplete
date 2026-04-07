package com.autocomplete.service;

import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.event.TermSavedEvent;
import com.autocomplete.event.TrieUpdateEvent;
import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TermSavedEventListener {

    private static final Logger log = LoggerFactory.getLogger(TermSavedEventListener.class);

    private final FrequencyTermRepository frequencyTermRepository;
    private final RedisTemplate<String, TrieUpdateEvent> redisTemplate;
    private final ChannelTopic trieUpdateTopic;

    public TermSavedEventListener(FrequencyTermRepository frequencyTermRepository,
                                   RedisTemplate<String, TrieUpdateEvent> redisTemplate,
                                   ChannelTopic trieUpdateTopic) {
        this.frequencyTermRepository = frequencyTermRepository;
        this.redisTemplate = redisTemplate;
        this.trieUpdateTopic = trieUpdateTopic;
    }

    @Async
    @EventListener
    @Transactional
    public void handleTermSaved(TermSavedEvent event) {
        String term = event.getTerm();
        log.debug("Persistiendo término de forma asíncrona: {}", term);

        FrequencyTerm savedTerm = frequencyTermRepository.findByTerm(term)
                .map(existingTerm -> {
                    existingTerm.incrementFrequency();
                    return frequencyTermRepository.save(existingTerm);
                })
                .orElseGet(() -> {
                    FrequencyTerm newTerm = new FrequencyTerm();
                    newTerm.setTerm(term);
                    newTerm.setFrequency(1L);
                    return frequencyTermRepository.save(newTerm);
                });

        try {
            TrieUpdateEvent redisEvent = new TrieUpdateEvent(savedTerm.getTerm(), savedTerm.getFrequency());
            redisTemplate.convertAndSend(trieUpdateTopic.getTopic(), redisEvent);
            log.debug("Evento Redis publicado para término: {}", term);
        } catch (Exception e) {
            log.warn("Error publicando evento a Redis: {}", e.getMessage());
        }
    }
}
