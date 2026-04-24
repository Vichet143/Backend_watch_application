package com.appwatch.backend;

import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendApplication {

	public static void main(String[] args) {
		loadEnvFile();
		SpringApplication.run(BackendApplication.class, args);
	}

	private static void loadEnvFile() {
		try {
			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMalformed()
					.ignoreIfMissing()
					.load();

			setIfMissing("BAKONG_TOKEN", dotenv.get("BAKONG_TOKEN"));
			setIfMissing("BAKONG_ACCOUNT_ID", dotenv.get("BAKONG_ACCOUNT_ID"));
			setIfMissing("BAKONG_ACCOUNT_NAME", dotenv.get("BAKONG_ACCOUNT_NAME"));
			setIfMissing("BAKONG_MERCHANT_ID", dotenv.get("BAKONG_MERCHANT_ID"));
			setIfMissing("JWT_SECRET", dotenv.get("jwt.secret"));
			setIfMissing("SERVER_PORT", dotenv.get("SERVER_PORT"));
		} catch (Exception ex) {
			System.err.println("Failed to load .env: " + ex.getMessage());
		}
	}

	private static void setIfMissing(String key, String value) {
		if (value == null || value.isBlank()) {
			return;
		}
		String fromEnv = System.getenv(key);
		String fromProp = System.getProperty(key);
		if ((fromEnv == null || fromEnv.isBlank()) && (fromProp == null || fromProp.isBlank())) {
			System.setProperty(key, value);
		}
	}

}
