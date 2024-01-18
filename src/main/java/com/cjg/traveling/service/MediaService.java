package com.cjg.traveling.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.repository.MediaRepository;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class MediaService {
	
	Logger logger = LoggerFactory.getLogger(MediaService.class);
	
	@Autowired
	MediaRepository mediaRepository;
	
	@Transactional
	public Map<String, Object> deleteMedia(Long mediaId) {
		
		Map<String, Object> result = new HashMap();
		mediaRepository.deleteByMediaId(mediaId);
		
		result.put("code", HttpServletResponse.SC_OK);
		result.put("message", "deleted");
		return result;
	}

}
