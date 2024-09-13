package com.cjg.traveling.common.response;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Result {
    SUCCESS(0, "success"),
    FAIL(-1, "fail"),
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized");

    private final int code;
    private final String message;



}
