package org.scribbler.api.controller;


import org.scribbler.api.model.*;
import org.scribbler.service.business.UserBusinessService;
import org.scribbler.service.entity.UserAuthEntity;
import org.scribbler.service.entity.UserEntity;
import org.scribbler.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",allowedHeaders = "*",exposedHeaders = {"access-token"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserBusinessService userBusinessService;

    @RequestMapping(method = RequestMethod.POST, path="/user/signup",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(final SignupUserRequest signupUserRequest) throws SignUpRestrictedException{

        final UserEntity userEntity=new UserEntity();
        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setUserName(signupUserRequest.getUserName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());

        final UserEntity createdUserEntity=userBusinessService.signup(userEntity);
        final SignupUserResponse userResponse=new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.POST, path= "/user/signin",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") String authorization) throws AuthenticationFailedException{

        byte[] decode= Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decoded=new String(decode);
        String userCredentials[]=decoded.split(":");
        final UserAuthEntity userAuthEntity=userBusinessService.authenticate(userCredentials[0],userCredentials[1]);
        final SigninResponse signinResponse=new SigninResponse().id(userAuthEntity.getUser().getUuid()).message("SIGNED IN SUCCESSFULLY");

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("access-token",userAuthEntity.getAccessToken());

        return new ResponseEntity<SigninResponse>(signinResponse,httpHeaders,HttpStatus.OK);
    }



    @RequestMapping(method = RequestMethod.POST,path="/user/signout",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") String authorization) throws SignOutRestrictedException{


        UserAuthEntity userAuthEntity=userBusinessService.signout(authorization);
        SignoutResponse signoutResponse=new SignoutResponse().id(userAuthEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");

        return new ResponseEntity<SignoutResponse>(signoutResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,path = "user/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserDetails(@PathVariable("userId")final String userId,@RequestHeader("authorization") String authorization) throws UserNotFoundException, AuthorizationFailedException {


        UserEntity userEntity=userBusinessService.getUser(userId,authorization);

        final UserDetailsResponse userDetailsResponse=new UserDetailsResponse();
        userDetailsResponse.setFirstName(userEntity.getFirstName());
        userDetailsResponse.setLastName(userEntity.getLastName());
        userDetailsResponse.setUserName(userEntity.getUserName());
        userDetailsResponse.setEmailAddress(userEntity.getEmail());
        userDetailsResponse.setCountry(userEntity.getCountry());
        userDetailsResponse.setContactNumber(userEntity.getContactNumber());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse,HttpStatus.OK);


    }


}
