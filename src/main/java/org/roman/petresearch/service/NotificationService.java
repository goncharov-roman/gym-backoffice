package org.roman.petresearch.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.roman.petresearch.entity.Training;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationService {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public void sendTrainingReminder(Training training) {
        String message = String.format(
                """
                        🔔 Напоминание о тренировке!
                        
                        Тренировка: %s
                        Тип: %s
                        Дата: %s
                        Время: %s
                        
                        Не забудьте подготовиться к тренировке!""",
                training.getName(),
                training.getType().name(),
                training.getStartedAt().atZone(java.time.ZoneId.systemDefault()).format(DATE_FORMATTER),
                training.getStartedAt().atZone(java.time.ZoneId.systemDefault()).format(TIME_FORMATTER)
        );

        // TODO: Integrate with actual notification service (email, SMS, push notifications)
        log.info("Sending 24-hour training reminder: {}", message);

        // For now, just log the notification
        // In a real implementation, you would:
        // 1. Send email via SMTP
        // 2. Send SMS via Twilio/AWS SNS
        // 3. Send push notification via Firebase
        // 4. Store notification in database for audit
    }

    public void sendFinalTrainingReminder(Training training) {
        String message = String.format(
                """
                        ⏰ Финальное напоминание!
                        
                        Тренировка '%s' начинается через 2 часа!
                        Тип: %s
                        Время: %s
                        
                        Увидимся на тренировке!""",
                training.getName(),
                training.getType().name(),
                training.getStartedAt().atZone(java.time.ZoneId.systemDefault()).format(TIME_FORMATTER)
        );

        log.info("Sending final training reminder: {}", message);
    }
}



