package com.shakepoint.web.auth.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shakepoint.web.data.v1.dto.rest.response.AuthenticationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    public RestAuthenticationSuccessHandler() {

    }

    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth)
            throws IOException, ServletException {
        //get user i
        String name = auth.getName();
        String pass = (String) auth.getCredentials();
        AuthenticationResult result = new AuthenticationResult();
        result.setSuccess(true);
        result.setMessage("Welcome to ShakePoint :)");
        result.setAuthenticationToken(getAuthToken(name, pass));
        ObjectMapper mapper = new ObjectMapper();
        res.setContentType("application/json");
        res.getWriter().write(mapper.writeValueAsString(result));
    }

    private String getAuthToken(String email, String pass) {
        //get user info
        String string = email + ":" + pass;
        String encoded = new String(Base64.encode(string.getBytes()));

        return encoded;
    }

}
