package com.autocomplete.service;

import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.event.TermSavedEvent;
import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TermSavedEventListener {

    private static final Logger log = LoggerFactory.getLogger(TermSavedEventListener.class);

    private final FrequencyTermRepository frequencyTermRepository;

    public TermSavedEventListener(FrequencyTermRepository frequencyTermRepository) {
        this.frequencyTermRepository = frequencyTermRepository;
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

        log.debug("Término persistido: {} con frecuencia: {}", savedTerm.getTerm(), savedTerm.getFrequency());
    }
}
