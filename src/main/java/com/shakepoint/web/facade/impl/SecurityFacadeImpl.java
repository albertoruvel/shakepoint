package com.shakepoint.web.facade.impl;

import java.util.Date;

import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.dto.req.rest.SignupRequest;
import com.shakepoint.web.data.dto.res.rest.AuthenticationResult;
import com.shakepoint.web.data.entity.User;
import com.shakepoint.web.util.ShakeUtils;
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
			User user = getUser(request);
			userRepository.addUser(user); 
			ar.setAuthenticationToken(getAuthToken(request.getEmail(), request.getPassword()));
			ar.setMessage("User signed up successfully");
			ar.setSuccess(true);
		}
		return ar;
	}
	
	private String getAuthToken(String email, String pass){
		return new String(Base64.encode((email + ":" + pass).getBytes())); 
	}

	private User getUser(SignupRequest request){
		User user = new User(); 
		user.setActive(true);
		user.setAddedBy("");
		user.setConfirmed(false);
		user.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
		user.setEmail(request.getEmail());
		user.setName(request.getName());
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRole("member");
		return user; 
	}
}
