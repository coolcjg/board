package com.cjg.traveling.dto.board;


import lombok.Data;

@Data
public class PostBoardRequestDto {

    private String userId;
    private String title;
    private String region;
    private String contents;
}
