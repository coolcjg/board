package com.cjg.traveling.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EncryptTest {

    @InjectMocks
    Encrypt encrypt;

    @Test
    @DisplayName("salt값 가져오기")
    public void getSalt(){
        String result  = encrypt.getSalt();
        Assertions.assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("암호화 하기")
    public void getEncrypt(){
        String pwd = "test";
        String salt = encrypt.getSalt();

        String result = encrypt.getEncrypt(pwd, salt);
        Assertions.assertThat(result).isNotEmpty();
    }

}
