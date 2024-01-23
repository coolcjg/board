package com.cjg.traveling.service;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cjg.traveling.domain.Media;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.repository.BoardRepository;
import com.cjg.traveling.repository.MediaRepository;
import com.cjg.traveling.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ApiServiceTest {
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	BoardRepository boardRepository;
	
	@Mock
	MediaRepository mediaRepository;
	
	
	
	@Test
	public void encodingResult(){
			
		List<MediaDto> paramList = new ArrayList();
		
		MediaDto mediaDto1 = new MediaDto();
		mediaDto1.setMediaId(1L);
		mediaDto1.setStatus("encoding");
		mediaDto1.setPercent(80);
		
		paramList.add(mediaDto1);
				
		MediaDto mediaDto2 = new MediaDto();
		mediaDto2.setMediaId(2L);
		mediaDto2.setStatus("success");
		mediaDto2.setPercent(100);
		
		paramList.add(mediaDto2);
		
		for(MediaDto param : paramList) {
			
			Media media = new Media();
			media.setMediaId(param.getMediaId());		
			
			//given
			given(mediaRepository.findByMediaId(param.getMediaId())).willReturn(media);
					
			//when
			Media findMedia = mediaRepository.findByMediaId(param.getMediaId());
			
			String status = param.getStatus();
			
			findMedia.setStatus(status);
			
			if(status.equals("encoding")) {
				findMedia.setPercent(param.getPercent());
			}else if(status.equals("success")) {
				findMedia.setEncodingFileName(param.getEncodingFileName());
				findMedia.setEncodingFilePath(param.getEncodingFilePath());
				findMedia.setEncodingFileSize(param.getEncodingFileSize());
				findMedia.setThumbnailPath(param.getThumbnailPath());
				findMedia.setPercent(100);
			}
			
			//then
			Assertions.assertThat(findMedia.getMediaId()).isEqualTo(param.getMediaId());
			Assertions.assertThat(findMedia.getStatus()).isEqualTo(param.getStatus());
			Assertions.assertThat(findMedia.getPercent()).isEqualTo(param.getPercent());			
			
		}

		
	}
	

}
