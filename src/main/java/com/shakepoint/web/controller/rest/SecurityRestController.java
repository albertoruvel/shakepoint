package com.shakepoint.web.controller.rest;

import com.shakepoint.web.data.dto.req.rest.SignupRequest;
import com.shakepoint.web.data.dto.res.rest.AuthenticationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shakepoint.web.facade.SecurityFacade;

import org.springframework.security.crypto.codec.Base64;

@RequestMapping("/rest/security")
@RestController
public class SecurityRestController {
	
	@Autowired
	private SecurityFacade securityFacade;
	
	@RequestMapping(value="/auth_failure", method=RequestMethod.GET, 
			produces="application/json")
	public @ResponseBody ResponseEntity<AuthenticationResult> authFailure(){
		AuthenticationResult result = new AuthenticationResult();
		result.setSuccess(false);
		result.setMessage("Authentication failed :/");
		result.setAuthenticationToken("N/A");
		return new ResponseEntity<AuthenticationResult>(result, HttpStatus.OK); 
	}
	
	@RequestMapping(value="/auth_success", method=RequestMethod.GET, 
			produces="application/json")
	public @ResponseBody ResponseEntity<AuthenticationResult> authSuccess(@RequestParam(value="name", required=true)String name, 
											@RequestParam(value="pass", required=true)String pass){
		AuthenticationResult result = new AuthenticationResult();
		result.setSuccess(true);
		result.setMessage("Welcome to ShakePoint :)");
		result.setAuthenticationToken(getAuthToken(name, pass));
		return new ResponseEntity<AuthenticationResult>(result, HttpStatus.OK); 
	}
	
	private String getAuthToken(String email, String pass){
		//get user info 
		String string = email + ":" + pass; 
		String encoded = new String(Base64.encode(string.getBytes()));
		
		return encoded; 
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.POST, 
			produces="application/json", consumes="application/json")
	public @ResponseBody AuthenticationResult signup(@RequestBody SignupRequest request){
		return securityFacade.signup(request); 
	}
	
}
