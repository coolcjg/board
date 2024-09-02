package com.cjg.traveling.repository;

import com.cjg.traveling.domain.Media;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cjg.traveling.domain.QMedia.media;

@Repository
@AllArgsConstructor
public class MediaCustomRepositoryImpl implements MediaCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Media> findByBoardId(Long boardId) {
        return jpaQueryFactory.selectFrom(media).where(media.board.boardId.eq(boardId)).fetch();
    }

    @Override
    public long deleteByBoardId(Long boardId){
        return jpaQueryFactory.delete(media).where(media.board.boardId.eq(boardId)).execute();
    }
}
