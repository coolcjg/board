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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cjg.traveling.common.FileExtension;
import com.cjg.traveling.common.Jwt;
import com.cjg.traveling.common.PageUtil;
import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.Media;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.dto.BoardDTO;
import com.cjg.traveling.dto.MediaDTO;
import com.cjg.traveling.dto.UserDTO;
import com.cjg.traveling.repository.BoardRepository;
import com.cjg.traveling.repository.MediaRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional
public class BoardService {
	
	Logger logger = LoggerFactory.getLogger(BoardService.class);
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private MediaRepository mediaRepository;
	
	@Autowired
	private Jwt jwt;
	
	@Value("${uploadPath}")
	private String uploadPath;
	
	
	@Value("${serverUrl}")
	private String serverUrl;
	
	
	public Map<String, Object> list(BoardDTO boardDTO){
		
		Map<String, Object> map = new HashMap();
				
		PageRequest pageRequest = PageRequest.of(boardDTO.getPageNumber()-1, 10, Sort.Direction.DESC, "regDate");
		Page<Board> page = boardRepository.findPageBy(pageRequest);
		
		List<BoardDTO> boardList = new ArrayList();
		
		for(Board board : page.getContent()) {
			BoardDTO temp = new BoardDTO();
			
			temp.setBoardId(board.getBoardId());
			temp.setContents(board.getContents());
			temp.setRegDate(board.getRegDate());
			temp.setRegion(board.getRegion());
			temp.setTitle(board.getTitle());
			temp.setView(board.getView());
			
			UserDTO userDTO = new UserDTO();
			userDTO.setName(board.getUser().getName());
			userDTO.setUserId(board.getUser().getUserId());
			temp.setUserDTO(userDTO);
			
			boardList.add(temp);
			
		}
		
		
		map.put("code", "200");
		map.put("boardList", boardList);
		
		int pageNumber = page.getPageable().getPageNumber()+1;
		int totalPage = page.getTotalPages() == 0 ? 1 : page.getTotalPages();
		
		map.put("pageNumber", pageNumber);
		map.put("totalPage", totalPage);
		
		List<Integer> pagination = PageUtil.getStartEndPage(pageNumber, totalPage);
		map.put("pagination", pagination);
		
		return map;
	}
	
	public Map<String, Object> findByBoardId(long boardId) {
		
		Map<String, Object> map = new HashMap();
		BoardDTO boardDTO = new BoardDTO();
		UserDTO userDTO = new UserDTO();
		Board board = boardRepository.findByBoardId(boardId);
		
		boardDTO.setBoardId(board.getBoardId());
		boardDTO.setContents(board.getContents());
		boardDTO.setRegion(board.getRegion());
		boardDTO.setTitle(board.getTitle());
		boardDTO.setRegDate(board.getRegDate());
		
		userDTO.setName(board.getUser().getName());
		userDTO.setUserId(board.getUser().getUserId());
		
		boardDTO.setUserDTO(userDTO);
		
		List<MediaDTO> mediaDTOList = new ArrayList<>();
		for(Media media : board.getMediaList()) {
			MediaDTO mediaDTO = new MediaDTO();
			
			mediaDTO.setMediaId(media.getMediaId());
			mediaDTO.setType(media.getType());
			mediaDTO.setStatus(media.getStatus());
			mediaDTO.setOriginalFileUrl(serverUrl + media.getOriginalFilePath() + media.getOriginalFileName());
			
			if(media.getType().equals("video")) {
				mediaDTO.setThumbnailImgUrl(serverUrl + media.getThumbnailPath());
			}else if(media.getType().equals("audio")) {
				mediaDTO.setThumbnailImgUrl(serverUrl + "/image/audio.jpg");
			}else if(media.getType().equals("image")) {
				mediaDTO.setThumbnailImgUrl(serverUrl + media.getOriginalFilePath() + media.getOriginalFileName());
			}else {
				mediaDTO.setThumbnailImgUrl(serverUrl + "/image/document.jpg");
			}
			
			mediaDTOList.add(mediaDTO);
		}
		
		boardDTO.setMediaDTOList(mediaDTOList);
		
		map.put("board", boardDTO);
		map.put("code", 200);
		
		return map;
	}
	
	
	public Map<String, Object> save(HttpServletRequest request, BoardDTO boardDTO){
		
		Map<String, Object> map = new HashMap();
		
		String accessToken = request.getHeader("accessToken");
		String userId = jwt.getUserId(accessToken);
		
		User user = new User();
		user.setUserId(userId);
		
		Board board = new Board();
		board.setUser(user);
		board.setTitle(boardDTO.getTitle());
		board.setRegion(boardDTO.getRegion());
		board.setContents(boardDTO.getContents());
		
		Board newBoard = boardRepository.save(board);
		
		//업로드 파일
		uploadFile(newBoard, boardDTO.getFiles());
		
		
		map.put("code", "200");		
		return map;	
	}
	
	
	private boolean uploadFile(Board board, List<MultipartFile> fileList) {
		boolean result = true;
		
		LocalDate now = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String dateFormat = now.format(dtf);
		String originalPath = "/original/" +  dateFormat + "/";
		String path = uploadPath + originalPath;
		File dir = new File(path);
		
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		try {
			for(MultipartFile mf : fileList) {
				String uuid = UUID.randomUUID().toString();
				String extension = mf.getOriginalFilename().substring(mf.getOriginalFilename().lastIndexOf("."));
				String originalFileName = uuid + extension;
				File tempFile = new File(path + originalFileName);
				
				mf.transferTo(tempFile);
				
				Media media = new Media();
				media.setBoard(board);
				media.setType(FileExtension.checkExtension(mf.getOriginalFilename()));
				
				if(media.getType().equals("document")) {
					media.setStatus("success");					
				}else {
					media.setStatus("wait");
				}
				
				media.setOriginalFilePath(originalPath);
				media.setOriginalFileName(originalFileName);
				media.setOriginalFileClientName(mf.getOriginalFilename());
				media.setOriginalFileSize(mf.getSize());			
				
				mediaRepository.save(media);
			}			
		}catch(IOException e) {
			result = false;
			logger.error("ERROR : ", e);
		}
		
		return result;
	}

}
