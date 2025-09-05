package org.roman.petresearch.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.roman.petresearch.dto.TrainingDto;
import org.roman.petresearch.mapper.TrainingMapper;
import org.roman.petresearch.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@CrossOrigin(origins = "*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TrainingController {

    TrainingMapper trainingMapper;
    TrainingService trainingService;

    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingMapper.toDtoList(trainingService.getAllTrainings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingDto> getTrainingById(@PathVariable Long id) {
        return trainingService.getTrainingById(id)
                .map(trainingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TrainingDto> createTraining(@Valid @RequestBody TrainingDto trainingDto) {
        try {
            TrainingDto createdTraining = trainingMapper.toDto(trainingService.createTraining(trainingDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTraining);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingDto> updateTraining(@PathVariable Long id,
                                                      @Valid @RequestBody TrainingDto trainingDto) {
        return trainingService.updateTraining(id, trainingDto)
                .map(trainingMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void deleteTraining(@PathVariable Long id) {
        trainingService.deleteTraining(id);
    }
} 