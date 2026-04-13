package com.autocomplete.listener;

import com.autocomplete.event.TermRecordedEvent;
import com.autocomplete.service.TermBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TermRecordedEventListener {

    private static final Logger log = LoggerFactory.getLogger(TermRecordedEventListener.class);

    private final TermBuffer termBuffer;

    public TermRecordedEventListener(TermBuffer termBuffer) {
        this.termBuffer = termBuffer;
    }

    @Async
    @EventListener
    public void handleTermRecorded(TermRecordedEvent event) {
        log.debug("Registrando término en buffer de forma asíncrona: {}", event.getTerm());
        termBuffer.record(event.getTerm());
    }
}
