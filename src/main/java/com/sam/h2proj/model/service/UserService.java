package com.sam.h2proj.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sam.h2proj.model.User;
import com.sam.h2proj.model.dao.UserDaoImpl;

@Service
@Transactional
public class UserService {

//	@Autowired
//	private UserDao userDao;
	
	@Autowired
	private UserDaoImpl userDao;
	
	
	public void addUser(User user) {
		userDao.save(user);
	}
	
	public User getUser(Integer id) {
		return userDao.findById(id);
	}
	
	public List<User> getAllUser(){
		return userDao.findAll();
	}
	
	public List<User> getAllUser(Map<String,Object> paramMap){
		return userDao.findAllByParams(paramMap);
	}
	
}
