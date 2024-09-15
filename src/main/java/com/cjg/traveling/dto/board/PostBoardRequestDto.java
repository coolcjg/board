package com.cjg.traveling.dto.board;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostBoardRequestDto {

    private String userId;
    private String title;
    private String region;
    private String contents;
    List<MultipartFile> files;
}
