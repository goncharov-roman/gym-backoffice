package org.roman.petresearch.controller;

import org.roman.petresearch.dto.TrainingDto;
import org.roman.petresearch.entity.Training;
import org.roman.petresearch.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@CrossOrigin(origins = "*")
public class TrainingController {
    
    private final TrainingService trainingService;
    
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }
    
    @GetMapping
    public ResponseEntity<List<Training>> getAllTrainings() {
        List<Training> trainings = trainingService.getAllTrainings();
        return ResponseEntity.ok(trainings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Training> getTrainingById(@PathVariable Long id) {
        return trainingService.getTrainingById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Training> createTraining(@Valid @RequestBody TrainingDto trainingDto) {
        try {
            Training createdTraining = trainingService.createTraining(trainingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Training> updateTraining(@PathVariable Long id, @Valid @RequestBody TrainingDto trainingDto) {
        return trainingService.updateTraining(id, trainingDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
        trainingService.deleteTraining(id);
        return ResponseEntity.noContent().build();
    }
} 