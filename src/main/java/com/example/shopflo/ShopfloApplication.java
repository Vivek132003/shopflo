package com.example.shopflo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ShopfloApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopfloApplication.class, args);
	}

}
