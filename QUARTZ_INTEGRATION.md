# Quartz Scheduler Integration

This document describes the Quartz scheduler integration added to the Pet Training Service.

## Overview

The Quartz scheduler has been integrated to provide automated training reminders:
- Training reminders (24-hour and 2-hour notifications)

## Components Added

### 1. Dependencies
- `spring-boot-starter-quartz` - Added to build.gradle

### 2. Configuration
- `QuartzConfig.java` - Configures job details and triggers
- `application.yml` - Quartz database configuration

### 3. Scheduled Jobs

#### TrainingReminderJob
- **Frequency**: Every 5 minutes
- **Purpose**: Sends training reminders
- **Features**:
  - 24-hour advance reminders
  - 2-hour final reminders

### 4. Services

#### NotificationService
- Handles all notification logic
- Supports multiple notification types:
  - Training reminders
  - Status change notifications
  - Weather alerts
  - Cancellation notices

### 5. Management & Monitoring
- **Spring Boot Actuator**: Provides Quartz management endpoints
- **Application Logs**: Detailed job execution logging
- **Database Tracking**: Job status and history in PostgreSQL

## Configuration

### Database Storage
Quartz is configured to use PostgreSQL for job persistence with string-based job data storage:
```yaml
spring:
  quartz:
    job-store-type: jdbc
    jdbc:
      initialize-schema: always
    wait-for-jobs-to-complete-on-shutdown: true
    properties:
      org:
        quartz:
          jobStore:
            driverDelegateClass: org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
            tablePrefix: QRTZ_
            useProperties: true

# Actuator Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,info,quartz
  endpoint:
    quartz:
      enabled: true
      show-details: always
```

### Job Scheduling
- **Training Reminders**: Every 5 minutes

## Usage Examples

### Monitoring Job Execution
Jobs run automatically every 5 minutes. You can monitor execution through:

1. **Actuator Endpoints**:
   ```bash
   # Get Quartz scheduler information
   curl http://localhost:8080/actuator/quartz
   
   # Get application health (includes Quartz status)
   curl http://localhost:8080/actuator/health
   
   # Get application info
   curl http://localhost:8080/actuator/info
   ```

2. **Application Logs**: Look for "Training reminder job execution" messages

3. **Database Queries**: Check `QRTZ_JOB_DETAILS` and `QRTZ_TRIGGERS` tables

### Test Configuration
Tests use in-memory Quartz configuration via `src/test/resources/application.yml`:
```yaml
spring:
  quartz:
    job-store-type: memory
    properties:
      org:
        quartz:
          jobStore:
            class: org.quartz.simpl.RAMJobStore
          threadPool:
            threadCount: 1
          scheduler:
            instanceName: TestScheduler
            instanceId: AUTO
```

## Database Schema

Quartz will automatically create the following tables:
- `QRTZ_JOB_DETAILS`
- `QRTZ_TRIGGERS`
- `QRTZ_CRON_TRIGGERS`
- `QRTZ_SIMPLE_TRIGGERS`
- `QRTZ_SCHEDULER_STATE`
- And other Quartz system tables

## Monitoring

### Logs
All job executions are logged with appropriate log levels:
- `INFO` - Successful job executions
- `ERROR` - Failed job executions
- `DEBUG` - Detailed execution information

### Metrics
Job execution metrics can be monitored through:
- **Actuator Endpoints**: Real-time scheduler status and job information
- **Application logs**: Detailed execution logs with timestamps
- **Database tables**: Job history and scheduler state in `QRTZ_*` tables
- **Health checks**: Scheduler health status via `/actuator/health`

## Future Enhancements

1. **Email Integration**: Replace log-based notifications with actual email/SMS
2. **Weather API**: Add weather-based training adjustments
3. **Advanced Scheduling**: Support for cron expressions
4. **Job Clustering**: Enable multi-instance job execution
5. **Metrics Dashboard**: Web-based job monitoring interface

## Troubleshooting

### Common Issues

1. **Jobs not executing**: Check application logs and database connection
2. **Database connection issues**: Verify PostgreSQL configuration and `useProperties=true` setting
3. **BLOB serialization errors**: Ensure `spring.quartz.properties.org.quartz.jobStore.useProperties=true` is set
4. **Test failures**: Verify test configuration uses in-memory job store

### Debug Mode
Enable debug logging for Quartz:
```yaml
logging:
  level:
    org.quartz: DEBUG
    org.roman.petresearch.job: DEBUG
```

