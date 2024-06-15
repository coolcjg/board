package com.cjg.traveling.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileExtensionTest {

    @Test
    @DisplayName("금지 파일 체크")
    public void checkExtension_ban(){
        String fileName  = "abc.exe";
        String result = FileExtension.checkExtension(fileName);
        Assertions.assertThat(result).isEqualTo("ban");
    }

    @Test
    @DisplayName("동영상 체크")
    public void checkExtension_video(){
        String fileName  = "abc.mp4";
        String result = FileExtension.checkExtension(fileName);
        Assertions.assertThat(result).isEqualTo("video");
    }

    @Test
    @DisplayName("오디오 체크")
    public void checkExtension_audio(){
        String fileName  = "abc.mp3";
        String result = FileExtension.checkExtension(fileName);
        Assertions.assertThat(result).isEqualTo("audio");
    }

    @Test
    @DisplayName("이미지 체크")
    public void checkExtension_image(){
        String fileName  = "abc.jpg";
        String result = FileExtension.checkExtension(fileName);
        Assertions.assertThat(result).isEqualTo("image");
    }

    @Test
    @DisplayName("문서 체크")
    public void checkExtension_doc(){
        String fileName  = "abc.hwp";
        String result = FileExtension.checkExtension(fileName);
        Assertions.assertThat(result).isEqualTo("document");
    }
}
