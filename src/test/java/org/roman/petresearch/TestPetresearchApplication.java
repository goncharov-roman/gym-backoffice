package org.roman.petresearch;

import org.springframework.boot.SpringApplication;

public class TestPetresearchApplication {

	public static void main(String[] args) {
		SpringApplication.from(PetresearchApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
