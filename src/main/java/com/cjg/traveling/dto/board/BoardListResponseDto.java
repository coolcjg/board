package com.cjg.traveling.dto.board;

import com.cjg.traveling.dto.BoardDto;
import lombok.Data;

import java.util.List;

@Data
public class BoardListResponseDto {
    List<BoardDto> boardList;
    int pageNumber;
    int totalPage;
    List<Integer> pagination;
}
