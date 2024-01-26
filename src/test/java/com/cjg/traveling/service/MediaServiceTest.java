package com.cjg.traveling.service;

import static org.mockito.BDDMockito.given;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cjg.traveling.domain.Media;
import com.cjg.traveling.repository.MediaRepository;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {
	
	@Mock
	MediaRepository mediaRepository;
	
	@Test
	public void deleteMedia() {
		
		Media media1 = new Media();
		media1.setMediaId(84L);
		media1.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media1.setType("document");
		media1.setStatus("success");
		media1.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media1.setOriginalFileName("9db1494c-fb39-4663-98b2-816d40782500.txt");
		media1.setOriginalFileClientName("1.txt");
		media1.setOriginalFileSize(3l);
		media1.setPercent(100);
		
		Media media2 = new Media();
		media2.setMediaId(85L);
		media2.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media2.setType("audio");
		media2.setStatus("success");
		media2.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media2.setOriginalFileName("41383286-3bcf-43eb-b22c-575298cd6c89.mp3");
		media2.setOriginalFileClientName("2.mp3");
		media2.setOriginalFileSize(5319693l);
		media2.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media2.setEncodingFileName("85.mp3");
		media2.setEncodingFileSize(3191886l);
		media2.setPercent(100);
		
		Media media3 = new Media();
		media3.setMediaId(86L);
		media3.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media3.setType("image");
		media3.setStatus("success");
		media3.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media3.setOriginalFileName("f5247aea-345b-4e5a-bf8d-1b562e1fd623.jpg");
		media3.setOriginalFileClientName("3.jpg");
		media3.setOriginalFileSize(278934l);
		media3.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media3.setEncodingFileName("86.jpg");
		media3.setEncodingFileSize(279650l);
		media3.setThumbnailPath("D:/NAS/upload/encoding/2024/01/24/86.jpg");
		media3.setPercent(100);
		
		Media media4 = new Media();
		media4.setMediaId(87L);
		media4.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media4.setType("video");
		media4.setStatus("success");
		media4.setOriginalFilePath("D:/NAS/upload/original/2024/01/24/");
		media4.setOriginalFileName("774bf5ff-cf9a-4c26-b608-2443c9ce211f.mp4");
		media4.setOriginalFileClientName("4.mp4");
		media4.setOriginalFileSize(10498677l);
		media4.setEncodingFilePath("D:/NAS/upload/encoding/2024/01/24/");
		media4.setEncodingFileName("87.mp4");
		media4.setEncodingFileSize(9328117l);
		media4.setThumbnailPath("D:/NAS/upload/encoding/2024/01/24/87.jpg");
		media4.setPercent(100);	
		
		List<Media> mediaListMock = new ArrayList();
		mediaListMock.add(media1);
		mediaListMock.add(media2);
		mediaListMock.add(media3);
		mediaListMock.add(media4);
		
		for(Media media : mediaListMock) {
			Long mediaId = media.getMediaId();
			
			Map<String, Object> result = new HashMap();
			
			given(mediaRepository.findByMediaId(mediaId)).willReturn(media);
			deleteMediaFile(mediaId);
			
			result.put("code", HttpServletResponse.SC_OK);
			result.put("message", "deleted");
		}
				

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
