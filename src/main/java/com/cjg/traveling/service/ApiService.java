package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.domain.Media;
import com.cjg.traveling.dto.MediaDTO;
import com.cjg.traveling.repository.MediaRepository;

@Service
@Transactional
public class ApiService {
	
	@Autowired
	MediaRepository mediaRepository;
	
	public Map<String, Object> encodingResult(MediaDTO dto){
		
		System.out.println("encodingResult dto : " + dto.toString());
		
		Map<String, Object> result = new HashMap();
		result.put("code", HttpStatus.OK);
		result.put("message", "updated");

		Media media = mediaRepository.findByMediaId(dto.getMediaId());
		media.setEncodingFileName(dto.getEncodingFileName());
		media.setEncodingFilePath(dto.getEncodingFilePath());
		media.setEncodingFileSize(dto.getEncodingFileSize());
		media.setStatus(dto.getStatus());
		
		return result;
		
	}

}
