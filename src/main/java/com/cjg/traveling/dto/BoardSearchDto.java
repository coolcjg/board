package com.cjg.traveling.dto;

import lombok.Data;

@Data
public class BoardSearchDto {
    private int pageNumber=1;
    private String searchType="";
    private String searchText="";
}
