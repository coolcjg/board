package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class MediaDTO {
	
	private Long mediaId;
	
	private String type;
	
	private String status;
	
	private String originalFilePath;

	private String originalFileName;
	
	private String originalFileUrl;
	
}
