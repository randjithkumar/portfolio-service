package com.peplatform.portfolioservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * Main application class for the Portfolio Service.
 * This class initializes and starts the Spring Boot application.
 *
 * @author Randjith
 * @version 1.0.0
 * @email "mailto:randjithkumar@users.noreply.github.com"
 * @since 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class PortfolioServiceApplication {


    /**
     * Entry point of the application.
     * <p>Starts the Spring Boot 应用程序 and runs it with the provided arguments.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        SpringApplication.run(PortfolioServiceApplication.class, args);
    }

}