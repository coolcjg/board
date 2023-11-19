package com.cjg.traveling.repository;

import org.springframework.data.repository.CrudRepository;

import com.cjg.traveling.domain.User;

public interface UserRepository extends CrudRepository<User, String> {
	
	// 사용자 등록
	public User save(User user);

	// 사용자 등록시 아이디 중복 체크
	public boolean existsByUserId(String userId);
	
	/*	
	// 사용자 목록 검색
	public List<User> getUserList(){
		return em.createQuery("SELECT u FROM User u ORDER BY u.id", User.class).getResultList();
	}
	
	public void deleteUser(User user) {
		em.remove(user);
	}
	*/

}
