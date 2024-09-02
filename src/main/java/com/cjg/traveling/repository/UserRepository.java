package com.cjg.traveling.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cjg.traveling.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	// 사용자 등록
	public User save(User user);

	// 사용자 등록시 아이디 중복 체크
	public boolean existsByUserId(String userId);
	
	public User findByUserId(String userId);
	
	public void deleteByUserId(String userId);

}
