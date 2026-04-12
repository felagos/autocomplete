package com.autocomplete.service;

import com.autocomplete.datastructure.Trie;
import com.autocomplete.entity.FrequencyTerm;
import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrieInitializerService {

    private static final Logger log = LoggerFactory.getLogger(TrieInitializerService.class);

    private final Trie trie;
    private final FrequencyTermRepository frequencyTermRepository;

    public TrieInitializerService(Trie trie, FrequencyTermRepository frequencyTermRepository) {
        this.trie = trie;
        this.frequencyTermRepository = frequencyTermRepository;
    }

    public void initialize() {
        log.info("Inicializando Trie con datos de la base de datos");
        List<FrequencyTerm> terms = frequencyTermRepository.findAll();

        for (FrequencyTerm term : terms) {
            trie.insert(term.getTerm(), term.getFrequency());
        }

        log.info("Trie inicializado con {} términos", terms.size());
    }
}
