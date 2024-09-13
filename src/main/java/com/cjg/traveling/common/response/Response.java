package com.cjg.traveling.common.response;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Response<T> {
    private final int code;
    private final String message;
    private final T data;

    public static <T> Response<T> success(T data){
        return new Response<>(Result.SUCCESS.getCode(), Result.SUCCESS.getMessage(), data);
    }

    public static <T> Response<T> success(){
        return new Response<>(Result.SUCCESS.getCode(), Result.SUCCESS.getMessage(), null);
    }

    public static <T> Response<T> fail(Result result, String message){
        return new Response<>(result.getCode(), message, null);
    }
}
