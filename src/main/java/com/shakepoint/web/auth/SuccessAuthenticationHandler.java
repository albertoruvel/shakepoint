/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.auth;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.security.SecurityRole;
import com.shakepoint.web.data.v1.entity.User;
import com.shakepoint.web.util.ShakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author Alberto Rubalcaba
 */
public class SuccessAuthenticationHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    public SuccessAuthenticationHandler() {

    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        HttpSession session = request.getSession();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String redirect = "";
        if (user != null) {
            session.setAttribute("username", user.getUsername());
            if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    || user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) {
                redirect = "admin/";
                //check if super admin exists in database
                String id = userRepository.getUserId(user.getUsername());
                if (id == null) {
                    //create it, may be first time use
                    userRepository.addShakepointUser(createAdminUser(user));
                }
            } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TECHNICIAN"))) {
                redirect = "partner/";
            }
            //update last signin column
            userRepository.updateLastSignin(user.getUsername());
        }
        if (redirect.isEmpty())
            redirect = "signin";

        response.sendRedirect(redirect);
    }

    private User createAdminUser(org.springframework.security.core.userdetails.User user) {
        User newUser = new User();
        newUser.setActive(true);
        newUser.setAddedBy(null);
        newUser.setConfirmed(true);
        newUser.setCreationDate(ShakeUtils.DATE_FORMAT.format(new Date()));
        newUser.setEmail(user.getUsername());
        newUser.setLastSignin(ShakeUtils.DATE_FORMAT.format(new Date()));
        newUser.setName("Your majesty");
        newUser.setPassword(user.getPassword());
        newUser.setRole(SecurityRole.SUPER_ADMIN.toString());
        return newUser;
    }

}
