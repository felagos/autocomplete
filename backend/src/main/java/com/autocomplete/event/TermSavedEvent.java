package com.autocomplete.event;

import org.springframework.context.ApplicationEvent;

public class TermSavedEvent extends ApplicationEvent {

    private final String term;

    public TermSavedEvent(Object source, String term) {
        super(source);
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}
