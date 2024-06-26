package com.cjg.traveling.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class DateFormatTest {

    @Test
    @DisplayName("날짜 변환 테스트 : 같은 년도")
    public void convertDateFormat(){
        LocalDateTime localDateTime = LocalDateTime.now();
        String result = DateFormat.convertDateFormat(localDateTime);
        Assertions.assertThat(result).isEqualTo(localDateTime.getMonthValue() + "월 " + localDateTime.getDayOfMonth() + "일");
    }

    @Test
    @DisplayName("날짜 변환 테스트 : 다른 년도")
    public void convertDateFormat_2(){
        LocalDateTime localDateTime = LocalDateTime.of(2022, 1,2,10,22);
        String result = DateFormat.convertDateFormat(localDateTime);
        Assertions.assertThat(result).isEqualTo("2022년 1월 2일");
    }
}
