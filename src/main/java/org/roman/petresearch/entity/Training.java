package org.roman.petresearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;

@AllArgsConstructor
@Data
@Table("trainings")
public class Training implements Serializable {
    @Id
    private Long id;
    private String name;
    private String description;
    private Instant startedAt;
    private TrainingStatus status;
    private TrainingType type;
    
    // Default constructor
    public Training() {}
}