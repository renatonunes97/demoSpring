package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void testSenha(){
		String hash = "$2a$10$x1Zi/xJuAohXZluMMc/2LejtPx8/n/aMKQoYC0/NvPjdPN8xwAs8a";
		String senha ="123";

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		boolean result= encoder.matches(senha,hash);

		System.out.println(result);

	}

}
