package com.cjg.traveling.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cjg.traveling.domain.Media;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.repository.MediaRepository;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class MediaService {
	
	Logger logger = LoggerFactory.getLogger(MediaService.class);
	
	@Autowired
	MediaRepository mediaRepository;
	
	public Map<String, Object> getMedia(Long mediaId) {
		Map<String, Object> result = new HashMap();
		
		Media media = mediaRepository.findByMediaId(mediaId);
		
		MediaDto mediaDto = new MediaDto();
		mediaDto.setMediaId(media.getMediaId());
		mediaDto.setBoardId(media.getBoard().getBoardId());
		mediaDto.setTitle(media.getBoard().getTitle());
		
		result.put("data", mediaDto);
		result.put("code", HttpServletResponse.SC_OK);
		result.put("message", "success");		
		
		return result;
	}	
	
	@Transactional
	public Map<String, Object> deleteMedia(Long mediaId) {
		
		Map<String, Object> result = new HashMap();
		
		deleteMediaFile(mediaId);
		
		result.put("code", HttpServletResponse.SC_OK);
		result.put("message", "deleted");
		return result;
	}

	public void deleteMediaFile(Long mediaId) {
		Media media = mediaRepository.findByMediaId(mediaId);
		
		File originalFile = new File(media.getOriginalFilePath() + media.getOriginalFileName());
		File encodingFile = new File(media.getEncodingFilePath() + media.getEncodingFileName());

		if(originalFile.isFile()) {
			originalFile.delete();
		}
		
		if(encodingFile.isFile()) {
			encodingFile.delete();
		}
		
		if(media.getType().equals("video")) {
			File thumbFile = new File(media.getThumbnailPath());
			if(thumbFile.isFile()) {
				thumbFile.delete();
			}
		}
		
		mediaRepository.deleteByMediaId(mediaId);
		
	}
	
}
