package com.shakepoint.web.auth.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler{

	public RestAuthenticationFailureHandler(){
		
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException arg2)
			throws IOException, ServletException {
		//send a redirect 
		res.sendRedirect("/rest/security/auth_failure");
	}

}
