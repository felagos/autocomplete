package com.autocomplete.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "frequency_table", indexes = {
    @Index(name = "idx_term", columnList = "term"),
    @Index(name = "idx_frequency", columnList = "frequency DESC")
})
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
    public FrequencyTerm() {
    }
    public FrequencyTerm(Long id, String term, Long frequency, LocalDateTime lastUsed, LocalDateTime createdAt) {
        this.id = id;
        this.term = term;
        this.frequency = frequency;
        this.lastUsed = lastUsed;
        this.createdAt = createdAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public Long getFrequency() {
        return frequency;
    }
    public void setFrequency(Long frequency) {
        this.frequency = frequency;
    }
    public LocalDateTime getLastUsed() {
        return lastUsed;
    }
    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FrequencyTerm that = (FrequencyTerm) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(term, that.term) &&
                java.util.Objects.equals(frequency, that.frequency) &&
                java.util.Objects.equals(lastUsed, that.lastUsed) &&
                java.util.Objects.equals(createdAt, that.createdAt);
    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, term, frequency, lastUsed, createdAt);
    }
    @Override
    public String toString() {
        return "FrequencyTerm{" +
                "id=" + id +
                ", term='" + term + "'" +
                ", frequency=" + frequency +
                ", lastUsed=" + lastUsed +
                ", createdAt=" + createdAt +
                "}";
    }
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