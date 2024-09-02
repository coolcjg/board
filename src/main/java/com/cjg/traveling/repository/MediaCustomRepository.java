package com.cjg.traveling.repository;

import com.cjg.traveling.domain.Media;

import java.util.List;

public interface MediaCustomRepository {
    public List<Media> findByBoardId(Long boardId);

    public long deleteByBoardId(Long boardId);

}
