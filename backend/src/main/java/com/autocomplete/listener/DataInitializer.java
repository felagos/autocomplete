package com.autocomplete.listener;

import com.autocomplete.repository.FrequencyTermRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final FrequencyTermRepository repository;
    private final DataSource dataSource;

    public DataInitializer(FrequencyTermRepository repository, DataSource dataSource) {
        this.repository = repository;
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) {
        long count = repository.count();
        if (count == 0) {
            log.info("frequency_table is empty — running initial data seed...");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("data.sql"));
            populator.execute(dataSource);
            log.info("Initial data seeded successfully ({} records inserted).", repository.count());
        } else {
            log.info("frequency_table already has {} records — skipping seed.", count);
        }
    }
}
