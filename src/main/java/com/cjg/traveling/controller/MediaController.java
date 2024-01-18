package com.cjg.traveling.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cjg.traveling.service.MediaService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MediaController {
	
	@Autowired
	MediaService mediaService;
	
	@DeleteMapping(value ="/media/{mediaId}")
	public Map<String, Object> deleteMedia(HttpServletRequest request, @PathVariable("mediaId") long mediaId) throws Exception{
		return mediaService.deleteMedia(mediaId);
	}	
	
}
