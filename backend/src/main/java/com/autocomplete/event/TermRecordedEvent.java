package com.autocomplete.event;

import org.springframework.context.ApplicationEvent;

public class TermRecordedEvent extends ApplicationEvent {

    private final String term;

    public TermRecordedEvent(Object source, String term) {
        super(source);
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}
