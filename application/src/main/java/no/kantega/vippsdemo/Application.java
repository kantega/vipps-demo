package no.kantega.vippsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import no.kantega.vipps.HttpClient;

@SpringBootApplication
@ComponentScan("no.kantega")
public class Application {

	@Bean
	public HttpClient getHttpClient() {
		return new HttpClient();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
