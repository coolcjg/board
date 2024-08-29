package com.cjg.traveling.repository;


import com.cjg.traveling.domain.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.cjg.traveling.domain.QBoard.board;

@Repository
@AllArgsConstructor
public class BoardCustomRepositoryImpl  implements BoardCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Board findTest(Long id) {

        return jpaQueryFactory.selectFrom(board)
                .where(board.boardId.eq(id))
                .fetchOne();
    }
}
