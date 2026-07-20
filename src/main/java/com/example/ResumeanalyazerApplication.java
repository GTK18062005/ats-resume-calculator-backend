package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResumeanalyazerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeanalyazerApplication.class, args);
		System.out.println("🚀 ATS Resume Checker Started Successfully!");
		System.out.println("📝 API Documentation: http://localhost:8080/api/swagger-ui.html");
		System.out.println("💡 Ready to analyze resumes!");
	}
}