package com.cjg.traveling.dto.board;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PutBoardRequestDto extends PostBoardRequestDto {
    private Long boardId;
}
