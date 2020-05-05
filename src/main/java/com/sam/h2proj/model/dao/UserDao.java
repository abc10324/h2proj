package com.sam.h2proj.model.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sam.h2proj.model.User;

@Repository
public class UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void save(User user) {
		getSession().save(user);
	}
	
	public User findById(Integer id) {
		return getSession().get(User.class, id);
	}
	
	public List<User> findAll(){
		CriteriaQuery<User> criteriaQuery = getSession().getCriteriaBuilder().createQuery(User.class);
		criteriaQuery.select(criteriaQuery.from(User.class));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	}
	
}
