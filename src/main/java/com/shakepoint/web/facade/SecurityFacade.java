package com.shakepoint.web.facade;

import com.shakepoint.web.data.v1.dto.rest.request.SignupRequest;
import com.shakepoint.web.data.v1.dto.rest.response.AuthenticationResult;

public interface SecurityFacade {
	public AuthenticationResult signup(SignupRequest request);
}
