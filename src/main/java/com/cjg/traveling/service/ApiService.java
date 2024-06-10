package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.domain.Media;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.repository.MediaRepository;

@Service
@Transactional
public class ApiService {
	
	Logger logger = LoggerFactory.getLogger(ApiService.class);
	
	@Autowired
	MediaRepository mediaRepository;
	
	public Map<String, Object> encodingResult(MediaDto dto){
		
		Media media = mediaRepository.findByMediaId(dto.getMediaId());
		
		String status = dto.getStatus();		
		media.setStatus(status);
		
		if(status.equals("encoding")) {
			media.setPercent(dto.getPercent());
		}else if(status.equals("success")) {
			media.setEncodingFileName(dto.getEncodingFileName());
			media.setEncodingFilePath(dto.getEncodingFilePath());
			media.setEncodingFileSize(dto.getEncodingFileSize());
			media.setThumbnailPath(dto.getThumbnailPath());
			media.setPercent(100);
		}
		
		Map<String, Object> result = new HashMap();
		result.put("message", "success");
		
		return result;
	}

}
