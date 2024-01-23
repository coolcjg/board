package com.cjg.traveling.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.Media;
import com.cjg.traveling.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //자동으로 내장 메모리 DB로 설정되는것을 막는다.
public class MediaRepositoryTest {
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MediaRepository mediaRepository;		
	
	@Test
	public void save() {
		
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		Board createdBoard = boardRepository.save(board);
		
		Media media = new Media();
		media.setBoard(createdBoard);
		media.setOriginalFileClientName("test.mp4");
		media.setOriginalFileName("aaa-bbb-ccc.mp4");
		media.setOriginalFilePath("/home/test/");
		media.setEncodingFilePath("/home/test/");
		media.setOriginalFileSize(1L);
		media.setStatus("wait");
		media.setType("video");
		Media createdMedia = mediaRepository.save(media);
		
		Assertions.assertThat(createdMedia).isNotNull();
	}
	
	@Test
	public void findByMediaId() {
		
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		Board createdBoard = boardRepository.save(board);
		
		Media media = new Media();
		media.setBoard(createdBoard);
		media.setOriginalFileClientName("test.mp4");
		media.setOriginalFileName("aaa-bbb-ccc.mp4");
		media.setOriginalFilePath("/home/test/");
		media.setEncodingFilePath("/home/test/");
		media.setOriginalFileSize(1L);
		media.setStatus("wait");
		media.setType("video");
		Media createdMedia = mediaRepository.save(media);
		
		Media findMedia = mediaRepository.findByMediaId(createdMedia.getMediaId());
		
		Assertions.assertThat(createdMedia).isEqualTo(findMedia);
				
	}
	
	@Test
	public void findByBoard_boardId(){
		
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		Board createdBoard = boardRepository.save(board);
		
		Media media = new Media();
		media.setBoard(createdBoard);
		media.setOriginalFileClientName("test.mp4");
		media.setOriginalFileName("aaa-bbb-ccc.mp4");
		media.setOriginalFilePath("/home/test/");
		media.setEncodingFilePath("/home/test/");
		media.setOriginalFileSize(1L);
		media.setStatus("wait");
		media.setType("video");
		Media createdMedia = mediaRepository.save(media);
		
		List<Media> findMedia = mediaRepository.findByBoard_boardId(createdBoard.getBoardId());
		
		Assertions.assertThat(findMedia.size()).isEqualTo(1);
		
	}
	
	@Test
	public void deleteByMediaId(){
		
		User user = new User();
		user.setUserId("---");
		userRepository.save(user);
				
		Board board = new Board();
		board.setUser(user);
		Board createdBoard = boardRepository.save(board);
		
		Media media = new Media();
		media.setBoard(createdBoard);
		media.setOriginalFileClientName("test.mp4");
		media.setOriginalFileName("aaa-bbb-ccc.mp4");
		media.setOriginalFilePath("/home/test/");
		media.setEncodingFilePath("/home/test/");
		media.setOriginalFileSize(1L);
		media.setStatus("wait");
		media.setType("video");
		Media createdMedia = mediaRepository.save(media);
				
		List<Media> mediaList = mediaRepository.deleteByMediaId(createdMedia.getMediaId());
		
		Assertions.assertThat(mediaList.size()).isEqualTo(1);	
	}

}
