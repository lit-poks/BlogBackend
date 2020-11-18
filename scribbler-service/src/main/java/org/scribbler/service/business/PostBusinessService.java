package org.scribbler.service.business;


import org.scribbler.service.dao.PostDao;
import org.scribbler.service.dao.UserDao;
import org.scribbler.service.entity.PostEntity;
import org.scribbler.service.entity.UserAuthEntity;
import org.scribbler.service.entity.UserEntity;
import org.scribbler.service.exception.AuthorizationFailedException;
import org.scribbler.service.exception.InvalidPostException;
import org.scribbler.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;

@Service
public class PostBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity createPost(final PostEntity postEntity,final String authorization) throws AuthorizationFailedException {

        final UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        postEntity.setUser(userAuthEntity.getUser());
        final PostEntity createdPostEntity=postDao.createPost(postEntity);
        return createdPostEntity;
    }

    public TypedQuery<PostEntity> getPosts(String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
        }

        final TypedQuery<PostEntity> receivedPosts=postDao.getPosts();
        return receivedPosts;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity editPostContent(PostEntity postEntity,String postId,String authorization) throws AuthorizationFailedException, InvalidPostException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
        }
        PostEntity oldPostEntity=postDao.getPostByUuid(postId);
        if(oldPostEntity==null){
            throw new InvalidPostException("POST-001","Entered post uuid does not exist");
        }


        oldPostEntity.setTitle(postEntity.getTitle());
        oldPostEntity.setBody(postEntity.getBody());
        oldPostEntity.setLikes(postEntity.getLikes());
        oldPostEntity.setDate(ZonedDateTime.now());
        PostEntity updatedPostEntity=postDao.editPost(oldPostEntity);
        return updatedPostEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity deletePost(String postId,String authorization) throws AuthorizationFailedException, InvalidPostException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");
        }

        PostEntity postEntity=postDao.getPostByUuid(postId);
        if(postEntity==null){
            throw new InvalidPostException("POST-001","Entered post uuid does not exist");
        }

        if((!userAuthEntity.getUser().getUuid().equals(postEntity.getUser().getUuid()))){
            throw new AuthorizationFailedException("ATHR-003","Only the post owner can delete the question");
        }

        PostEntity deletedPostEntity=postDao.deletePost(postEntity);
        return deletedPostEntity;
    }

    public TypedQuery<PostEntity> getPostsByUser(String userId,String authorization) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }

        UserEntity userEntity=userDao.getUserByUuid(userId);
        if(userEntity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose post details are to be seen does not exist");
        }

        TypedQuery<PostEntity> postSet=postDao.getPostsByUser(userEntity);
        return postSet;
    }


}
