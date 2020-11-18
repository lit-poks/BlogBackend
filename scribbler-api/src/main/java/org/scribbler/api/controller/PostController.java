package org.scribbler.api.controller;


import org.scribbler.api.model.*;
import org.scribbler.service.business.PostBusinessService;
import org.scribbler.service.entity.PostEntity;
import org.scribbler.service.exception.AuthorizationFailedException;
import org.scribbler.service.exception.InvalidPostException;
import org.scribbler.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*",exposedHeaders = {"access-token"})
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostBusinessService postBusinessService;

    @RequestMapping(method = RequestMethod.POST,path = "/post/create",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostResponse> createPost(PostRequest postRequest,@RequestHeader("authorization") String authorization) throws AuthorizationFailedException {

        final PostEntity postEntity=new PostEntity();
        postEntity.setUuid(UUID.randomUUID().toString());
        postEntity.setTitle(postRequest.getTitle());
        postEntity.setBody(postRequest.getBody());
        postEntity.setDate(ZonedDateTime.now());
        postEntity.setLikes("0");
        final PostEntity createdPostEntity=postBusinessService.createPost(postEntity,authorization);
        final PostResponse questionResponse=new PostResponse().id(createdPostEntity.getUuid()).status("POST CREATED");

        return new ResponseEntity<PostResponse>(questionResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET,path = "/post/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PostDetailsResponse>> getAllPosts(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException{

        final TypedQuery<PostEntity> allPosts=postBusinessService.getPosts(authorization);
        final List<PostEntity> listOfPosts=allPosts.getResultList();
        final List<PostDetailsResponse>  postDetailsResponseList=new ArrayList<PostDetailsResponse>();

        for(PostEntity e:listOfPosts){
            postDetailsResponseList.add(new PostDetailsResponse().id(e.getUuid()).title(e.getTitle()).body(e.getBody()).author(e.getUser().getUserName()).likes(e.getLikes()));
        }

        return new ResponseEntity<List<PostDetailsResponse>>(postDetailsResponseList,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT,path = "/post/edit/{postId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostEditResponse> editPostContent(PostEditRequest postEditRequest, @PathVariable("postId") String postId,@RequestHeader("authorization")String authorization)
            throws  AuthorizationFailedException, InvalidPostException{
        PostEntity postEntity=new PostEntity();
        postEntity.setTitle(postEditRequest.getTitle());
        postEntity.setBody(postEditRequest.getBody());
        postEntity.setLikes(postEditRequest.getLikes());
        final PostEntity editedPostEntity=postBusinessService.editPostContent(postEntity,postId,authorization);
        PostEditResponse postEditResponse=new PostEditResponse().id(editedPostEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<PostEditResponse>(postEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE,path = "post/delete/{postId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PostDeleteResponse> deletePost(@RequestHeader("authorization") String authorization,@PathVariable("postId") String postId)
            throws AuthorizationFailedException,InvalidPostException{

        PostEntity deletedPostEntity=postBusinessService.deletePost(postId,authorization);
        PostDeleteResponse postDeleteResponse=new PostDeleteResponse().id(deletedPostEntity.getUuid()).status("POST DELETED");
        return new ResponseEntity<PostDeleteResponse>(postDeleteResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "post/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PostDetailsResponse>> getAllPostsByUser(@PathVariable("userId") String userId,@RequestHeader("authorization") String authorization)
            throws AuthorizationFailedException, UserNotFoundException {
        TypedQuery<PostEntity> responseQuery=postBusinessService.getPostsByUser(userId,authorization);
        List<PostEntity> postEntityList=responseQuery.getResultList();
        List<PostDetailsResponse> responses=new ArrayList<PostDetailsResponse>();

        for(PostEntity e:postEntityList){
            responses.add(new PostDetailsResponse().id(e.getUuid()).title(e.getTitle()).body(e.getBody()).author(e.getUser().getUserName()).likes(e.getLikes()));
        }

        return new ResponseEntity<List<PostDetailsResponse>>(responses,HttpStatus.OK);
    }


}
