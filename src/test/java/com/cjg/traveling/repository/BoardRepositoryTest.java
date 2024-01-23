package com.cjg.traveling.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //자동으로 내장 메모리 DB로 설정되는것을 막는다.
public class BoardRepositoryTest {
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void save() {
				
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		
		Board createdBoard = boardRepository.save(board);
		
		Assertions.assertThat(board).isEqualTo(createdBoard);
	}
	
	@Test
	void findByBoardId() {
		
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		
		Board createdBoard = boardRepository.save(board);
		
		Board findBoard = boardRepository.findByBoardId(createdBoard.getBoardId());
		
		Assertions.assertThat(createdBoard).isEqualTo(findBoard);
	}	
	
	@Test
	void deleteByBoardId() {
		
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		
		Board createdBoard = boardRepository.save(board);
		boardRepository.deleteByBoardId(createdBoard.getBoardId());
		
		Board findBoard = boardRepository.findByBoardId(createdBoard.getBoardId());
		
		Assertions.assertThat(findBoard).isNull();		
	}
	
}
