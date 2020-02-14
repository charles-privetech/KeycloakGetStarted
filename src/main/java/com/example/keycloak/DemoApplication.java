package com.example.keycloak;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.example.keycloak.entity.Eam;
import com.example.keycloak.service.KeyCloakService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private KeyCloakService keyCloakService;
	 
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);	 		
	}

	@Override
	public void run(String... args) throws Exception {
		//Create an entity
		Eam eam1 = new Eam();
		eam1.setId(LocalDateTime.now().toString());

		//Register the newly created entity as a Resource on Keycloak and specific what scopes will link to it
		keyCloakService.createResource(eam1, Arrays.asList("view", "edit"));	
		 
		//Evaluate granted permissions for user: charles
		keyCloakService.evaluatePermissions("charles", "password");

		//Evaluate granted permissions for user: david
		keyCloakService.evaluatePermissions("david", "password");
	}

}
