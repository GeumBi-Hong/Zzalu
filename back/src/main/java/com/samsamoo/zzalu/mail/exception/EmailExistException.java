package com.samsamoo.zzalu.mail.exception;

import com.samsamoo.zzalu.advice.BadRequestException;

public class EmailExistException extends BadRequestException {
    private static final String MESSAGE = "이미 가입된 이메일입니다.";

    public EmailExistException() {
        super(MESSAGE);
    }
}
