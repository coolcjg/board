package com.cjg.traveling.repository;

import com.cjg.traveling.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BoardRepository extends JpaRepository<Board, Long>, JpaSpecificationExecutor<Board>, BoardCustomRepository {
	Board save(Board board);

	Board findByBoardId(long boardId);
	
	void deleteByBoardId(long boardId);
	
}
