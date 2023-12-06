package com.cjg.traveling.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardDTO {
	
	private int pageNumber;
	
	private String title;
	
	private String region;
	
	private String contents;
	
	private List<MultipartFile> files;
	

}
