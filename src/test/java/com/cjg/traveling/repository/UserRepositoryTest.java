package com.cjg.traveling.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cjg.traveling.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //자동으로 내장 메모리 DB로 설정되는것을 막는다.
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;	
	
	// 사용자 등록
	@Test
	public void save() {
		
		User user = new User();
		user.setUserId("---");
		
		User createdUser = userRepository.save(user);
		Assertions.assertThat(createdUser.getUserId()).isEqualTo(user.getUserId());
	}
	
	@Test
	public void findByUserId() {
		
		User user = new User();
		user.setUserId("---");
		
		userRepository.save(user);
		
		User findUser = userRepository.findByUserId(user.getUserId());
		Assertions.assertThat(findUser.getUserId()).isEqualTo(user.getUserId());
	}

}
