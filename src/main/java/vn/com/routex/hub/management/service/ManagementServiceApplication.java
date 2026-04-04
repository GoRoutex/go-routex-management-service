package vn.com.routex.hub.management.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.com.go.routex.identity.security.annotation.EnableIdentitySecurity;

@SpringBootApplication
@EnableIdentitySecurity
@EnableScheduling
public class ManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementServiceApplication.class, args);
	}

}
