/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shakepoint.web.core.repository;

import com.shakepoint.web.data.security.UserInfo;
import com.shakepoint.web.data.v1.entity.PartnerProductOrder;
import com.shakepoint.web.data.v1.entity.User;
import com.shakepoint.web.data.v1.entity.UserProfile;

import java.util.List;

/**
 *
 * @author Alberto Rubalcaba
 */
public interface UserRepository {
    public User getUserByEmail(String email);
    public UserInfo getUserInfo(String email);
    public List<User> getTechnicians();
    public String getUserId(String email);
    public User getTechnician(String id);
    public List<User> getUsers(int pageNumber);
    public int getRegisteredTechnicians(); 
    public void updateLastSignin(String email); 
    public String getLastSignin(String id); 
    public boolean userExists(String email);
    public UserProfile getUserProfile(String userId);
    public void saveProfile(UserProfile profile);

    //JTA
    public void addShakepointUser(User user);

    public void updateProfile(UserProfile existingProfile);

    public void saveUserOrder(PartnerProductOrder order);
}
