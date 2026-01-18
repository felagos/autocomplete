package com.autocomplete.repository;

import com.autocomplete.entity.FrequencyTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FrequencyTermRepository extends JpaRepository<FrequencyTerm, Long> {
    
    Optional<FrequencyTerm> findByTerm(String term);
    
    /**
     * Encuentra términos que comienzan con el prefijo dado, 
     * ordenados por frecuencia descendente
     */
    @Query("SELECT f FROM FrequencyTerm f WHERE LOWER(f.term) LIKE LOWER(CONCAT(:prefix, '%')) " +
           "ORDER BY f.frequency DESC, f.term ASC")
    List<FrequencyTerm> findByTermStartingWithOrderByFrequencyDesc(
        @Param("prefix") String prefix
    );
    
    /**
     * Obtiene los top N términos por frecuencia
     */
    @Query("SELECT f FROM FrequencyTerm f ORDER BY f.frequency DESC, f.term ASC")
    List<FrequencyTerm> findTopByFrequency();
}
