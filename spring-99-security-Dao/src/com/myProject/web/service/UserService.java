package com.myProject.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myProject.web.dao.User;
import com.myProject.web.dao.UserDao;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public void create(User user){
		userDao.create(user);
	}

}
