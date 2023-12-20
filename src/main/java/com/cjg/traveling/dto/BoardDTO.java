package com.cjg.traveling.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
	
	private UserDTO userDTO;
	
	@JsonFormat(pattern="yyyy-MM-dd hh:mm")
	private LocalDateTime regDate;
	

}
