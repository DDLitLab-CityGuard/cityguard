package de.uni_hamburg.isa.cityguard.cityguardserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *  Entry point of the application.
 *  It is a Spring Boot application, so it will start the autoconfiguration.
 *  Spring Boot will scan the classpath for components, configuration, and services
 *  and automatically configure them.
 */
@SpringBootApplication
public class CityGuardServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityGuardServerApplication.class, args);
	}

}
