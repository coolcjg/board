package com.cjg.traveling.repository;

import org.springframework.data.repository.CrudRepository;

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.User;

public interface UserRepository extends CrudRepository<Board, Long> {
	
	// 사용자 등록
	public User save(User user);
	
	/*
	// 사용자 조회
	public User getUser(Long id) {
		return em.find(User.class, id);
	}
	
	// 사용자 목록 검색
	public List<User> getUserList(){
		return em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
	}
	
	public void deleteUser(User user) {
		em.remove(user);
	}
	*/

}
