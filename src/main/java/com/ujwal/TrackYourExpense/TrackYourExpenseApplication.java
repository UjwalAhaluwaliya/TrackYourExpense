package com.ujwal.TrackYourExpense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy

public class TrackYourExpenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackYourExpenseApplication.class, args);
	}

}
