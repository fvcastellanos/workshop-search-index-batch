package net.cavitos.workshop.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorkshopSearchIndexApplication {

	public static void main(String[] args) {

		final var applicationContext = SpringApplication.run(WorkshopSearchIndexApplication.class, args);
		final var status = SpringApplication.exit(applicationContext);

		System.exit(status);
	}
}
