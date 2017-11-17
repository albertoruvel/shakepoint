/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.dto.res.rest.UserProfileResponse;

import com.shakepoint.web.data.security.UserInfo;
import com.shakepoint.web.data.v1.entity.ShakepointUser;
import com.shakepoint.web.data.v1.entity.ShakepointUserProfile;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface UserRepository {
    public UserInfo getUserInfo(String email);
    //public String addUser(User user);
    public List<ShakepointUser> getTechnicians();
    public String getUserId(String email);
    public ShakepointUser getTechnician(String id);
    public List<ShakepointUser> getUsers(int pageNumber);
    public int getRegisteredTechnicians(); 
    public void updateLastSignin(String email); 
    public String getLastSignin(String id); 
    public boolean userExists(String email);
    public UserProfileResponse getUserProfile(String userId);
    public boolean hasProfile(String userId);
    public void saveProfile(ShakepointUserProfile profile);


    //JTA
    public void addShakepointUser(ShakepointUser shakepointUser);
}
