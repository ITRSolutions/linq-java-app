package com.linq.website;

import com.linq.website.entity.User;
import com.linq.website.enums.RoleType;
import com.linq.website.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
class LinqApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		List<User> admin = userRepository.findByRole(RoleType.ADMIN);
		for (User user : admin) {
			// Assuming the User class has methods like getFirstName(), getLastName(), and getEmail()
			System.out.println("Admin Name: " + user.getFirstName() + " " + user.getLastName());
			System.out.println("Admin Email: " + user.getEmail());
			// You can add more fields to print if needed
		}
	}

}
