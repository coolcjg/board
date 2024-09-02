package com.cjg.traveling.repository;


import com.cjg.traveling.domain.Board;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import ognl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.cjg.traveling.domain.QBoard.board;

@Repository
@AllArgsConstructor
public class BoardCustomRepositoryImpl  implements BoardCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Board> selectBoardPage(Pageable pageable) {
        List<Board> list =  jpaQueryFactory
                .selectFrom(board)
                .orderBy(board.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory
                .select(board.count())
                .from(board);

        return PageableExecutionUtils.getPage(list , pageable, count::fetchOne);
    }

    @Override
    public Page<Board> selectBoardPage(Pageable pageable, String searchType, String searchText) {
        JPAQuery<Board> query =  jpaQueryFactory
                .selectFrom(board);

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(board.count())
                .from(board);

        if(searchType.equals("all")){
            query.where(
                    board.title.containsIgnoreCase(searchText)
                            .or(board.region.eq(searchText))
                            .or(board.user.userId.eq(searchText))
                    );

            countQuery.where(
                    board.title.containsIgnoreCase(searchText)
                            .or(board.region.eq(searchText))
                            .or(board.user.userId.eq(searchText))
            );
        }else{
            if(searchType.equals("title")){
                query.where(
                        board.title.containsIgnoreCase(searchText)
                );
            }else if(searchType.equals("region")){
                query.where(board.region.eq(searchText));
            }else if(searchType.equals("userId")){
                query.where(board.user.userId.eq(searchText));
            }else if(searchType.equals("regDate")){

                String startDate = searchText.split("~")[0];
                String endDate = searchText.split("~")[1];

                LocalDateTime start = LocalDateTime.of(Integer.parseInt(startDate.split("-")[0]), Integer.parseInt(startDate.split("-")[1]), Integer.parseInt(startDate.split("-")[2]), 0, 0);
                LocalDateTime end = LocalDateTime.of(Integer.parseInt(endDate.split("-")[0]), Integer.parseInt(endDate.split("-")[1]), Integer.parseInt(endDate.split("-")[2]), 0, 0);
                end = end.plusDays(1);

                query.where(board.regDate.between(start, end));
            }
        }

        List<Board> list = query.orderBy(board.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return PageableExecutionUtils.getPage(list , pageable, countQuery::fetchOne);
    }


}
