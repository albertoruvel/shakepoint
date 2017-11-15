/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.auth;

import com.shakepoint.web.util.ShakeUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Alberto Rubalcaba
 */
public class ShaPasswordEncoder implements PasswordEncoder{

    public String encode(CharSequence cs) {
        final String encodedPass = ShakeUtils.encodePassword(cs.toString());
        return encodedPass; 
    }

    public boolean matches(CharSequence cs, String string) {
        final String enc = ShakeUtils.encodePassword(cs.toString());
        boolean matches = enc.equals(string);
        return matches; 
    }
    
}
