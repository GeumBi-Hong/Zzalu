package com.samsamoo.zzalu.TitleHakwon.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ReplyCommentRequest {

    /** 사용자 ID **/
    private String username;
    /** 댓글 내용 **/
    private String content;

    /** 부모 댓글 ID (댓글 : 자기 자신 ID / 대댓글 : 댓글 ID ) **/
    private Long parentCommentId;
}
