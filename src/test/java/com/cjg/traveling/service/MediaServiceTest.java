package com.cjg.traveling.service;

import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.Media;
import com.cjg.traveling.repository.MediaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {
	
	@Mock
	MediaRepository mediaRepository;

	@InjectMocks
	MediaService mediaService;

	@Test
	@DisplayName("미디어 가져오기")
	public void getMedia(){
		Long mediaId = 1L;

		Board board = new Board();
		board.setBoardId(1L);
		board.setTitle("제목");

		Media media = new Media();
		media.setMediaId(mediaId);
		media.setBoard(board);

		given(mediaRepository.findByMediaId(mediaId)).willReturn(media);

		Map<String,Object> result = mediaService.getMedia(mediaId);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
		Assertions.assertThat(result.get("data")).isNotNull();

	}
	
	@Test
	@DisplayName("미디어 삭제")
	public void deleteMedia() throws Exception {

		Long mediaId = 87L;

		String originalFilePath = "D:/NAS/uploadTest/original/2024/01/24/";
		File path1 = new File(originalFilePath);
		path1.mkdirs();

		String originalFileName = "774bf5ff-cf9a-4c26-b608-2443c9ce211f.mp4";
		File originalFile = new File(originalFilePath + originalFileName);
		originalFile.createNewFile();

		String encodingFilePath = "D:/NAS/uploadTest/encoding/2024/01/24/";
		File path2 = new File(encodingFilePath);
		path2.mkdirs();

		String encodingFileName = "87.mp4";
		File encodingFile = new File(encodingFilePath + encodingFileName);
		encodingFile.createNewFile();

		String thumbnailPath = "D:/NAS/uploadTest/encoding/2024/01/24/87.jpg";
		File thumbnailFile = new File(thumbnailPath);
		thumbnailFile.createNewFile();

		Media media = new Media();
		media.setMediaId(mediaId);
		media.setRegDate(LocalDateTime.of(2024, 1, 24, 17, 21, 47));
		media.setType("video");
		media.setStatus("success");
		media.setOriginalFilePath(originalFilePath);
		media.setOriginalFileName(originalFileName);
		media.setOriginalFileClientName("4.mp4");
		media.setOriginalFileSize(10498677l);
		media.setEncodingFilePath(encodingFilePath);
		media.setEncodingFileName(encodingFileName);
		media.setEncodingFileSize(9328117l);
		media.setThumbnailPath(thumbnailPath);
		media.setPercent(100);

		List<Media> list = new ArrayList();
		list.add(media);

		given(mediaRepository.findByMediaId(media.getMediaId())).willReturn(media);
		given(mediaRepository.deleteByMediaId(media.getMediaId())).willReturn(list);

		Map<String,Object> result = mediaService.deleteMedia(mediaId);

		Assertions.assertThat(result.get("message")).isEqualTo("success");
	}

}
