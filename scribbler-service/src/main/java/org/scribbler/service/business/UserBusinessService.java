package org.scribbler.service.business;


import org.scribbler.service.common.JwtTokenProvider;
import org.scribbler.service.dao.UserDao;
import org.scribbler.service.entity.UserAuthEntity;
import org.scribbler.service.entity.UserEntity;
import org.scribbler.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        String[] encryptedText =passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);

        if(userDao.getUserByEmail(userEntity.getEmail())!=null){
            throw new SignUpRestrictedException("SGR-002","This user has already been registered, try with any other emailId");
        }

        if(userDao.getUserByUsername(userEntity.getUserName())!=null){
            throw new SignUpRestrictedException("SGR-001","Try any other Username, this Username has already been taken");
        }


        return userDao.createUser(userEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(final String username,final String password) throws AuthenticationFailedException{
        UserEntity userEntity=userDao.getUserByUsername(username);

        if(userEntity==null){
            throw new AuthenticationFailedException("ATH-001","This username does not exist");
        }

        final String encryptedPassword=PasswordCryptographyProvider.encrypt(password,userEntity.getSalt());
        if(userEntity.getPassword().equals(encryptedPassword)){
            JwtTokenProvider jwtTokenProvider=new JwtTokenProvider(encryptedPassword);
            UserAuthEntity userAuthEntity=new UserAuthEntity();
            userAuthEntity.setUuid(UUID.randomUUID().toString());
            userAuthEntity.setUser(userEntity);
            ZonedDateTime now=ZonedDateTime.now();
            ZonedDateTime expiresAt=now.plusHours(8);
            userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(),now,expiresAt));
            userAuthEntity.setExpiresAt(expiresAt);
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setLogoutAt(null);

            final UserAuthEntity createdUserAuthEntity=userDao.createUserAuth(userAuthEntity);
            return createdUserAuthEntity;
        }else{
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(String authorization) throws SignOutRestrictedException {

        final UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }

        userAuthEntity.setLogoutAt(ZonedDateTime.now());
        //logs out the user
        final UserAuthEntity updatedUserAuth=userDao.updateUserAuth(userAuthEntity);
        return updatedUserAuth;
    }

    public UserEntity getUser(String uuid,String authorization) throws UserNotFoundException, AuthorizationFailedException{

        final UserAuthEntity userAuthEntity=userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get user details");
        }

        final UserEntity userEntity=userDao.getUserByUuid(uuid);
        // if the user with the paticular uuid does not exits in the database the exception is thrown
        if(userEntity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }

        return userEntity;
    }





}
