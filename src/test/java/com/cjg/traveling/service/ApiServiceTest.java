package com.cjg.traveling.service;

import com.cjg.traveling.domain.Media;
import com.cjg.traveling.dto.MediaDto;
import com.cjg.traveling.repository.MediaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ApiServiceTest {
	@Mock
	MediaRepository mediaRepository;

	@InjectMocks
	ApiService apiService;

	@Test
	@DisplayName("인코딩중")
	public void encodingResult_encoding(){

		//given
		MediaDto mediaDto = new MediaDto();
		mediaDto.setMediaId(1L);
		mediaDto.setStatus("encoding");
		mediaDto.setPercent(80);

		Media media = new Media();
		media.setMediaId(mediaDto.getMediaId());

		given(mediaRepository.findByMediaId(mediaDto.getMediaId())).willReturn(media);

		//when
		Map<String, Object> map = apiService.encodingResult(mediaDto);

		//then
		Assertions.assertThat((String)map.get("message")).isEqualTo("success");

	}

	@Test
	@DisplayName("인코딩 완료")
	public void encodingResult_success() {

		//given
		MediaDto mediaDto = new MediaDto();
		mediaDto.setMediaId(1L);
		mediaDto.setStatus("success");
		mediaDto.setPercent(100);
		mediaDto.setEncodingFileName("abc.mp4");
		mediaDto.setEncodingFilePath("/root/upload/encoding/2022/04/24/");
		mediaDto.setEncodingFileSize(54564564555L);
		mediaDto.setThumbnailPath("/root/upload/encoding/2022/04/24/");

		Media media = new Media();
		media.setMediaId(mediaDto.getMediaId());

		given(mediaRepository.findByMediaId(mediaDto.getMediaId())).willReturn(media);

		//when
		Map<String, Object> map = apiService.encodingResult(mediaDto);

		//then
		Assertions.assertThat((String)map.get("message")).isEqualTo("success");

	}

}
