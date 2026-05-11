package com.loomi.desafiotech;

import org.springframework.boot.SpringApplication;

public class TestDesafiotechApplication {

	public static void main(String[] args) {
		SpringApplication.from(DesafiotechApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
