package com.project.auto.testing.messagepublisher.build;

import com.project.auto.testing.messagepublisher.build.controllers.PublisherController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Slf4j
@EnableCaching
public class MessagePublisherApp implements CommandLineRunner {

    @Autowired
    private PublisherController publisherController;

    public static void main(String[] args) {
        log.info("Starting Message Publisher App");

        SpringApplication.run(MessagePublisherApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        log.info("Starting Execution of Publisher Controller");
        publisherController.publishMessages();

        System.exit(0);
    }
}
