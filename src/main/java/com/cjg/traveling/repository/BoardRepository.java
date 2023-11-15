package com.cjg.traveling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cjg.traveling.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
	Page<Board> findPageBy(Pageable paging);
	
	

}
