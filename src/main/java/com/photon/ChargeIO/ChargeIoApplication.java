package com.photon.ChargeIO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class ChargeIoApplication {

	public static void main(String[] args) {
        System.out.println("[MAIN] System initialization");
        SpringApplication.run(ChargeIoApplication.class, args);
	}

}
