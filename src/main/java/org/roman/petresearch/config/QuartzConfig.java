package org.roman.petresearch.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SimpleTrigger;
import org.roman.petresearch.job.TrainingReminderJob;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Slf4j
@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean trainingReminderJobDetail() {
        JobDetailFactoryBean factory = new JobDetailFactoryBean();
        factory.setJobClass(TrainingReminderJob.class);
        factory.setName("trainingReminderJob");
        factory.setDescription("Job to send training reminders to participants");
        factory.setDurability(true);
        return factory;
    }

    @Bean
    public SimpleTriggerFactoryBean trainingReminderTrigger(JobDetailFactoryBean trainingReminderJobDetail) {
        SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
        factory.setJobDetail(trainingReminderJobDetail.getObject());
        factory.setName("trainingReminderTrigger");
        factory.setDescription("Trigger for training reminder job");
        factory.setRepeatInterval(60000); // Run every minute
        factory.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factory.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factory;
    }


}
