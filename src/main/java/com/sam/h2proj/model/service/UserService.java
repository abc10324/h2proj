package com.sam.h2proj.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sam.h2proj.model.User;
import com.sam.h2proj.model.dao.UserDaoImpl;
import com.sam.h2proj.util.Page;
import com.sam.h2proj.util.PageInfo;

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
	
	public void updateUser(User user) {
		if(userDao.existsById(user.getId())) {
			userDao.save(user);
		}
	}
	
	public void deleteUser(Integer id) {
		userDao.deleteById(id);
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
	
	public Page<User> getAllUserWithPages(Map<String,Object> paramMap){
		return userDao.findAllByParams(paramMap,getPageInfo(paramMap));
	}
	
	private PageInfo getPageInfo(Map<String,Object> paramMap) {
		return new PageInfo(paramMap);
	}
	
}
