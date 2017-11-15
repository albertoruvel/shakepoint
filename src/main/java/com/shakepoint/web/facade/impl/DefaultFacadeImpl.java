/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.facade.impl;

import com.shakepoint.web.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.shakepoint.web.facade.DefaultFacade;

/**
 *
 * @author Alberto Rubalcaba
 */
public class DefaultFacadeImpl implements DefaultFacade{
	
	@Autowired
    private UserRepository userRepository;
    
}
