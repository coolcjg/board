package com.cjg.traveling.service;


import com.cjg.traveling.domain.Opinion;
import com.cjg.traveling.repository.OpinionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OpinionServiceTest {

    @Mock
    private OpinionRepository opinionRepository;

    @InjectMocks
    private OpinionService opinionService;

    @Test
    @DisplayName("의견 가져오기")
    public void findByBoard_boardId(){

        Long boardId = 1L;

        List<Opinion> list = new ArrayList();
        Opinion opinion = new Opinion();
        opinion.setOpinionId(1L);
        list.add(opinion);

        given(opinionRepository.findByBoardId(boardId)).willReturn(list);

        List<Opinion> result = opinionService.findByBoard_boardId(boardId);

        Assertions.assertThat(result).isNotNull();

    }

    @Test
    @DisplayName("의견 삭제")
    public void deleteByBoard_boardId(){

        Long boardId = 1L;

        given(opinionRepository.deleteByBoardId(boardId)).willReturn(1L);

        Long result = opinionService.deleteByBoard_boardId(boardId);

        Assertions.assertThat(result).isEqualTo(result);
    }

}
