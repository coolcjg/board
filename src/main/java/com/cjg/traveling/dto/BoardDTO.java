package com.cjg.traveling.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.cjg.traveling.domain.Media;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BoardDTO {
	
	private Long boardId;
	
	private int pageNumber;
	
	private String title;
	
	private String region; 
	
	private String contents;
	
	private List<MultipartFile> files;
	
	private List<MediaDTO> mediaDTOList;
	
	private UserDTO userDTO;
	
	private int view;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime regDate;
	
	private String searchType;
	
	private String searchText;
	
	private String boardIdArray;

}
