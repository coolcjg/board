package com.cjg.traveling.repository;

import com.cjg.traveling.domain.Opinion;

import java.util.List;

public interface OpinionCustomRepository {

    Opinion findByBoardIdAndUserId(Long boardId, String userId);

    Long deleteByBoardIdAndUserId(Long boardId, String userId);

    List<Opinion> findByBoardId(Long boardId);

    Long deleteByBoardId(Long boardId);

}
