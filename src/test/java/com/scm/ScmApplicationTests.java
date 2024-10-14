package com.scm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.services.EmailService;

@SpringBootTest
class ScmApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService mailService;

	@Test
	void sendEmailTest(){
		mailService.sendEmail(
			"atharvagamingacc@gmail.com",
			"Testing email service", 
			"This is from scm project email testing");
			
	}

}
