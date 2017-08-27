package com.example.demo;

import com.example.demo.jobs.HelloJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jerry on 2017/8/28.
 */
@Slf4j
@RestController
@RequestMapping("/fire")
public class FireJobResource {

    /**
     * Fire Hello Job Resource
     * http://www.quartz-scheduler.org/documentation/quartz-2.1.x/examples/Example1.html
     * http://tw.gitbook.net/quartz/quartz-helloworld.html
     */
    @RequestMapping(
        value = "/hello-job",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public void fireHelloJob() throws SchedulerException {
        // defined job using JobDetail
        JobDetail helloJob = JobBuilder.newJob(HelloJob.class)
            .withIdentity("helloJob", "group1")
            .build();

        // initial trigger
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("helloJobTrigger", "gorup1")
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever()
            ).build();

        // initial schedule, connection job and trigger
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(helloJob, trigger);

        // fire job
        scheduler.start();
        log.info("Job({}) is fired.", helloJob.getKey().getName());
    }
}
