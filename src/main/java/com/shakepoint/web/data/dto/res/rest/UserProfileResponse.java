package com.shakepoint.web.data.dto.res.rest;

import javax.persistence.ColumnResult;
import javax.persistence.SqlResultSetMapping;
import java.util.List;

@SqlResultSetMapping(name = "UserProfileMapper", columns = {
		@ColumnResult(name = "userName"),
		@ColumnResult(name = "userId"),
		@ColumnResult(name = "userSince"),
		@ColumnResult(name = "age"),
		@ColumnResult(name = "birthday"),
		@ColumnResult(name = "weight"),
		@ColumnResult(name = "height"),
		@ColumnResult(name = "purchasesTotal"),
		@ColumnResult(name = "email")
})
public class UserProfileResponse {
	
	private String userName; 
	private String userId; 
	private String userSince; 
	private boolean availableProfile; 
	private int age; 
	private String birthday; 
	private double weight;
	private double height; 
	private double purchasesTotal; 
	private double cmi; 
	private String email; 
	
	
	private List<UserPurchaseResponse> purchases;
	public UserProfileResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isAvailableProfile() {
		return availableProfile;
	}
	public void setAvailableProfile(boolean availableProfile) {
		this.availableProfile = availableProfile;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public List<UserPurchaseResponse> getPurchases() {
		return purchases;
	}
	public void setPurchases(List<UserPurchaseResponse> purchases) {
		this.purchases = purchases;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserSince() {
		return userSince;
	}
	public void setUserSince(String userSince) {
		this.userSince = userSince;
	}
	public double getPurchasesTotal() {
		return purchasesTotal;
	}
	public void setPurchasesTotal(double purchasesTotal) {
		this.purchasesTotal = purchasesTotal;
	}	
	
	public double getCmi(){
		return this.weight / (Math.pow(this.height, 2)); 
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
