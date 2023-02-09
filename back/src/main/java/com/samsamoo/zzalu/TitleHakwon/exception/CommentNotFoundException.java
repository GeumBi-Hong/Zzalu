package com.samsamoo.zzalu.TitleHakwon.exception;

import com.samsamoo.zzalu.advice.NotFoundException;

public class CommentNotFoundException  extends NotFoundException {
    private static final String MESSAGE = "해당 댓글이 존재하지 않습니다.";

    public CommentNotFoundException() {
        super(MESSAGE);
    }
    public CommentNotFoundException(String message) {
        super(message);
    }
}
