package com.cjg.traveling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cjg.traveling.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board> {
		
	Board save(Board board);
	
	Board findByBoardId(long boardId);
	
	void deleteByBoardId(long boardId);
	
}
