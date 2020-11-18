package org.scribbler.api.controller;


import org.scribbler.api.model.CommentDeleteResponse;
import org.scribbler.api.model.CommentDetailsResponse;
import org.scribbler.api.model.CommentRequest;
import org.scribbler.api.model.CommentResponse;
import org.scribbler.service.business.CommentBusinessService;
import org.scribbler.service.entity.CommentEntity;
import org.scribbler.service.entity.PostEntity;
import org.scribbler.service.exception.AuthorizationFailedException;
import org.scribbler.service.exception.CommentNotFoundException;
import org.scribbler.service.exception.InvalidPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*",exposedHeaders = {"access-token"})
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentBusinessService commentBusinessService;

    @RequestMapping(method = RequestMethod.POST,path = "/comment/{postId}/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CommentResponse> createComment(CommentRequest commentRequest, @PathVariable("postId")String postId,@RequestHeader("authorization")String authorization)
            throws AuthorizationFailedException, InvalidPostException {

        CommentEntity commentEntity=new CommentEntity();
        commentEntity.setComment(commentRequest.getComment());
        commentEntity.setUuid(UUID.randomUUID().toString());
        PostEntity postEntity=commentBusinessService.getPostByUuid(postId);
        if(postEntity==null){
            throw new InvalidPostException("POST-001","The post entered is invalid");
        }
        commentEntity.setPost(postEntity);

        CommentEntity createdComment=commentBusinessService.createComment(commentEntity,authorization);
        CommentResponse commentResponse=new CommentResponse().id(createdComment.getUuid()).status("COMMENT CREATED");
        return new ResponseEntity<>(commentResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "/comment/delete/{commentId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CommentDeleteResponse> deleteComment(@PathVariable("commentId")String commentId,@RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, CommentNotFoundException {

        CommentEntity commentEntity=commentBusinessService.deleteComment(commentId,authorization);

        CommentDeleteResponse commentDeleteResponse=new CommentDeleteResponse().id(commentEntity.getUuid()).status("COMMENT DELETED");
        return new ResponseEntity<>(commentDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "comment/all/{postId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<CommentDetailsResponse>> getAllCommentsToPost(@PathVariable("postId")String postId,@RequestHeader("authorization")String authorization)
            throws AuthorizationFailedException,InvalidPostException{

        TypedQuery<CommentEntity> queriedComments=commentBusinessService.getCommentsByPost(postId,authorization);

        List<CommentEntity> commentList=queriedComments.getResultList();
        List<CommentDetailsResponse> commentDetailsResponses=new ArrayList<CommentDetailsResponse>();

        for(CommentEntity c:commentList){
            commentDetailsResponses.add(new CommentDetailsResponse().id(c.getUuid()).postContent(c.getPost().getTitle()).commentContent(c.getComment()).author(c.getUser().getUserName()));
        }
        return new ResponseEntity<List<CommentDetailsResponse>>(commentDetailsResponses,HttpStatus.OK);
    }


}
