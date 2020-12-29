package org.scribbler.service.business;


import org.scribbler.service.dao.CommentDao;
import org.scribbler.service.dao.UserDao;
import org.scribbler.service.entity.CommentEntity;
import org.scribbler.service.entity.PostEntity;
import org.scribbler.service.entity.UserAuthEntity;
import org.scribbler.service.exception.AuthorizationFailedException;
import org.scribbler.service.exception.CommentNotFoundException;
import org.scribbler.service.exception.InvalidPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;

@Service
public class CommentBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;

    public PostEntity getPostByUuid(String Uuid) throws InvalidPostException{
        PostEntity postEntity=commentDao.getPostByUuid(Uuid);
        return  postEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity createComment(final CommentEntity commentEntity,final String authorization) throws AuthorizationFailedException{
        final UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }
        //if the user has logged out
        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }
        commentEntity.setDate(ZonedDateTime.now());
        commentEntity.setUser(userAuthEntity.getUser());
        final CommentEntity createdComment=commentDao.createComment(commentEntity);

        return createdComment;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity deleteComment(String commentId,String authorization) throws AuthorizationFailedException, CommentNotFoundException{
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }

        final CommentEntity commentEntity=commentDao.getCommentByUuid(commentId);
        if(commentEntity==null){

            throw new CommentNotFoundException("COM-001","Entered comment uuid does not exist");
        }
        if(!((userAuthEntity.getUser().getUuid().equals(commentEntity.getUser().getUuid()))||(userAuthEntity.getUser().getUuid().equals(commentEntity.getPost().getUser().getUuid())))){

            throw new AuthorizationFailedException("ATHR-003","Only the comment owner or the Post owner can delete the answer");
        }

        final CommentEntity deletedComment=commentDao.deleteComment(commentEntity);
        return deletedComment;

    }

    public TypedQuery<CommentEntity> getCommentsByPost(String postId,String authorization)throws AuthorizationFailedException, InvalidPostException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }

        final PostEntity postEntity=commentDao.getPostByUuid(postId);

        if(postEntity==null){
            throw new InvalidPostException("POST-001","The post with entered uuid whose details are to be seen does not exist");
        }
        final TypedQuery<CommentEntity> commentEntityTypedQuery=commentDao.getCommentsByPost(postEntity);
        return commentEntityTypedQuery;
    }
}
