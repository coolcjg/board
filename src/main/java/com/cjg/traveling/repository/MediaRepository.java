package com.cjg.traveling.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cjg.traveling.domain.Media;

public interface MediaRepository extends CrudRepository<Media, Long> {
	
	public Media save(Media media);
	
	public Media findByMediaId(Long mediaId);
	
	public List<Media> findByBoard_boardId(Long boardId);
	
}
