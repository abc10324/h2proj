package com.sam.h2proj.model.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		getSession().saveOrUpdate(user);
	}
	
	public void saveAll(Iterable<User> users) {
		for(User user : users) {
			getSession().saveOrUpdate(user);
		}
	}
	
	public User findById(Integer id) {
		return getSession().get(User.class, id);
	}
	
	public List<User> findAll(){
		CriteriaQuery<User> criteriaQuery = getSession().getCriteriaBuilder().createQuery(User.class);
		criteriaQuery.select(criteriaQuery.from(User.class));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	}
	
	/**
	 * 使用輸入的Id List取得相對應的User
	 * @param ids 該User主鍵值的List
	 * @return User List 或 空的List
	 */
	public List<User> findAllByIds(Iterable<Integer> ids) {
		CriteriaQuery<User> criteriaQuery = getSession().getCriteriaBuilder().createQuery(User.class);
		
		Root<User> root = criteriaQuery.from(User.class);
		
		criteriaQuery.select(root);
		criteriaQuery.where(root.in(ids));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	} 
	
	/**
	 * 使用輸入的Map作為查詢參數依據 <br/>
	 * Map內<K,V>對應為<欄位名稱(對應Entity Class內的Field Name),查詢值> <br/>
	 * 查詢參數對應關係為等於 , SQL範例 : username = 'Sam' <br/>
	 * 各組查詢參數間使用AND串接 , SQL範例 : username = 'Sam' AND password = '123'
	 * @param paramMap 查詢參數Map
	 * @return User List 或 空的List
	 */
	public List<User> findAllByParams(Map<String,Object> paramMap) {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		
		Root<User> root = criteriaQuery.from(User.class);
		
		criteriaQuery.select(root);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Set<String> entityFieldNameSet = new HashSet<>();
		
		for(Field field : (User.class).getDeclaredFields()) {
			entityFieldNameSet.add(field.getName());
		}
		
		for(String key : paramMap.keySet()) {
			if(entityFieldNameSet.contains(key)) {
				predicates.add(criteriaBuilder.equal(root.get(key), paramMap.get(key)));
			}
		} 
		
		criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	} 
	
	/**
	 * 刪除給定的User (須與資料庫的該筆資料完全相符 , 包含Id)
	 * @param user
	 */
	public void delete(User user) {
		getSession().delete(user);
	}
	
	/**
	 * 使用給定的User Id刪除該筆對應資料
	 * @param id 該User主鍵值(@Id)
	 */
	public void deleteById(Integer id) {
		User user = findById(id);
		
		if(user != null)
			delete(user);
	}
	
	/**
	 * 刪除對應該User的Table的所有資料
	 */
	public void deleteAll() {
		List<User> users = findAll();
		
		for(User user : users) {
			delete(user);
		}
	}
	
	/**
	 * 刪除對應該User的Table的所有符合輸入Id的資料
	 * @param ids 該Entity主鍵值的List
	 */
	public void deleteAll(Iterable<Integer> ids) {
		List<User> users = findAllByIds(ids);
		
		for(User user : users) {
			delete(user);
		}
	}
	
	/**
	 * 檢查此User Id的資料是否存在
	 * @param id 該User主鍵值(@Id)
	 * @return true(存在) / false(不存在)
	 */
	public boolean existsById(Integer id) {
		return findById(id) != null;
	}
	
	/**
	 * 取得目前該User對應的Table的資料筆數
	 * @return 資料筆數
	 */
	public long count() {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<User> root = criteriaQuery.from(User.class);
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return getSession().createQuery(criteriaQuery).getSingleResult();
	}
}
