package com.cjg.traveling.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardDto {
	
	@NotNull(groups = {BoardDtoUpdate.class})
	private Long boardId;
	
	private int pageNumber;
	
	@NotBlank(groups = {BoardDtoInsert.class, BoardDtoUpdate.class})
	private String title;
	
	@NotBlank(groups = {BoardDtoInsert.class, BoardDtoUpdate.class})
	private String region; 
	
	private String contents;
	
	private List<MultipartFile> files;
	
	private List<MediaDto> mediaDTOList;
	
	private UserDto userDTO;
	
	private int view;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime regDate;
	
	private String searchType;
	
	private String searchText;
	
	private String boardIdArray;
	
	private String opinion;

}
