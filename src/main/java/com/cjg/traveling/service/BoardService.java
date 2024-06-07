package com.cjg.traveling.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cjg.traveling.common.DateFormat;
import com.cjg.traveling.common.FileExtension;
import com.cjg.traveling.common.HttpRequestUtil;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.PageUtil;
import com.cjg.traveling.common.kafka.KafkaProducer;
import com.cjg.traveling.domain.Alarm;
import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.Media;
import com.cjg.traveling.domain.Opinion;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.AlarmDto;
import com.cjg.traveling.dto.BoardDto;
import com.cjg.traveling.dto.BoardSpecs;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.dto.OpinionDto;
import com.cjg.traveling.dto.UserDto;
import com.cjg.traveling.repository.AlarmRepository;
import com.cjg.traveling.repository.BoardRepository;
import com.cjg.traveling.repository.MediaRepository;
import com.cjg.traveling.repository.OpinionRepository;
import com.cjg.traveling.repository.UserRepository;
import com.cjg.traveling.status.AlarmType;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional(rollbackFor = Exception.class)
public class BoardService {
	
	Logger logger = LoggerFactory.getLogger(BoardService.class);
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	@Autowired
	private OpinionRepository opinionRepository;
	
	@Autowired
	private OpinionService opinionService;		
	
	@Autowired
	private UserRepository userRepository;	
	
	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private AlarmService alaramService;
	
	@Autowired
	private HttpRequestUtil httpRequestUtil;
	
	@Autowired
	private Jwt jwt;
	
	@Autowired
	private AlarmService alarmService;
	
	@Autowired
	private KafkaProducer kafkaProducer;	
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	
	@Value("${serverUrl}")
	private String serverUrl;
	
	public Map<String, Object> list(Map<String, String> map){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		BoardDto boardDTO = new BoardDto();
		int pageNumber=1;
		
		if(map.get("pageNumber") != null && !map.get("pageNumber").isEmpty()) {
			pageNumber = Integer.parseInt(map.get("pageNumber"));
		}
		
		boardDTO.setPageNumber(pageNumber);
		
		Specification<Board> specification = null;
		if(map.get("searchText") != null && !map.get("searchText").isEmpty()) {
			Map<String, Object> whereParam = new HashMap<String, Object>();
			whereParam.put("searchType", map.get("searchType"));
			whereParam.put("searchText", map.get("searchText"));
			specification = BoardSpecs.searchWith(whereParam);
		}
				
		PageRequest pageRequest = PageRequest.of(boardDTO.getPageNumber()-1, 10, Sort.Direction.DESC, "regDate");
		Page<Board> page;
		
		if(specification == null) {
			page = boardRepository.findAll(pageRequest);
		}else {
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
		
		result.put("message", "success");
		result.put("boardList", boardList);
		
		int totalPage = page.getTotalPages() == 0 ? 1 : page.getTotalPages();
		result.put("pageNumber", page.getPageable().getPageNumber()+1);
		result.put("totalPage", totalPage);
		
		List<Integer> pagination = PageUtil.getStartEndPage(pageNumber, totalPage);
		result.put("pagination", pagination);
		
		return result;
	}
	
	public Map<String, Object> findByBoardId(long boardId) {
		
		Map<String, Object> map = new HashMap();
		BoardDto boardDTO = new BoardDto();
		UserDto userDTO = new UserDto();
		Board board = boardRepository.findByBoardId(boardId);
		
		if(board == null) {
			map.put("message", "게시글이 존재하지 않습니다.");
			return map; 
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
		map.put("message", "success");
		
		return map;
	}
	
	
	public Map<String, Object> save(BoardDto boardDto) throws Exception{
		
		Map<String, Object> map = new HashMap();

		User user = new User();
		user.setUserId(boardDto.getUserId());

		Board board = new Board();
		board.setUser(user);
		board.setTitle(boardDto.getTitle());
		board.setRegion(boardDto.getRegion());
		board.setContents(boardDto.getContents());
		
		Board newBoard = boardRepository.save(board);
		
		checkUploadFile(boardDto, newBoard);
		
		map.put("message", "success");
		return map;	
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
				encodingParam.put("returnUrl", serverUrl + "/api/encodingResult");
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
				
				Media newMedia = mediaRepository.save(media);
				System.out.println("new Media");
				System.out.println(newMedia);
				
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
	
	public Map<String, Object> updateBoard(BoardDto boardDto) throws Exception{
		
		Map<String, Object> result = new HashMap();
		
		Board board = boardRepository.findByBoardId(boardDto.getBoardId());
		board.setTitle(boardDto.getTitle());
		board.setRegion(boardDto.getRegion());
		board.setContents(boardDto.getContents());
				
		checkUploadFile(boardDto, board);

		result.put("message", "success");
		return result;
	}
	
	public Map<String, Object> deleteBoard(BoardDto boardDto) throws Exception{
		
		Map<String, Object> result = new HashMap();
		
		String[] boardIdArray = boardDto.getBoardIdArray().split(",");
		
		
		for(String boardIdString : boardIdArray) {
			
			long boardId = Long.parseLong(boardIdString);
			
			// 미디어 삭제
			List<Media> mediaList = mediaRepository.findByBoard_boardId(boardId);
			for(Media media: mediaList) {
				mediaService.deleteMediaFile(media.getMediaId());
			}
			
			// 좋아요 삭제
			opinionService.deleteByBoard_boardId(boardId);
			
			// 알람 삭제
			alaramService.deleteByBoard_boardId(boardId);		

			// 게시글 삭제
			boardRepository.deleteByBoardId(boardId);
		}

		result.put("message", "success");
		return result;
		
	}
	
	public Map<String, Object> postOpinion(BoardDto boardDto) throws Exception{
		Map<String, Object> result = new HashMap();
		
		Opinion opinion = opinionRepository.findByBoard_boardIdAndUser_userId(boardDto.getBoardId(), boardDto.getUserId());
		
		// Opinion테이블 처리
		if(opinion == null) {
			User user = userRepository.findByUserId(boardDto.getUserId());
			Board board = boardRepository.findByBoardId(boardDto.getBoardId());
			
			Opinion newOpinion = new Opinion();
			newOpinion.setUser(user);
			newOpinion.setBoard(board);
			newOpinion.setValue(boardDto.getValue());
			
			Opinion savedOpinion = opinionRepository.save(newOpinion);
			
			
			//Alarm테이블 처리(여기 작업해야함)
			if(!board.getUser().getUserId().equals(user.getUserId())
					&& //alarmRepository.findByBoard_boardIdAndFromUser_userId(boardDto.getBoardId(), boardDto.getUserId()).size() == 0) {
					true) {
				
				// Alarm테이블 저장
				Alarm newAlarm = new Alarm();
				newAlarm.setType(AlarmType.LIKE.getText());
				newAlarm.setValue(savedOpinion.getValue());
				newAlarm.setBoard(board);
				newAlarm.setFromUser(user);
				newAlarm.setToUser(board.getUser());
				Alarm savedAlarm = alarmRepository.save(newAlarm);
				
				// KAFKA PRODUCER 처리				
				Gson gson = new Gson();
				String opinionString = gson.toJson(alarmService.setAlarmDto(savedAlarm));
				
				kafkaProducer.create("opinion", opinionString);				
			}else {
				logger.info("알람 이미 존재함");
			}
			
		}else {
			opinion.setValue(boardDto.getValue());
		}

		result.put("message", "success");
		return result;
	}

	public Map<String, Object> deleteOpinion(BoardDto boardDto) throws Exception{
		Map<String, Object> result = new HashMap();
		
		Long deleteResult = opinionRepository.deleteByBoard_boardIdAndUser_userId(boardDto.getBoardId(), boardDto.getUserId());
		
		if(deleteResult == 1) {
			result.put("message", "success");
		}else {
			result.put("message", "fail");
		}

		return result;		
		
	}
	
	public Map<String, Object> getUserOpinion(BoardDto boardDto) throws Exception{
		Map<String, Object> result = new HashMap();
		
		Opinion opinion = opinionRepository.findByBoard_boardIdAndUser_userId(boardDto.getBoardId(), boardDto.getUserId());
		
		if(opinion == null) {
			result.put("opinion", "");
		}else {
			OpinionDto opinionDto = new OpinionDto();
			opinionDto.setBoardId(opinion.getBoard().getBoardId());
			opinionDto.setUserId(opinion.getUser().getUserId());
			opinionDto.setValue(opinion.getValue());
			result.put("opinion", opinionDto);
		}

		result.put("message", "success");
		return result;
	}
}
