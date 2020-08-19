package com.biddingapp.dao;

import java.util.List;

import com.biddingapp.pojo.User;

public interface UserDAO {

	public void register(User user) throws Exception;

	public User getUserDetails(String email, String password) throws Exception;

	public void updateProfile(User user) throws Exception;

	public void deleteProfile(String email) throws Exception;

	public boolean changePassword(String email, String oldpassword, String newpassword) throws Exception;

	public String forgotPassword(String email) throws Exception;

	public List<User> getPendingUsers() throws Exception;

	public void approveProfile(String email, String role) throws Exception;
	
	public List<User> getAllUsers() throws Exception;

}
