package com.samsamoo.zzalu.member.exception;

import com.samsamoo.zzalu.advice.ForbiddenException;

public class InvalidTokenException extends ForbiddenException {
    private static final String MESSAGE = "토큰이 유효하지 않습니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
