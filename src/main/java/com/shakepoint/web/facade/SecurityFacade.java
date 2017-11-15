package com.shakepoint.web.facade;

import com.shakepoint.web.data.dto.req.rest.SignupRequest;
import com.shakepoint.web.data.dto.res.rest.AuthenticationResult;

public interface SecurityFacade {
	public AuthenticationResult signup(SignupRequest request);
}
