package com.sam.h2proj.model.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDao<T,ID extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> entityClass;
	
	public BaseDao() {
		this.entityClass =null;
        Class c = getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<T>) p[0];
        }
	}
	
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public void save(T entity) {
		getSession().save(entity);
	}
	
	public void saveAll(Iterable<T> entities) {
		for(T entity : entities) {
			getSession().save(entity);
		}
	}
	
	public T findById(ID id) {
		return getSession().get(entityClass,id);
	}
	
	public List<T> findAll() {
		CriteriaQuery<T> criteriaQuery = getSession().getCriteriaBuilder().createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		
		return getSession().createQuery(criteriaQuery).getResultList();
	}
	
	public List<T> findAllByIds(Iterable<ID> ids) {
		CriteriaQuery<T> criteriaQuery = getSession().getCriteriaBuilder().createQuery(entityClass);
		
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		criteriaQuery.where(root.in(ids));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	} 
	
	public List<T> findAllByParams(Map<String,Object> paramMap) {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(String key : paramMap.keySet()) {
			predicates.add(criteriaBuilder.equal(root.get(key), paramMap.get(key)));
		} 
		
		criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	} 
	
	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	public void deleteById(ID id) {
		T entity = findById(id);
		
		if(entity != null)
			delete(entity);
	}
	
	public void deleteAll() {
		List<T> entities = findAll();
		
		for(T entity : entities) {
			delete(entity);
		}
	}
	
	public void deleteAll(Iterable<ID> ids) {
		List<T> entities = findAllByIds(ids);
		
		for(T entity : entities) {
			delete(entity);
		}
	}
	
	public boolean existsById(ID id) {
		return findById(id) != null;
	}
	
	public long count() {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return getSession().createQuery(criteriaQuery).getSingleResult();
	}
	
	
}
