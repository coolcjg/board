package com.cjg.traveling.dto.board;


import lombok.Data;

@Data
public class DeleteOpinionDto {

    private Long boardId;

    private String userId;

    private String type = "like";

    private String value;
}
