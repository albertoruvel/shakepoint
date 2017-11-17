package com.shakepoint.web.auth.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.v1.dto.rest.response.UnauthorizedResponse;
import com.shakepoint.web.data.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BasicAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private UserRepository userRepository;

    private AuthenticationManager authManager;

    public BasicAuthenticationFilter() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc)
            throws IOException, ServletException {
        //get basic header
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String header = request.getHeader("Authorization");

        if (header == null || header.isEmpty()) {
            //set content type
            response.setContentType("application/json");
            UnauthorizedResponse r = new UnauthorizedResponse();
            r.setMessage("Unauthorized");
            r.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write(new ObjectMapper().writeValueAsString(r));
            ;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            //let the filter chain continue processing
            final String encoded = header.split(" ")[1];
            //decode
            final String decoded = new String(Base64.decode(encoded.getBytes()));
            //split by :
            try {
                String[] values = decoded.split(":");
                final String email = values[0];
                final String pass = values[1];
                //get user info
                UserInfo info = userRepository.getUserInfo(email);
                if (info != null) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, pass);
                    Authentication auth = authManager.authenticate(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    fc.doFilter(req, res);
                } else {
                    //error: null
                }
            } catch (Exception ex) {

            }
            //fc.doFilter(req, res);
        }
    }

    public void setAuthManager(AuthenticationManager mgr) {
        this.authManager = mgr;
    }

}
