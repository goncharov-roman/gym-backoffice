package org.roman.petresearch.job;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.roman.petresearch.entity.Training;
import org.roman.petresearch.entity.TrainingStatus;
import org.roman.petresearch.service.NotificationService;
import org.roman.petresearch.service.TrainingService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TrainingReminderJob implements Job {

    TrainingService trainingService;
    NotificationService notificationService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Starting training reminder job execution");
        
        try {
            // Get trainings starting in the next 24 hours
            Instant now = Instant.now();
            Instant reminderTime = now.plus(24, ChronoUnit.HOURS);
            
            List<Training> upcomingTrainings = trainingService.getTrainingsStartingBetween(
                now.plus(23, ChronoUnit.HOURS), 
                reminderTime
            );
            
            log.info("Found {} trainings starting in the next 24 hours", upcomingTrainings.size());
            
            for (Training training : upcomingTrainings) {
                if (training.getStatus() == TrainingStatus.SCHEDULED) {
                    try {
                        notificationService.sendTrainingReminder(training);
                        log.info("Sent 24-hour reminder for training: {}", training.getName());
                    } catch (Exception e) {
                        log.error("Failed to send reminder for training: {}", training.getName(), e);
                    }
                }
            }
            
            // Get trainings starting in the next 2 hours for final reminders
            Instant finalReminderTime = now.plus(2, ChronoUnit.HOURS);
            List<Training> finalReminderTrainings = trainingService.getTrainingsStartingBetween(
                now.plus(1, ChronoUnit.HOURS), 
                finalReminderTime
            );
            
            log.info("Found {} trainings starting in the next 2 hours", finalReminderTrainings.size());
            
            for (Training training : finalReminderTrainings) {
                if (training.getStatus() == TrainingStatus.SCHEDULED) {
                    try {
                        notificationService.sendFinalTrainingReminder(training);
                        log.info("Sent final reminder for training: {}", training.getName());
                    } catch (Exception e) {
                        log.error("Failed to send final reminder for training: {}", training.getName(), e);
                    }
                }
            }
            
        } catch (Exception e) {
            log.error("Error executing training reminder job", e);
            throw new JobExecutionException("Training reminder job failed", e);
        }
        
        log.info("Training reminder job execution completed");
    }
}
