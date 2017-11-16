/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.auth;
import java.util.ArrayList;
import java.util.List;

import com.shakepoint.web.core.repository.UserRepository;
import com.shakepoint.web.data.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Alberto Rubalcaba
 */
public class AuthenticationService implements UserDetailsService{

    public UserRepository repository; 
    
    @Autowired
    private BCryptPasswordEncoder encoder; 

    @Value("${com.shakepoint.web.admin.user}")
    private String adminUsername;

    @Value("${com.shakepoint.web.admin.pass}")
    private String adminPass;


    public AuthenticationService(UserRepository repository){
        this.repository = repository; 
    }

    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        UserDetails details = null; 
        List<SimpleGrantedAuthority> coll = new ArrayList<SimpleGrantedAuthority>(); 
        UserInfo info = null;
        if(adminUsername.equals(string)){
        	info = new UserInfo(); 
        	info.setEmail(string);
        	info.setPassword(encoder.encode(adminPass));
        	info.setRole("super-admin");
        }else{
        	try{
                info = repository.getUserInfo(string);
            }catch(Exception ex){
                //not found

            }
        }
        if(info == null)
            throw new UsernameNotFoundException(string + " not found"); 
        SimpleGrantedAuthority userAuth = new SimpleGrantedAuthority("ROLE_MEMBER"); 
        SimpleGrantedAuthority techAuth = new SimpleGrantedAuthority("ROLE_TECHNICIAN"); 
        SimpleGrantedAuthority adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN"); 
        SimpleGrantedAuthority superAdminAuth = new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"); 
        
        if(info.getRole().equals("member"))
            coll.add(userAuth);
        else if(info.getRole().equals("admin"))
            coll.add(adminAuth); 
        else if(info.getRole().equals("super-admin"))
            coll.add(superAdminAuth); 
        else if(info.getRole().equals("technician"))
            coll.add(techAuth);
            
        details = new User(
                info.getEmail(), 
                info.getPassword(),
                true, true, true, true, coll); 
        return details; 
    }
    
}
