package com.shakepoint.web.facade.impl;

import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.v1.dto.rest.request.SignupRequest;
import com.shakepoint.web.data.v1.dto.rest.response.AuthenticationResult;
import com.shakepoint.web.data.v1.entity.ShakepointUser;
import com.shakepoint.web.util.TransformationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Base64;

import com.shakepoint.web.facade.SecurityFacade;

public class SecurityFacadeImpl implements SecurityFacade{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder; 
	
	@Override
	public AuthenticationResult signup(SignupRequest request) {
		AuthenticationResult ar = new AuthenticationResult();
		//check if  user has already been signed up with the same email 
		if(userRepository.userExists(request.getEmail())){
			ar.setAuthenticationToken("N/A");
			ar.setMessage("This email is already registered on shakepoint");
			ar.setSuccess(false);
			
		}else{
			//get the user 
			ShakepointUser user = TransformationUtils.getUser(request, encoder);
			userRepository.addShakepointUser(user);
			ar.setAuthenticationToken(getAuthToken(request.getEmail(), request.getPassword()));
			ar.setMessage("User signed up successfully");
			ar.setSuccess(true);
		}
		return ar;
	}
	
	private String getAuthToken(String email, String pass){
		return new String(Base64.encode((email + ":" + pass).getBytes())); 
	}


}
