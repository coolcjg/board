package com.cjg.traveling.service;


import com.cjg.traveling.domain.Alarm;
import com.cjg.traveling.domain.Board;
import com.cjg.traveling.domain.User;
import com.cjg.traveling.repository.AlarmRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AlarmServiceTest {

    @Mock
    AlarmRepository alarmRepository;

    @InjectMocks
    AlarmService alarmService;

    @Test
    @DisplayName("알람 리스트 가져올 때 - 정상 accessToken일 경우")
    public void list(){

        //Given
        Map<String, Object> map = new HashMap();

        int pageNumber = 1;

        map.put("accessTokenUserId", "coolcjg");
        map.put("userId", "coolcjg");
        map.put("pageNumber", String.valueOf(pageNumber));

        List<Alarm> alarmList = new ArrayList<>();
        Alarm alarm = new Alarm();
        alarm.setAlarmId(1L);

        Board board = new Board();
        board.setBoardId(1L);
        alarm.setBoard(board);

        User fromUser = new User();
        fromUser.setUserId("coolcjg");
        alarm.setFromUser(fromUser);

        User toUser = new User();
        toUser.setUserId("coolcjg2");
        alarm.setToUser(toUser);

        alarm.setRegDate(LocalDateTime.now());
        alarm.setType("abc");
        alarm.setValue("message");
        alarm.setChecked("N");

        alarmList.add(alarm);

        Page<Alarm> page = new PageImpl<>(alarmList);

        given(alarmRepository.findAll(any(Specification.class), any(PageRequest.class))).willReturn(page);

        //When
        Map<String,Object> result = alarmService.list(map);

        //Then
        Assertions.assertThat((String)result.get("message")).isEqualTo("success");
    }

    @Test
    @DisplayName("알람 리스트 가져올 때 - 비정상 accessToken일 경우")
    public void list_2(){
        Map<String, Object> map = new HashMap();

        int pageNumber = 1;

        map.put("accessTokenUserId", "coolcjg");
        map.put("userId", "coolcjg1");
        map.put("pageNumber", String.valueOf(pageNumber));

        Map<String,Object> result = alarmService.list(map);

        Assertions.assertThat((String)result.get("message")).isEqualTo("authFail");
    }

    @Test
    @DisplayName("삭제 - 정상 삭제")
    public void delete(){

        //given
        Long alarmId = 1L;
        String userId = "coolcjg";

        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmId);

        User user = new User();
        user.setUserId("coolcjg");
        alarm.setToUser(user);

        given(alarmRepository.findByAlarmId(alarmId)).willReturn(alarm);
        given(alarmRepository.deleteByAlarmId(alarmId)).willReturn(1);

        //when
        Map<String, Object> result = alarmService.delete(alarmId, userId);

        //then
        Assertions.assertThat((String)result.get("message")).isEqualTo("success");
    }

    @Test
    @DisplayName("삭제 : 타겟 없을 때")
    public void delete_noTarget(){

        //given
        Long alarmId = 1L;
        String userId = "coolcjg";

        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmId);

        User user = new User();
        user.setUserId("coolcjg");
        alarm.setToUser(user);

        given(alarmRepository.findByAlarmId(alarmId)).willReturn(alarm);
        given(alarmRepository.deleteByAlarmId(alarmId)).willReturn(0);

        //when
        Map<String, Object> result = alarmService.delete(alarmId, userId);

        //then
        Assertions.assertThat((String)result.get("message")).isEqualTo("target empty");
    }

    @Test
    @DisplayName("삭제 : 권한 없을 때")
    public void delete_noAuth(){

        //given
        Long alarmId = 1L;
        String userId = "coolcjg1";

        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmId);

        User user = new User();
        user.setUserId("coolcjg2");
        alarm.setToUser(user);

        given(alarmRepository.findByAlarmId(alarmId)).willReturn(alarm);

        //when
        Map<String, Object> result = alarmService.delete(alarmId, userId);

        //then
        Assertions.assertThat((String)result.get("message")).isEqualTo("auth fail");
    }

    @Test
    @DisplayName("체크")
    public void check_success(){

        //given
        String userId = "coolcjg";
        Long alarmId = 1L;

        Alarm alarm = new Alarm();
        User user = new User();
        user.setUserId("coolcjg");
        alarm.setToUser(user);

        given(alarmRepository.findByAlarmId(alarmId)).willReturn(alarm);

        //when
        Map<String, Object> result = alarmService.check(userId, alarmId);

        //then
        Assertions.assertThat((String)result.get("message")).isEqualTo("success");
    }

    @Test
    @DisplayName("체크 : 권한 오류")
    public void check_authFail(){

        //given
        String userId = "coolcjg";
        Long alarmId = 1L;

        Alarm alarm = new Alarm();
        User user = new User();
        user.setUserId("coolcjg1");
        alarm.setToUser(user);

        given(alarmRepository.findByAlarmId(alarmId)).willReturn(alarm);

        //when
        Map<String, Object> result = alarmService.check(userId, alarmId);

        //then
        Assertions.assertThat((String)result.get("message")).isEqualTo("auth fail");
    }

    @Test
    @DisplayName("게시글 삭제에 따른 알람 삭제")
    public void deleteByBoard_boardId(){

        //given
        Long boardId = 1L;

        given(alarmRepository.deleteByBoard_boardId(boardId)).willReturn(1L);

        //when
        Long result = alarmService.deleteByBoard_boardId(boardId);

        //then
        Assertions.assertThat(result).isEqualTo(1L);
    }
}
