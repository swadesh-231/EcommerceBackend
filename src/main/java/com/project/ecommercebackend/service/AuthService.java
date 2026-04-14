package com.project.ecommercebackend.service;

import com.project.ecommercebackend.security.request.LoginRequest;
import com.project.ecommercebackend.security.request.SignupRequest;
import com.project.ecommercebackend.security.response.MessageResponse;
import com.project.ecommercebackend.security.response.UserInfoResponse;
import org.springframework.http.ResponseCookie;

public interface AuthService {

    UserInfoResponse authenticateUser(LoginRequest loginRequest);

    ResponseCookie generateJwtCookie(LoginRequest loginRequest);

    MessageResponse registerUser(SignupRequest signupRequest);

    ResponseCookie getCleanJwtCookie();

    UserInfoResponse getCurrentUser();
}
