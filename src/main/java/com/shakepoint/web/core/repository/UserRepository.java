/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.dto.res.ShakePointUser;
import com.shakepoint.web.data.dto.res.TechnicianDTO;
import com.shakepoint.web.data.dto.res.rest.UserProfile;
import com.shakepoint.web.data.entity.Profile;
import com.shakepoint.web.data.entity.User;
import com.shakepoint.web.data.security.UserInfo;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface UserRepository {
    public UserInfo getUserInfo(String email);
    public String addUser(User user);
    public List<TechnicianDTO> getTechnicians(int pageNumber);
    public String getUserId(String email);
    public TechnicianDTO getTechnician(String id);
    public List<ShakePointUser> getUsers(int pageNumber);
    public int getRegisteredTechnicians(); 
    public void updateLastSignin(String email); 
    public String getLastSignin(String id); 
    public boolean userExists(String email);
    public UserProfile getUserProfile(String userId);
    public boolean hasProfile(String userId); 
    public void updateProfile(Profile profile);
    public void saveProfile(Profile profile); 
    public String getUserEmail(String userId); 
}
