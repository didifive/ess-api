package me.didi.api.ess;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080", description = "Localhost")})
public class EssApplication {

	public static void main(String[] args) {
		SpringApplication.run(EssApplication.class, args);
	}

}
