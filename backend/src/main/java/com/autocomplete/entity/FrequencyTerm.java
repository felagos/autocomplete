package com.autocomplete.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa la tabla de frecuencias (frequency_table)
 * Almacena los términos y su frecuencia de uso para el sistema de autocompletado
 * Basado en los conceptos de System Design Interview para sistemas de búsqueda
 */
@Entity
@Table(name = "frequency_table", indexes = {
    @Index(name = "idx_term", columnList = "term"),
    @Index(name = "idx_frequency", columnList = "frequency DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyTerm {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String term;
    
    @Column(nullable = false)
    private Long frequency;
    
    @Column(name = "last_used")
    private LocalDateTime lastUsed;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUsed = LocalDateTime.now();
        if (frequency == null) {
            frequency = 1L;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastUsed = LocalDateTime.now();
    }
    
    public void incrementFrequency() {
        this.frequency++;
    }
}
