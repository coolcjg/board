package com.cjg.traveling.repository;

import com.cjg.traveling.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    Page<Board> selectBoardPage(Pageable pageable, String searchType, String searchText);
}
