package com.CloudBike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author unique
 */
@EnableScheduling
@SpringBootApplication
public class CloudBikeApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(CloudBikeApplication.class,args);
    }
}
