package com.cjg.traveling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
public class MediaDto {
	
	private Long mediaId;
	private Long boardId;
	
	private String title;
	
	private String type;
	
	private String status;
	
	private String originalFilePath;
	private String originalFileName;
	private String originalFileClientName;
	
	private String encodingFilePath;
	private String encodingFileName;
	private Long encodingFileSize;
	private String encodingFileUrl;
		
	private String originalFileUrl;
	
	private String thumbnailPath;
	
	private String thumbnailImgUrl;
	
	private int percent;
	
}

