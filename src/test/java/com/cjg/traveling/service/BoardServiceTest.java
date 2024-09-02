package com.cjg.traveling.service;

import com.cjg.traveling.common.DateFormat;
import com.cjg.traveling.common.HttpRequestUtil;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.kafka.KafkaProducer;
import com.cjg.traveling.domain.*;
import com.cjg.traveling.dto.AlarmDto;
import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardSearchDto;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.redis.RedisPublisher;
import com.cjg.traveling.repository.*;
import com.cjg.traveling.status.AlarmType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
	
	Logger logger = LoggerFactory.getLogger(BoardServiceTest.class);
	
	@Mock
	private BoardRepository boardRepository;
	
	@Mock
	private MediaRepository mediaRepository;

	@Mock
	private AlarmService alaramService;

	@Mock
	private OpinionService opinionService;
	
	@Mock
	private OpinionRepository opinionRepository;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private AlarmService alarmService;
	@Mock
	private AlarmRepository alarmRepository;
	
	@Mock
	private MediaService mediaService;
	
	@Mock
	private HttpRequestUtil httpRequestUtil;
	
	@InjectMocks
	private Jwt jwt;

	@Mock
	KafkaProducer kafkaProducer;

	@InjectMocks
	private BoardService boardService;

	@Value("${uploadPath}")
	private static String uploadPath;

	@Value("${serverUrl}")
	private static String serverUrl;
	
	@Value("${encodeReturnUrl}")
	private static String encodeReturnUrl;

	@Mock
	private RedisPublisher redisPublisher;
	
	@BeforeAll
	public static void setUp() {
	    ReflectionTestUtils.setField(BoardServiceTest.class, "uploadPath", "D:/NAS/uploadTest/");
	    ReflectionTestUtils.setField(BoardServiceTest.class, "serverUrl", "http://localhost:8080");
	    ReflectionTestUtils.setField(BoardServiceTest.class, "encodeReturnUrl", "http://localhost:8080/api/encodingResult");
	}	
	
	@Test
	@DisplayName("리스트 검색 : 검색조건 있을 때")
	public void list(){

		Map<String,String> map = new HashMap();
		map.put("pageNumber", "1");
		map.put("searchType", "all");
		map.put("searchText", "test");

		BoardSearchDto boardSearchDto = new BoardSearchDto();
		boardSearchDto.setPageNumber(1);
		boardSearchDto.setSearchType("all");
		boardSearchDto.setSearchText("test");

		List<Board> boardList = new ArrayList();
		User user = new User();
		user.setUserId("testId");

		Board boardMock1 = new Board();
		boardMock1.setBoardId(1l);
		boardMock1.setUser(user);

		Board boardMock2 = new Board();
		boardMock2.setBoardId(2l);
		boardMock2.setUser(user);

		Board boardMock3 = new Board();
		boardMock3.setBoardId(3l);
		boardMock3.setUser(user);

		Board boardMock4 = new Board();
		boardMock4.setBoardId(4l);
		boardMock4.setUser(user);

		Board boardMock5 = new Board();
		boardMock5.setBoardId(5l);
		boardMock5.setUser(user);

		boardList.add(boardMock1);
		boardList.add(boardMock2);
		boardList.add(boardMock3);
		boardList.add(boardMock4);
		boardList.add(boardMock5);

		Pageable pageableMock = PageRequest.of(0, 10);
		Page<Board> page = new PageImpl<>(boardList, pageableMock, 10L);

		given(boardRepository.selectBoardPage(any(Pageable.class))).willReturn(page);

		Map<String,Object> result  = boardService.list(boardSearchDto);

		Assertions.assertThat((String)result.get("message")).isEqualTo("success");

	}

	@Test
	@DisplayName("리스트 검색 : 검색조건 없을 때")
	public void list_2(){

		Map<String,String> map = new HashMap();
		map.put("pageNumber", "1");
		map.put("searchType", "");
		map.put("searchText", "");

		BoardSearchDto boardSearchDto = new BoardSearchDto();

		List<Board> boardList = new ArrayList();
		User user = new User();
		user.setUserId("testId");

		Board boardMock1 = new Board();
		boardMock1.setBoardId(1l);
		boardMock1.setUser(user);

		Board boardMock2 = new Board();
		boardMock2.setBoardId(2l);
		boardMock2.setUser(user);

		Board boardMock3 = new Board();
		boardMock3.setBoardId(3l);
		boardMock3.setUser(user);

		Board boardMock4 = new Board();
		boardMock4.setBoardId(4l);
		boardMock4.setUser(user);

		Board boardMock5 = new Board();
		boardMock5.setBoardId(5l);
		boardMock5.setUser(user);

		boardList.add(boardMock1);
		boardList.add(boardMock2);
		boardList.add(boardMock3);
		boardList.add(boardMock4);
		boardList.add(boardMock5);

		Pageable pageableMock = PageRequest.of(0, 10);
		Page<Board> page = new PageImpl<>(boardList, pageableMock, 10L);

		given(boardRepository.findAll(any(PageRequest.class))).willReturn(page);

		Map<String,Object> result  = boardService.list(boardSearchDto);

		Assertions.assertThat((String)result.get("message")).isEqualTo("success");

	}
	
	@Test
	@DisplayName("게시글 찾기")
	public void findByBoardId() {
		
		Long boardId = 1L;
		Board board = new Board();
		board.setBoardId(boardId);
		
		User userMock = new User();
		userMock.setUserId("coolcjg");
		userMock.setPassword("cda4a7cf32fed2e86a3ad118720560b94c40bf5b60a95059419c81734140a6ac");
		userMock.setSalt("7b963ebdefad18a43bcd0bd22d4a888b364a9044");
		userMock.setName("최종규");
		LocalDate birthDay = LocalDate.of(1990, 1, 24);		
		userMock.setBirthDay(birthDay);
		userMock.setRefreshToken("eyJhbGciOiJIUzM4NCJ9.eyJuYW1lIjoi7LWc7KKF6recIiwiaWQiOiJjb29sY2pnIiwiaWF0IjoxNzA2MDg0NDk3LCJleHAiOjE3MDg3NjI4OTd9.SsGyUqiZBAzCKvScLE30zx-6B2Znyb1iwdmJ8g2X_rsqChAnir1xcbO7j6V5YYZu");

		board.setUser(userMock);

		board.setTitle("22");
		board.setContents("323231");
		board.setRegion("인천");
		board.setRegDate(LocalDateTime.of(2024, 1, 21, 19, 40, 18));
		board.setView(10);
		
		Media media1 = new Media();
		media1.setMediaId(84L);
		media1.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media1.setType("document");
		media1.setStatus("success");
		media1.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media1.setOriginalFileName("9db1494c-fb39-4663-98b2-816d40782500.txt");
		media1.setOriginalFileClientName("1.txt");
		media1.setOriginalFileSize(3l);
		media1.setPercent(100);
		
		Media media2 = new Media();
		media2.setMediaId(85L);
		media2.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media2.setType("audio");
		media2.setStatus("success");
		media2.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media2.setOriginalFileName("41383286-3bcf-43eb-b22c-575298cd6c89.mp3");
		media2.setOriginalFileClientName("2.mp3");
		media2.setOriginalFileSize(5319693l);
		media2.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media2.setEncodingFileName("85.mp3");
		media2.setEncodingFileSize(3191886l);
		media2.setPercent(100);
		
		Media media3 = new Media();
		media3.setMediaId(86L);
		media3.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media3.setType("image");
		media3.setStatus("success");
		media3.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media3.setOriginalFileName("f5247aea-345b-4e5a-bf8d-1b562e1fd623.jpg");
		media3.setOriginalFileClientName("3.jpg");
		media3.setOriginalFileSize(278934l);
		media3.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media3.setEncodingFileName("86.jpg");
		media3.setEncodingFileSize(279650l);
		media3.setThumbnailPath("D:/NAS/upload/encoding/2024/01/24/86.jpg");
		media3.setPercent(100);
		
		Media media4 = new Media();
		media4.setMediaId(87L);
		media4.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media4.setType("video");
		media4.setStatus("success");
		media4.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media4.setOriginalFileName("774bf5ff-cf9a-4c26-b608-2443c9ce211f.mp4");
		media4.setOriginalFileClientName("4.mp4");
		media4.setOriginalFileSize(10498677l);
		media4.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media4.setEncodingFileName("87.mp4");
		media4.setEncodingFileSize(9328117l);
		media4.setThumbnailPath("D:/NAS/upload/encoding/2024/01/24/87.jpg");
		media4.setPercent(100);		
		
		List<Media> mediaListMock = new ArrayList();
		mediaListMock.add(media1);
		mediaListMock.add(media2);
		mediaListMock.add(media3);
		mediaListMock.add(media4);

		board.setMediaList(mediaListMock);

		given(boardRepository.findByBoardId(boardId)).willReturn(board);
			
		Map<String,Object> result = boardService.findByBoardId(boardId);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}

	@Test
	@DisplayName("게시글 찾기 : 없는 게시글일 경우")
	public void findByBoardId_2() {

		Long boardId = 1L;
		Board board = null;

		given(boardRepository.findByBoardId(boardId)).willReturn(board);

		Map<String,Object> result = boardService.findByBoardId(boardId);

		Assertions.assertThat(result.get("message")).isEqualTo("Board is not exists");
	}
	
	@Test
	@DisplayName("게시글 저장")
	public void save() throws Exception{
		
		//given		
		UserDto userDtoMock = new UserDto();
		userDtoMock.setUserId("coolcjg");
		userDtoMock.setName("최종규");
		
		//첨부 미디어가 있을 때
		BoardDto boardDto = new BoardDto();
		boardDto.setUserDTO(userDtoMock);
		boardDto.setTitle("1");
		
		List<MultipartFile> multiList = new ArrayList();
		
		//동영상
		MockMultipartFile multiMock1 = new MockMultipartFile("video", "video.mp4", "video/mp4", "thumbnail".getBytes());
		
		//오디오
		MockMultipartFile multiMock2 = new MockMultipartFile("audio", "audio.ogg", "audio/ogg", "thumbnail".getBytes());
		
		//이미지
		MockMultipartFile multiMock3 = new MockMultipartFile("video", "image.jpg", "image/jpg", "thumbnail".getBytes());
		
		//문서
		MockMultipartFile multiMock4 = new MockMultipartFile("video", "document.pdf", "application/pdf", "thumbnail".getBytes());
		
		multiList.add(multiMock1);
		multiList.add(multiMock2);
		multiList.add(multiMock3);
		multiList.add(multiMock4);

		boardDto.setFiles(multiList);

		User user = new User();
		user.setUserId(boardDto.getUserId());

		Board board = new Board();
		board.setUser(user);
		board.setTitle(boardDto.getTitle());
		board.setRegion(boardDto.getRegion());
		board.setContents(boardDto.getContents());

		Media media = new Media();
		media.setType("video");
		media.setMediaId(1L);
		media.setOriginalFilePath("D:/NAS/upload/original/2024/05/19/");
		media.setOriginalFileName("02238daa-6d43-4dc7-95a2-8f0c1d63ef95.jpg");

		given(boardRepository.save(board)).willReturn(board);
		given(mediaRepository.save(any(Media.class))).willReturn(media);

		boardService.uploadPath = uploadPath;
		Map<String,Object> result = boardService.save(boardDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		
	}
	
	@Test
	@DisplayName("게시글 수정")
	public void updateBoard() throws Exception{

		//given
		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(2l);

		List<MultipartFile> multiList = new ArrayList();

		//동영상
		MockMultipartFile multiMock1 = new MockMultipartFile("video", "video.mp4", "video/mp4", "thumbnail".getBytes());

		//오디오
		MockMultipartFile multiMock2 = new MockMultipartFile("audio", "audio.ogg", "audio/ogg", "thumbnail".getBytes());

		//이미지
		MockMultipartFile multiMock3 = new MockMultipartFile("video", "image.jpg", "image/jpg", "thumbnail".getBytes());

		//문서
		MockMultipartFile multiMock4 = new MockMultipartFile("video", "document.pdf", "application/pdf", "thumbnail".getBytes());

		multiList.add(multiMock1);
		multiList.add(multiMock2);
		multiList.add(multiMock3);
		multiList.add(multiMock4);

		boardDto.setFiles(multiList);

		Board board = new Board();
		board.setBoardId(boardDto.getBoardId());
		board.setTitle("testTitle");
		board.setRegion("경기");
		board.setContents("test");

		Media media = new Media();
		media.setType("video");
		media.setMediaId(1L);
		media.setOriginalFilePath("D:/NAS/upload/original/2024/05/19/");
		media.setOriginalFileName("02238daa-6d43-4dc7-95a2-8f0c1d63ef95.jpg");

		given(boardRepository.findByBoardId(boardDto.getBoardId())).willReturn(board);
		given(mediaRepository.save(any(Media.class))).willReturn(media);

		//When
		boardService.uploadPath=uploadPath;
		Map<String, Object> result = boardService.updateBoard(boardDto);

		//Then
		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}
	
	
	@Test
	@DisplayName("게시글 삭제")
	public void deleteBoard() throws Exception{

		BoardDto boardDto = new BoardDto();
		boardDto.setBoardIdArray("1,2");
		
		Media media1 = new Media();
		media1.setMediaId(84L);
		media1.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media1.setType("document");
		media1.setStatus("success");
		media1.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media1.setOriginalFileName("9db1494c-fb39-4663-98b2-816d40782500.txt");
		media1.setOriginalFileClientName("1.txt");
		media1.setOriginalFileSize(3l);
		media1.setPercent(100);
		
		Media media2 = new Media();
		media2.setMediaId(85L);
		media2.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media2.setType("audio");
		media2.setStatus("success");
		media2.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media2.setOriginalFileName("41383286-3bcf-43eb-b22c-575298cd6c89.mp3");
		media2.setOriginalFileClientName("2.mp3");
		media2.setOriginalFileSize(5319693l);
		media2.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media2.setEncodingFileName("85.mp3");
		media2.setEncodingFileSize(3191886l);
		media2.setPercent(100);
		
		Media media3 = new Media();
		media3.setMediaId(86L);
		media3.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media3.setType("image");
		media3.setStatus("success");
		media3.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media3.setOriginalFileName("f5247aea-345b-4e5a-bf8d-1b562e1fd623.jpg");
		media3.setOriginalFileClientName("3.jpg");
		media3.setOriginalFileSize(278934l);
		media3.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media3.setEncodingFileName("86.jpg");
		media3.setEncodingFileSize(279650l);
		media3.setThumbnailPath("D:/NAS/upload/encoding/2024/01/24/86.jpg");
		media3.setPercent(100);
		
		Media media4 = new Media();
		media4.setMediaId(87L);
		media4.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media4.setType("video");
		media4.setStatus("success");
		media4.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media4.setOriginalFileName("774bf5ff-cf9a-4c26-b608-2443c9ce211f.mp4");
		media4.setOriginalFileClientName("4.mp4");
		media4.setOriginalFileSize(10498677l);
		media4.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media4.setEncodingFileName("87.mp4");
		media4.setEncodingFileSize(9328117l);
		media4.setThumbnailPath("D:/NAS/upload/encoding/2024/01/24/87.jpg");
		media4.setPercent(100);		
		
		List<Media> mediaList = new ArrayList();
		mediaList.add(media1);
		mediaList.add(media2);
		mediaList.add(media3);
		mediaList.add(media4);

		given(mediaRepository.deleteByBoardId(any(Long.class))).willReturn((long)mediaList.size());

		//When
		Map<String, Object> result = boardService.deleteBoard(boardDto);

		//Then
		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}
	

	@Test
	@DisplayName("의견 등록 : 의견이 있을 때")
	public void postOpinion() throws Exception{

		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(1L);
		boardDto.setUserId("coolcjg");
		boardDto.setValue("Y");

		User user_click = new User();
		user_click.setUserId("coolcjg");

		User user_board = new User();
		user_board.setUserId("coolcjg1");

		Board board = new Board();
		board.setBoardId(boardDto.getBoardId());
		board.setUser(user_board);

		given(opinionRepository.findByBoardIdAndUserId(boardDto.getBoardId(), boardDto.getUserId())).willReturn(null);
		given(userRepository.findByUserId(boardDto.getUserId())).willReturn(user_click);
		given(boardRepository.findByBoardId(boardDto.getBoardId())).willReturn(board);

		Opinion opinion = new Opinion();
		opinion.setUser(user_click);
		opinion.setBoard(board);
		opinion.setValue(boardDto.getValue());

		given(opinionRepository.save(any(Opinion.class))).willReturn(opinion);

		Alarm alarm = new Alarm();
		alarm.setType(AlarmType.LIKE.getText());
		alarm.setValue(opinion.getValue());
		alarm.setBoard(board);
		alarm.setFromUser(user_click);
		alarm.setToUser(board.getUser());
		alarm.setRegDate(LocalDateTime.now());

		alarm.setToUser(user_board);

		given(alarmRepository.save(any(Alarm.class))).willReturn(alarm);

		AlarmDto alarmDto = new AlarmDto();
		alarmDto.setAlarmId(alarm.getAlarmId());
		alarmDto.setBoardId(alarm.getBoard().getBoardId());
		alarmDto.setFromUserId(alarm.getFromUser().getUserId());
		alarmDto.setToUserId(alarm.getToUser().getUserId());
		alarmDto.setRegDate(DateFormat.convertDateFormat(alarm.getRegDate()));
		alarmDto.setType(alarm.getType());
		alarmDto.setValue(alarm.getValue());
		alarmDto.setChecked(alarm.getChecked());

		given(alarmService.setAlarmDto(any(Alarm.class))).willReturn(alarmDto);

		Map<String, Object> result = boardService.postOpinion(boardDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");

	}

	@Test
	@DisplayName("의견 등록 : 의견이 없을 때")
	public void postOpinion_2() throws Exception{

		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(1L);
		boardDto.setUserId("coolcjg");
		boardDto.setValue("Y");

		User user_click = new User();
		user_click.setUserId("coolcjg");

		User user_board = new User();
		user_board.setUserId("coolcjg1");

		Board board = new Board();
		board.setBoardId(boardDto.getBoardId());
		board.setUser(user_board);

		Opinion opinion = new Opinion();
		opinion.setUser(user_click);
		opinion.setBoard(board);
		opinion.setValue(boardDto.getValue());

		given(opinionRepository.findByBoardIdAndUserId(boardDto.getBoardId(), boardDto.getUserId())).willReturn(opinion);

		Map<String, Object> result = boardService.postOpinion(boardDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}



	@Test
	@DisplayName("게시글 삭제 성공")
	public void deleteOpinion() throws Exception{
		
		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(1l);
		boardDto.setUserId("testId");
		
		given(opinionRepository.deleteByBoardIdAndUserId(boardDto.getBoardId(), boardDto.getUserId())).willReturn(1L);
		Map<String, Object> result = boardService.deleteOpinion(boardDto);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		
	}

	@Test
	@DisplayName("게시글 삭제 실패")
	public void deleteOpinion_2() throws Exception{
		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(1l);
		boardDto.setUserId("testId");

		given(opinionRepository.deleteByBoardIdAndUserId(boardDto.getBoardId(), boardDto.getUserId())).willReturn(0L);
		Map<String, Object> result = boardService.deleteOpinion(boardDto);

		Assertions.assertThat(result.get("message")).isEqualTo("fail");
	}
	

	@Test
	@DisplayName("사용자 의견 가져오기")
	public void getUserOpinion() throws Exception{

		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(1L);
		boardDto.setUserId("coolcjg");

		User user = new User();
		user.setUserId(boardDto.getUserId());

		Board board = new Board();
		board.setBoardId(boardDto.getBoardId());

		Opinion opinion = new Opinion();
		opinion.setUser(user);
		opinion.setOpinionId(1L);
		opinion.setBoard(board);
		opinion.setValue("Y");
		opinion.setRegDate(LocalDateTime.now());

		given(opinionRepository.findByBoardIdAndUserId(boardDto.getBoardId(), boardDto.getUserId())).willReturn(opinion);
		Map<String, Object> result= boardService.getUserOpinion(boardDto);

		assertThat(result.get("message")).isEqualTo("success");
						
	}

	@Test
	@DisplayName("사용자 의견 가져오기, 의견이 없을 때")
	public void getUserOpinion_2() throws Exception{

		BoardDto boardDto = new BoardDto();
		boardDto.setBoardId(1L);
		boardDto.setUserId("coolcjg");

		User user = new User();
		user.setUserId(boardDto.getUserId());

		Board board = new Board();
		board.setBoardId(boardDto.getBoardId());

		given(opinionRepository.findByBoardIdAndUserId(boardDto.getBoardId(), boardDto.getUserId())).willReturn(null);
		Map<String, Object> result= boardService.getUserOpinion(boardDto);

		assertThat(result.get("message")).isEqualTo("success");

	}

}

