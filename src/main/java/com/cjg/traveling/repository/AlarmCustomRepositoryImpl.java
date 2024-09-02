package com.cjg.traveling.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.cjg.traveling.domain.QAlarm.alarm;

@Repository
@AllArgsConstructor
public class AlarmCustomRepositoryImpl implements AlarmCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Long deleteByBoardId(Long boardId) {
        return jpaQueryFactory.delete(alarm).where(alarm.board.boardId.eq(boardId)).execute();
    }
}
