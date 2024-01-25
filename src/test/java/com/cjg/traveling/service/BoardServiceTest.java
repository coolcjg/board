package com.cjg.traveling.service;

import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cjg.traveling.common.FileExtension;
import com.cjg.traveling.common.HttpRequestUtil;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.PageUtil;
import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.Media;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardSpecs;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.BoardRepository;
import com.cjg.traveling.repository.MediaRepository;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
	
	Logger logger = LoggerFactory.getLogger(BoardServiceTest.class);
	
	@Mock
	private BoardRepository boardRepository;
	
	@Mock
	private MediaRepository mediaRepository;
	
	@InjectMocks
	private MediaService mediaService;
	
	@Mock
	private HttpRequestUtil httpRequestUtil;
	
	@InjectMocks
	private Jwt jwt;
	
	@Value("${uploadPath}")
	private static String uploadPath;
	
	@Value("${serverUrl}")
	private static String serverUrl;
	
	@Value("${encodeReturnUrl}")
	private static String encodeReturnUrl;
	
	@BeforeAll
	public static void setUp() {
	    ReflectionTestUtils.setField(BoardServiceTest.class, "uploadPath", "D:/NAS/upload/");
	    ReflectionTestUtils.setField(BoardServiceTest.class, "serverUrl", "http://localhost:8080");
	    ReflectionTestUtils.setField(BoardServiceTest.class, "encodeReturnUrl", "http://localhost:8080/api/encodingResult");
	}	
	
	@Test
	public void list(){
				
		List<Map<String,String>> paramList = new ArrayList();
		
		Map<String,String> map1 = new HashMap();
		map1.put("pageNumber", null);
		map1.put("searchType", "all");
		map1.put("searchText", "test");		
		
		Map<String,String> map2 = new HashMap();
		map2.put("pageNumber", "1");
		map2.put("searchType", "title");
		map2.put("searchText", "testTitle");
		
		Map<String,String> map3 = new HashMap();
		map3.put("pageNumber", "1");
		map3.put("searchType", "region");
		map3.put("searchText", "seoul");
		
		Map<String,String> map4 = new HashMap();
		map4.put("pageNumber", "1");
		map4.put("searchType", "userId");
		map4.put("searchText", "----");
		
		Map<String,String> map5 = new HashMap();
		map5.put("pageNumber", "1");
		map5.put("searchType", "regDate");
		map5.put("searchText", "2024-01-23~2024-01-23");
		
		Map<String,String> map6 = new HashMap();
		map6.put("pageNumber", "1");
		map6.put("searchType", null);
		map6.put("searchText", null);		
		
		paramList.add(map1);
		paramList.add(map2);
		paramList.add(map3);
		paramList.add(map4);
		paramList.add(map5);
		paramList.add(map6);
		
		List<Board> boardListMock = new ArrayList();
		
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
		
		boardListMock.add(boardMock1);
		boardListMock.add(boardMock2);
		boardListMock.add(boardMock3);
		boardListMock.add(boardMock4);
		boardListMock.add(boardMock5);
		
		for(Map<String, String> map : paramList) {
			
			Map<String, Object> result = new HashMap<String, Object>();
			
			BoardDto boardDTO = new BoardDto();
			int pageNumber=1;
			
			if(map.get("pageNumber") != null && !map.get("pageNumber").toString().equals("")) {
				pageNumber = Integer.parseInt(map.get("pageNumber").toString());
			}
			
			boardDTO.setPageNumber(pageNumber);
			
			Specification<Board> specification = null;
			if(map.get("searchText") != null && !map.get("searchText").toString().equals("")) {
				Map<String, Object> whereParam = new HashMap<String, Object>();
				whereParam.put("searchType", map.get("searchType"));
				whereParam.put("searchText", map.get("searchText"));
				specification = BoardSpecs.searchWith(whereParam);
			}
					
			PageRequest pageRequest = PageRequest.of(boardDTO.getPageNumber()-1, 10, Sort.Direction.DESC, "regDate");
			Page<Board> page;

			
			Pageable pageableMock = PageRequest.of(0, 10);
			page = new PageImpl(boardListMock, pageableMock, 10l);
			
			if(specification == null) {				
				given(boardRepository.findAll(pageRequest)).willReturn(page);
				page = boardRepository.findAll(pageRequest);
			}else {
				given(boardRepository.findAll(specification, pageRequest)).willReturn(page);
				page = boardRepository.findAll(specification, pageRequest);
			}
			
			List<BoardDto> boardList = new ArrayList();
			
			for(Board board : page.getContent()) {
				BoardDto temp = new BoardDto();
				
				temp.setBoardId(board.getBoardId());
				temp.setContents(board.getContents());
				temp.setRegDate(board.getRegDate());
				temp.setRegion(board.getRegion());
				temp.setTitle(board.getTitle());
				temp.setView(board.getView());
				
				UserDto userDTO = new UserDto();
				userDTO.setName(board.getUser().getName());
				userDTO.setUserId(board.getUser().getUserId());
				temp.setUserDTO(userDTO);
				
				boardList.add(temp);
				
			}
			
			result.put("code", "200");
			result.put("boardList", boardList);
			
			int totalPage = page.getTotalPages() == 0 ? 1 : page.getTotalPages();
			result.put("pageNumber", page.getPageable().getPageNumber()+1);
			result.put("totalPage", totalPage);
			
			List<Integer> pagination = PageUtil.getStartEndPage(pageNumber, totalPage);
			result.put("pagination", pagination);				
			
			Assertions.assertThat(boardList.size()).isEqualTo(boardListMock.size());
		}


	}
	
	@Test
	public void findByBoardId() {
		
		List<Board> mockList = new ArrayList();
		
		mockList.add(null);
		
		Long boardId = 1L;
		Board board1 = new Board();
		board1.setBoardId(boardId);
		
		User userMock = new User();
		userMock.setUserId("coolcjg");
		userMock.setPassword("cda4a7cf32fed2e86a3ad118720560b94c40bf5b60a95059419c81734140a6ac");
		userMock.setSalt("7b963ebdefad18a43bcd0bd22d4a888b364a9044");
		userMock.setName("최종규");
		LocalDate birthDay = LocalDate.of(1990, 1, 24);		
		userMock.setBirthDay(birthDay);
		userMock.setRefreshToken("eyJhbGciOiJIUzM4NCJ9.eyJuYW1lIjoi7LWc7KKF6recIiwiaWQiOiJjb29sY2pnIiwiaWF0IjoxNzA2MDg0NDk3LCJleHAiOjE3MDg3NjI4OTd9.SsGyUqiZBAzCKvScLE30zx-6B2Znyb1iwdmJ8g2X_rsqChAnir1xcbO7j6V5YYZu");
		
		board1.setUser(userMock);
		
		board1.setTitle("22");
		board1.setContents("323231");
		board1.setRegion("인천");
		board1.setRegDate(LocalDateTime.of(2024, 1, 21, 19, 40, 18));
		board1.setView(10);
		
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
		
		board1.setMediaList(mediaListMock);
		
		mockList.add(board1);
		
		for(Board boardMock : mockList) {
			
			Map<String, Object> map = new HashMap();
			BoardDto boardDTO = new BoardDto();
			UserDto userDTO = new UserDto();
			
			given(boardRepository.findByBoardId(boardId)).willReturn(boardMock);
			
			Board board = boardRepository.findByBoardId(boardId);
			
			if(board == null) {
				map.put("code", 500);
				map.put("message", "board is null");
				
				Assertions.assertThat(board).isEqualTo(boardMock);
				
				return;
			}
			
			//조회수 통계 추가
			board.setView(board.getView()+1);
			
			boardDTO.setBoardId(board.getBoardId());
			boardDTO.setContents(board.getContents());
			boardDTO.setRegion(board.getRegion());
			boardDTO.setTitle(board.getTitle());
			boardDTO.setRegDate(board.getRegDate());
			
			userDTO.setName(board.getUser().getName());
			userDTO.setUserId(board.getUser().getUserId());
			
			boardDTO.setUserDTO(userDTO);
			
			List<MediaDto> mediaDTOList = new ArrayList<>();
			for(Media media : board.getMediaList()) {
				MediaDto mediaDTO = new MediaDto();
				
				mediaDTO.setMediaId(media.getMediaId());
				mediaDTO.setType(media.getType());
				mediaDTO.setStatus(media.getStatus());
				mediaDTO.setOriginalFileClientName(media.getOriginalFileClientName());
				
				if(media.getStatus().equals("success")) {
								
					if(media.getType().equals("video")) {
						
						mediaDTO.setEncodingFileUrl(serverUrl 
								+ media.getEncodingFilePath().substring( media.getEncodingFilePath().indexOf("/upload/") ) 
								+ media.getEncodingFileName());					
					
						mediaDTO.setThumbnailImgUrl(serverUrl 
													+ media.getThumbnailPath().substring(media.getThumbnailPath().indexOf("/upload/")));
					}else if(media.getType().equals("audio")) {
						
						mediaDTO.setEncodingFileUrl(serverUrl 
								+ media.getEncodingFilePath().substring( media.getEncodingFilePath().indexOf("/upload/") ) 
								+ media.getEncodingFileName());
						
						mediaDTO.setThumbnailImgUrl(serverUrl + "/image/audio.jpg");
					}else if(media.getType().equals("image")) {
						
						mediaDTO.setEncodingFileUrl(serverUrl 
								+ media.getEncodingFilePath().substring( media.getEncodingFilePath().indexOf("/upload/") ) 
								+ media.getEncodingFileName());
						
						mediaDTO.setThumbnailImgUrl(serverUrl + media.getThumbnailPath().substring(media.getThumbnailPath().indexOf("/upload/")));
					}else {
						mediaDTO.setEncodingFileUrl(serverUrl + "/image/document-large.jpg");
						mediaDTO.setThumbnailImgUrl(serverUrl + "/image/document.jpg");
					}
				}
				
				mediaDTOList.add(mediaDTO);
			}
			
			boardDTO.setMediaDTOList(mediaDTOList);
			
			map.put("board", boardDTO);
			map.put("code", 200);
			map.put("message", "success");
			
			Assertions.assertThat(board).isEqualTo(boardMock);
		}

		
	}
	
	@Test
	public void save() throws Exception{
		
		//given		
		UserDto userDtoMock = new UserDto();
		userDtoMock.setUserId("coolcjg");
		userDtoMock.setName("최종규");
		
		User userMock = new User();
		userMock.setUserId("coolcjg");
		String accessToken = jwt.createAccessToken(userMock);
		
		//첨부 미디어가 있을 때
		BoardDto boardDto1 = new BoardDto();
		boardDto1.setUserDTO(userDtoMock);
		boardDto1.setTitle("1");
		
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
		
		boardDto1.setFiles(multiList);
		
		//첨부 미디어가 없을 때
		BoardDto boardDto2 = new BoardDto();
		boardDto2.setUserDTO(userDtoMock);
		boardDto2.setTitle("2");
		
		List<BoardDto> paramList = new ArrayList();
		paramList.add(boardDto1);
		paramList.add(boardDto2);
		
		for(BoardDto boardDto : paramList) {
			
			//구현 영역
			String userId = jwt.getUserId(accessToken);
			
			User user = new User();
			user.setUserId(userId);
			
			Board board = new Board();
			board.setUser(user);
			board.setTitle(boardDto.getTitle());
			board.setRegion(boardDto.getRegion());
			board.setContents(boardDto.getContents());
			
			given(boardRepository.save(board)).willReturn(board);
			Board newBoard = boardRepository.save(board);
			
			Assertions.assertThat(newBoard).isEqualTo(board);
			
			checkUploadFile(boardDto, newBoard);
			
		}
		
	}
	
	// 파일 업로드 체크
	private void checkUploadFile(BoardDto boardDTO, Board board) throws Exception {
		
		//업로드 파일
		if(boardDTO.getFiles() != null && boardDTO.getFiles().size() > 0) {
			List<Map<String, String>>  mediaList = uploadFile(board, boardDTO.getFiles());
			
			//업로드 서버 요청 전달
			for(Map<String, String> media : mediaList) {
				Map<String, String> encodingParam = new HashMap();
				encodingParam.put("mediaId", media.get("mediaId"));
				encodingParam.put("type", media.get("type"));
				encodingParam.put("originalFile", media.get("originalFile"));
				encodingParam.put("returnUrl", encodeReturnUrl);
				
				given(httpRequestUtil.encodingRequest(encodingParam)).willReturn("success");
				String postResult = httpRequestUtil.encodingRequest(encodingParam);
				logger.info("postResult");
				logger.info(postResult);
			}	
			
		}		
		
	}	
	
	// 파일 업로드	
	private List<Map<String,String>> uploadFile(Board board, List<MultipartFile> fileList) {
		List<Map<String,String>> result = new ArrayList();
		
		LocalDate now = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String dateFormat = now.format(dtf);
		String originalPath = uploadPath + "original/" +  dateFormat + "/";
		File dir = new File(originalPath);
		
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			for(MultipartFile mf : fileList) {
				String uuid = UUID.randomUUID().toString();
				String extension = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf("."));
				String originalFileName = uuid + extension;
				File tempFile = new File(originalPath + originalFileName);
				
				mf.transferTo(tempFile);
				
				Media media = new Media();
				media.setBoard(board);
				media.setType(FileExtension.checkExtension(mf.getOriginalFilename()));
				
				if(media.getType().equals("document")) {
					media.setStatus("success");
					media.setPercent(100);
				}else {
					media.setStatus("wait");
				}
				
				media.setOriginalFilePath(originalPath);
				media.setOriginalFileName(originalFileName);
				media.setOriginalFileClientName(mf.getOriginalFilename());
				media.setOriginalFileSize(mf.getSize());			
				
				given(mediaRepository.save(media)).willReturn(media);
				
				Media newMedia = mediaRepository.save(media);
				
				if(media.getType().equals("video") || media.getType().equals("audio") || media.getType().equals("image")) {
					Map<String, String> mediaMap = new HashMap();
					mediaMap.put("mediaId", newMedia.getMediaId() + "");
					mediaMap.put("type", newMedia.getType());
					mediaMap.put("originalFile", newMedia.getOriginalFilePath() + newMedia.getOriginalFileName());
					result.add(mediaMap);
				}
				
			}			
		}catch(IOException e) {
			logger.error("ERROR : ", e);
		}
		
		return result;
	}
	
	@Test
	public void updateBoard() throws Exception{
		
		BoardDto boardDto1 = new BoardDto();
		boardDto1.setBoardId(1l);
		
		BoardDto boardDto2 = new BoardDto();
		boardDto2.setBoardId(2l);
				
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
		
		boardDto2.setFiles(multiList);
		
		List<BoardDto> paramList = new ArrayList();
		paramList.add(boardDto1);
		paramList.add(boardDto2);
		
		for(BoardDto boardDto : paramList) {

			//given
			Board boardMock = new Board();
			boardMock.setBoardId(boardDto.getBoardId());
			boardMock.setTitle("testTitle");
			boardMock.setRegion("경기");
			boardMock.setContents("test");
			
			given(boardRepository.findByBoardId(boardDto.getBoardId())).willReturn(boardMock);
			
			Board board = boardRepository.findByBoardId(boardDto.getBoardId());
			board.setTitle(boardDto.getTitle());
			board.setRegion(boardDto.getRegion());
			board.setContents(boardDto.getContents());
					
			checkUploadFile(boardDto, board);
			
		}
		
	}
	
	
	@Test
	public void deleteBoard() throws Exception{
		//HttpServletRequest request,
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
		
		List<Media> mediaListMock = new ArrayList();
		mediaListMock.add(media1);
		mediaListMock.add(media2);
		mediaListMock.add(media3);
		mediaListMock.add(media4);		
				
		
		Map<String, Object> result = new HashMap();
		
		String[] boardIdArray = boardDto.getBoardIdArray().split(",");
		
		
		for(String boardIdString : boardIdArray) {
			
			long boardId = Long.parseLong(boardIdString);
			
			given(mediaRepository.findByBoard_boardId(boardId)).willReturn(mediaListMock);
			
			// 미디어 삭제
			List<Media> mediaList = mediaRepository.findByBoard_boardId(boardId);
			
			for(Media media: mediaList) {
				
				given(mediaRepository.findByMediaId(media.getMediaId())).willReturn(media);
				mediaService.deleteMediaFile(media.getMediaId());			
			}

			// 게시글 삭제
			boardRepository.deleteByBoardId(boardId);
		}
		
		result.put("code", HttpServletResponse.SC_OK);
		result.put("message", "boards deleted");
	}
}
