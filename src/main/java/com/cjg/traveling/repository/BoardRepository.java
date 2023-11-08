package com.cjg.traveling.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.cjg.traveling.domain.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {
	
	List<Board> findAll(Pageable paging);
	
	

}
