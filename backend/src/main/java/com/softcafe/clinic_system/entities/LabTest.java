package com.softcafe.clinic_system.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "laboratory_tests")
@Schema(name = "Laboratory Test", description = "An object representation of test")
public class LabTest {
    @Id
    @GeneratedValue
    @Schema(description = "Primary key", example = "1")
    private Long id;

    @OneToOne
    @JoinColumn(name = "record_id")
    @Schema(description = "Record details", implementation = Record.class)
    private Record record;

    @ElementCollection
    @CollectionTable(name = "test_investigations", joinColumns = @JoinColumn(name = "test_id"))
    @Schema(description = "List of tests to be done", example = "[\"blood test\", \"urinalysis\"]")
    @Column(name = "investigation")
    private List<String> investigations;

    @ElementCollection
    @CollectionTable(name = "test_findings", joinColumns = @JoinColumn(name = "test_id"))
    @Column(name = "finding")
    @Schema(description = "List of findings", example = "[\"tapeworm present\", \"low vitam B2\"]")
    private List<String> findings;

    @Column(name = "created_at")
    @Schema(description = "Creation date", example = "2025-05-24T10:41:56.976249081c")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Date updated", example = "2025-05-24T10:41:56.976249081")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
