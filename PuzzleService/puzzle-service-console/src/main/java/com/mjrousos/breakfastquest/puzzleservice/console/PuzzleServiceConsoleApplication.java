package com.mjrousos.breakfastquest.puzzleservice.console;

// NO SPRING

import com.mjrousos.breakfastquest.puzzleservice.PuzzleService;

public class PuzzleServiceConsoleApplication {
	public static void main(String[] args) {
		
		// Create instance of PuzzleService
		PuzzleService puzzleService = new PuzzleService();
		
		System.out.println("Calling PuzzleService's echo API");
		final String message = puzzleService.Echo("Hello!");
		System.out.println("Response from puzzle service echo API: " + message);	
		
		System.out.println();
		System.out.println("- Done -");
	}
}

/*

// SPRING

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.mjrousos.breakfastquest.puzzleservice.PuzzleService;

@Component
@Configuration
@ComponentScan
public class PuzzleServiceConsoleApplication {
	
	@Autowired
	private PuzzleService puzzleService;
	
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(PuzzleServiceConsoleApplication.class);
		PuzzleServiceConsoleApplication app = ctx.getBean(PuzzleServiceConsoleApplication.class);
		app.run(args);
	}

	private void run(String[] args) {
        final String message = puzzleService.Echo("Hello!");
		System.out.println("Response from puzzle service echo API: " + message);		
	}
	
	@Bean
	public PuzzleService puzzleService() {
		return new PuzzleService();
	}
}
*/


/* 

// SPRING BOOT

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PuzzleServiceConsoleApplication implements CommandLineRunner {

	@Autowired
	private PuzzleService puzzleService;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(PuzzleServiceConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
    }

	@Override
	public void run(String... args) {
		System.out.println("Hello, world!");
	}
	
	@Bean
	public PuzzleService puzzleService() {
		return new PuzzleService();
	}
}
*/
