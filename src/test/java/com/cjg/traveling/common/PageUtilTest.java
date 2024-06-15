package com.cjg.traveling.common;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PageUtilTest {

    @Test
    @DisplayName("getStartEndPage")
    public void getStartEndPage(){

        List<Integer> result = PageUtil.getStartEndPage(1, 20);

        List<Integer> result2 = PageUtil.getStartEndPage(11, 20);
        List<Integer> result3 = PageUtil.getStartEndPage(10, 20);
        List<Integer> result4 = PageUtil.getStartEndPage(1, 5);

        Assertions.assertThat(result.get(0)).isEqualTo(1);
        Assertions.assertThat(result.get(9)).isEqualTo(10);

        Assertions.assertThat(result2.get(0)).isEqualTo(11);
        Assertions.assertThat(result2.get(9)).isEqualTo(20);

        Assertions.assertThat(result3.get(0)).isEqualTo(1);
        Assertions.assertThat(result3.get(9)).isEqualTo(10);

        Assertions.assertThat(result4.get(0)).isEqualTo(1);
        Assertions.assertThat(result4.get(4)).isEqualTo(5);
    }
}
