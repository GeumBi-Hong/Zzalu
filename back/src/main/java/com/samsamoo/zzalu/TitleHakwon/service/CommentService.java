package com.samsamoo.zzalu.TitleHakwon.service;


import com.samsamoo.zzalu.TitleHakwon.dto.*;
import com.samsamoo.zzalu.TitleHakwon.entity.Comment;
import com.samsamoo.zzalu.TitleHakwon.entity.CommentLike;
import com.samsamoo.zzalu.TitleHakwon.entity.ReplyComment;
import com.samsamoo.zzalu.TitleHakwon.repository.CommentLikeRepository;
import com.samsamoo.zzalu.TitleHakwon.repository.CommentRepository;
import com.samsamoo.zzalu.TitleHakwon.repository.ReplyCommentRepository;
import com.samsamoo.zzalu.TitleHakwon.repository.TitleHackwonRepository;
import com.samsamoo.zzalu.member.entity.Member;
import com.samsamoo.zzalu.member.repo.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor  //얘가 자동으로 생성자 주입해줌
@Repository
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReplyCommentRepository replyCommentRepository;
    private final MemberRepository memberRepository;
    private final TitleHackwonRepository titleHackwonRepository;

    private final CommentLikeRepository commentLikeRepository;

    /**
     * 댓글 작성
     */
    public CommentResponse addComment (CommentRequest requestComment){

        Comment comment = Comment.builder()
                .member(memberRepository.findByUsername(requestComment.getUsername()).get())
                .titleHakwon(titleHackwonRepository.findTitleHakwonById(requestComment.getTitleHakwonId()))
                .content(requestComment.getContent())
                .replyCommentList(new ArrayList<>())
                .build();


        commentRepository.save(comment);

        return new CommentResponse(comment);


    }

    /**
     * 대댓글 작성
     */
    public ReplyCommentResponse addReplyComment (ReplyCommentRequest replyCommentRequest){

        /**
         * 예외 처리 1 : 부모 댓글이 존재하지 않을경우  ( )
         */

        ReplyComment replyComment = ReplyComment.builder()
                .member(memberRepository.findByUsername(replyCommentRequest.getUsername()).get())
                .content(replyCommentRequest.getContent())
                .parentComment(commentRepository.findById(replyCommentRequest.getParentCommentId()).get())
                .build();



      replyCommentRepository.save(replyComment);

        return new ReplyCommentResponse(replyComment);


    }

    /**
     * 댓글 가져오기
     * 무한 스크롤 / 커서 기반 페이지 네이션
     */

    public List<CommentResponse> getRecentCommentList (SearchCommentRequest searchCommentRequest){

        Page<Comment> comments = fetchCommentPages(searchCommentRequest.getLastCommentId(),searchCommentRequest.getTitleHakwonId(), searchCommentRequest.getSize());

        return getCommentList(comments.getContent(),searchCommentRequest.getUsername());
        }



    public List<CommentResponse> getCommentList (List<Comment>commentList ,String username){

        if(username==null){
            return CommentResponse.convertCommentToDtoList(commentList);
        }else{
            //사용자가 로그인이 되어있는 경우 좋아요를 눌렀던 기록을 불러온다.

            List<CommentResponse> commentResponseList = new ArrayList<>();

            for(Comment comment : commentList){
                CommentResponse  commentResponse = new CommentResponse(comment);

                //좋아요 누른 기록이 존재한다면
                if(existCommentLike(comment.getId(),username)){
                    //좋아요 누른 기록으로 보낸다.
                    commentResponse.updateIsPressed();
                }

                commentResponseList.add(commentResponse);
            }

            return commentResponseList;
        }
    }

    private Page<Comment> fetchCommentPages(Long lastCommentId, Long titleHakwonId ,int size) {
        PageRequest pageRequest = PageRequest.of(0, size); // 페이지네이션을 위한 PageRequest, 페이지는 0으로 고정한다.
        return commentRepository.findByIdLessThanAndTitleHakwonIdOrderByIdDesc(lastCommentId,titleHakwonId , pageRequest); // JPA 쿼리 메소드
    }

    /**
     * 대댓글 가져오기
     * 커서 기반 페이지 네이션
     */

    public List<ReplyCommentResponse> getReplyCommentList (SearchReplyCommentRequest sr){

        Page<ReplyComment> replyComments = fetchReplyCommentPages(sr.getLastCommentId(),sr.getParentCommentId(),sr.getSize());

        return ReplyCommentResponse.convertReplyCommentToDtoList(replyComments.getContent());
    }


    public Page<ReplyComment> fetchReplyCommentPages (Long lastReplyCommentId,Long parentCommentId, int size){
        PageRequest pageRequest = PageRequest.of(0, size);
        return  replyCommentRepository.findByIdLessThanAndParentCommentIdOrderByIdDesc(lastReplyCommentId,parentCommentId,pageRequest);
    }

    /**
     * [UPDATE]
     * 댓글 수정
     * 이 댓글을 작성한 사용자인지 아닌지 판단하게끔 백에서 해줘야하나?
     */
    public void updateComment (UpdateCommentRequest commentRequest){

       Optional<Comment> comment = commentRepository.findById(commentRequest.getCommentId());

        //수정하고자 하는 댓글이 존재할때만 수정한다.
        if(comment!=null){
            if(!StringUtils.isEmpty(commentRequest.getContent())) {
                comment.get().upDateContent(commentRequest.getContent(), true);
            }
            commentRepository.save(comment.get());
        }

    }


    /**
     * [UPDATE]
     * 대댓글 수정
     */

  public void updateReplyComment (UpdateCommentRequest ur){

      ReplyComment replyComment = replyCommentRepository.findById(ur.getCommentId());

      if(replyComment!=null){
          if(!StringUtils.isEmpty(ur.getContent())){
              replyComment.upDateContent(ur.getContent(),true);
          }
          replyCommentRepository.save(replyComment);
      }
  }

    /**
     * 댓글 삭제
     */
    @Transactional
    public int deleteComment (Long id){
         commentRepository.deleteById(id);
        return 1;

    }


    /**
     * 대댓글 삭제
     */
    @Transactional
    public void  deleteReplyCommnete(Long id){
        replyCommentRepository.deleteById(id);

    }


    /**
     * 댓글에 좋아요 누르기
     *
     * 1. 댓글 좋아요 기록에 추가
     * 2. 댓글 좋아요 +1
     */
    public void clickCommentLikes(Long commentId , String memberId){

        //존재하지 않은 댓글이였다면?
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Member> member = memberRepository.findByUsername(memberId);

        if(!comment.isPresent()){
            return;
        }
        if(!member.isPresent()){
                return;
        }
        //존재하지 않는 멤버였다면?

        CommentLike commentLike = CommentLike.builder()
                .member(member.get())
                .comment(comment.get()).build();
        //좋아요을 눌렀으면 좋아요기록 테이블에 저장하고

        commentLikeRepository.save(commentLike);

        //해당 댓글의 좋아요 +1을 증가시킨다.
        comment.get().plusLikeNum();
        commentRepository.save(comment.get());

    }

    /**
     * 댓글에 좋아요 취소하기
     * 1.댓글 좋아요 기록에서 삭제
     * 2. 댓글 좋아요 -1
     */
    @Transactional

    public void cancelCommentLikes(Long commentId , String memberId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Member> member = memberRepository.findByUsername(memberId);

        if(!comment.isPresent()){
            return;
        }
        if(!member.isPresent()){
            return;
        }

        commentLikeRepository.deleteByComment_IdAndMemberUsername(commentId,memberId);

        comment.get().minusLikeNum();
        commentRepository.save(comment.get());

    }


    /**
     * 댓글 좋아요 기록이 존재하는지
     */

    public  boolean existCommentLike(Long commentId ,String memberId ){

        return commentLikeRepository.existsByComment_IdAndMemberUsername(commentId,memberId);
    }

    /**
     * 상위 50개 댓글 가져오기
     */

    public List<CommentResponse> getBest50CommentList (Long titleHakwonId ,String username){

        List<Comment> commentList = commentRepository.findTop50ByTitleHakwonIdAndLikeNumGreaterThanOrderByLikeNumDesc(titleHakwonId,0);

        return getCommentList(commentList,username);

    }

}
