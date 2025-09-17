package com.winestore.winestore;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Data
@SpringBootApplication
public class WinestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WinestoreApplication.class, args);

		TestLombok test = new TestLombok();
		test.setName("Bipin");
		test.setAge(21);

		System.out.println("Name: " + test.getName());
		System.out.println("Age: " + test.getAge());
	}

}