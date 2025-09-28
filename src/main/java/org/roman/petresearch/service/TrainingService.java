package org.roman.petresearch.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.roman.petresearch.dto.TrainingDto;
import org.roman.petresearch.entity.Training;
import org.roman.petresearch.repository.TrainingRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TrainingService {

    TrainingRepository trainingRepository;

    public List<Training> getAllTrainings() {
        return trainingRepository.findAllOrderByName();
    }

    @Cacheable(value = "trainings", key = "#id", unless = "#result == null")
    public Optional<Training> getTrainingById(Long id) {
        try {
            log.debug("Cache miss for training id: {}", id);
            return trainingRepository.findById(id);
        } catch (Exception e) {
            log.error("Error getting training by id: {}", id, e);
            return Optional.empty();
        }
    }

    public List<Training> getTrainingsStartingBetween(Instant startTime, Instant endTime) {
        return trainingRepository.findByStartedAtBetween(startTime, endTime);
    }

    public Training createTraining(TrainingDto trainingDto) {
        Training training = new Training();
        training.setName(trainingDto.name());
        training.setDescription(trainingDto.description());
        training.setStartedAt(Instant.now());
        training.setType(trainingDto.type());
        training.setStatus(trainingDto.status());

        return trainingRepository.save(training);
    }

    @CacheEvict(value = "trainings", key = "#id")
    public Optional<Training> updateTraining(Long id, TrainingDto trainingDto) {
        return trainingRepository.findById(id)
                .map(existingTraining -> {
                    existingTraining.setName(trainingDto.name());
                    existingTraining.setDescription(trainingDto.description());
                    existingTraining.setType(trainingDto.type());
                    existingTraining.setStatus(trainingDto.status());

                    return trainingRepository.save(existingTraining);
                });
    }

    @CacheEvict(value = "trainings", key = "#id")
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }

    @CacheEvict(value = "trainings", allEntries = true)
    public void clearCache() {
        log.debug("Clearing all training caches");
    }
}