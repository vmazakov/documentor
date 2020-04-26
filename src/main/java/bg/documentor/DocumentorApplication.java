package bg.documentor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot Application
 * - Watches for a file at directory, defined in application.yml
 * - moves it after processing it
 * - the process is crawling for MS Word content controllers (like drop-down list of values) and maps/persists them to the DB tables and fields
 * - provides REST endpoints for: GET, POST and PUT for a dynamic web form
 * - generates ready for print word file filled with user's input
 */
@EnableScheduling @SpringBootApplication public class DocumentorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentorApplication.class, args);
	}

}
