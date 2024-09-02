package com.cjg.traveling.repository;

import com.cjg.traveling.domain.Opinion;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cjg.traveling.domain.QOpinion.opinion;


@Repository
@AllArgsConstructor
public class OpinionCustomRepositoryImpl implements OpinionCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Opinion findByBoardIdAndUserId(Long boardId, String userId) {
        return jpaQueryFactory
                .selectFrom(opinion)
                .where(opinion.board.boardId.eq(boardId)
                        .and(opinion.user.userId.eq(userId)))
                .fetchOne();
    }

    @Override
    public Long deleteByBoardIdAndUserId(Long boardId, String userId) {
        return jpaQueryFactory
                .delete(opinion)
                .where(opinion.board.boardId.eq(boardId)
                        .and(opinion.user.userId.eq(userId)))
                .execute();
    }

    @Override
    public List<Opinion> findByBoardId(Long boardId) {
        return jpaQueryFactory
                .selectFrom(opinion)
                .where(opinion.board.boardId.eq(boardId))
                .fetch();
    }

    @Override
    public Long deleteByBoardId(Long boardId) {
        return jpaQueryFactory
                .delete(opinion)
                .where(opinion.board.boardId.eq(boardId)).execute();
    }

}
