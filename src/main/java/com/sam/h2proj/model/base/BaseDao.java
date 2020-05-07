package com.sam.h2proj.model.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

import com.sam.h2proj.util.Page;
import com.sam.h2proj.util.PageInfo;

public class BaseDao<T,ID extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> entityClass;
	
	private Set<String> entityFieldNameSet = new HashSet<>();
	
	/**
	 * 繼承類別若有自定義的建構子, 必須呼叫父層的建構子以順利完成初始化工作 <br>
	 * ex: <br>
	 * ChildClass(){ <br>
	 * 
	 *   super(); <br>
	 *    
	 *   ... <br>
	 *   
	 *   其餘初始化指令 <br>
	 * }
	 * 
	 */
	public BaseDao() {
		
		// 取得Entity(T)的Class參照
		this.entityClass = null;
        Class c = getClass();
        Type t = c.getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] p = ((ParameterizedType) t).getActualTypeArguments();
            this.entityClass = (Class<T>) p[0];
            
            // 取得Entity(T)所有Field的Name, 供後續查詢比對, 過濾掉非法參數名稱
            for(Field field : this.entityClass.getDeclaredFields()) {
            	entityFieldNameSet.add(field.getName());
            }
        }
	}
	
	
	/**
	 * 取得Hibernate Current Session <br/>
	 * *設定為protected則繼承類別可使用此method,外部無法存取此method
	 * 
	 * @return Current Session
	 */
	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	/**
	 * 儲存或更新Entity
	 * @param entity
	 */
	public void save(T entity) {
		getSession().saveOrUpdate(entity);
	}
	
	/**
	 * 儲存或更新Entity List
	 * @param entities
	 */
	public void saveAll(Iterable<T> entities) {
		for(T entity : entities) {
			getSession().saveOrUpdate(entity);
		}
	}
	
	/**
	 * 使用Entity的主鍵查詢 (@Id)
	 * @param id 主鍵 (@Id)
	 * @return Entity或NULL
	 */
	public T findById(ID id) {
		return getSession().get(entityClass,id);
	}
	
	
	/**
	 * 取得所有的Entity
	 * @return Entity List 或 空的List
	 */
	public List<T> findAll() {
		CriteriaQuery<T> criteriaQuery = getSession().getCriteriaBuilder().createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		
		return getSession().createQuery(criteriaQuery).getResultList();
	}
	
	/**
	 * 取得所有的Entity
	 * @return 包含分頁資訊的Page物件, records寫入Entity List 或 空的List
	 */
	public Page<T> findAll(PageInfo pageInfo) {
		CriteriaQuery<T> criteriaQuery = getSession().getCriteriaBuilder().createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		
		Integer startPosition = pageInfo.getPageSize() * (pageInfo.getPageNum() - 1);
		
		List<T> resultList = getSession().createQuery(criteriaQuery)
				 .setFirstResult(startPosition)
				 .setMaxResults(pageInfo.getPageSize())
				 .getResultList(); 

		return new Page<T>(resultList, pageInfo.getPageNum(), pageInfo.getPageSize(), count());
	}
	
	/**
	 * 使用輸入的Id List取得相對應的Entity
	 * @param ids 該Entity主鍵值的List
	 * @return Entity List 或 空的List
	 */
	public List<T> findAllByIds(Iterable<ID> ids) {
		CriteriaQuery<T> criteriaQuery = getSession().getCriteriaBuilder().createQuery(entityClass);
		
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		criteriaQuery.where(root.in(ids));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	} 
	
	/**
	 * 使用輸入的Id List取得相對應的Entity
	 * @param ids 該Entity主鍵值的List
	 * @return 包含分頁資訊的Page物件, records寫入Entity List 或 空的List
	 */
	public Page<T> findAllByIds(Iterable<ID> ids,PageInfo pageInfo) {
		CriteriaQuery<T> criteriaQuery = getSession().getCriteriaBuilder().createQuery(entityClass);
		
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		criteriaQuery.where(root.in(ids));
		
		Integer startPosition = pageInfo.getPageSize() * (pageInfo.getPageNum() - 1);
		
		List<T> resultList = getSession().createQuery(criteriaQuery)
				 .setFirstResult(startPosition)
				 .setMaxResults(pageInfo.getPageSize())
				 .getResultList(); 

		return new Page<T>(resultList, pageInfo.getPageNum(), pageInfo.getPageSize(), count());
	} 
	
	/**
	 * 使用輸入的Map作為查詢參數依據 <br/>
	 * Map內<K,V>對應為<欄位名稱(對應Entity Class內的Field Name),查詢值> <br/>
	 * 查詢參數對應關係為等於 , SQL範例 : username = 'Sam' <br/>
	 * 各組查詢參數間使用AND串接 , SQL範例 : username = 'Sam' AND password = '123'
	 * @param paramMap 查詢參數Map
	 * @return Entity List 或 空的List
	 */
	public List<T> findAllByParams(Map<String,Object> paramMap) {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);

		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(String key : paramMap.keySet()) {
			if(entityFieldNameSet.contains(key)) {
				predicates.add(criteriaBuilder.equal(root.get(key), paramMap.get(key)));
			}
		} 
		
		criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
		
		return getSession().createQuery(criteriaQuery).getResultList();
	} 
	
	/**
	 * 使用輸入的Map作為查詢參數依據 <br/>
	 * Map內<K,V>對應為<欄位名稱(對應Entity Class內的Field Name),查詢值> <br/>
	 * 查詢參數對應關係為等於 , SQL範例 : username = 'Sam' <br/>
	 * 各組查詢參數間使用AND串接 , SQL範例 : username = 'Sam' AND password = '123'
	 * @param paramMap 查詢參數Map
	 * @return 包含分頁資訊的Page物件, records寫入Entity List 或 空的List
	 */
	public Page<T> findAllByParams(Map<String,Object> paramMap,PageInfo pageInfo) {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
		
		Root<T> root = criteriaQuery.from(entityClass);
		
		criteriaQuery.select(root);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(String key : paramMap.keySet()) {
			if(entityFieldNameSet.contains(key)) {
				predicates.add(criteriaBuilder.equal(root.get(key), paramMap.get(key)));
			}
		} 
		
		criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
		
		Integer startPosition = pageInfo.getPageSize() * (pageInfo.getPageNum() - 1);
		
		List<T> resultList = getSession().createQuery(criteriaQuery)
										 .setFirstResult(startPosition)
										 .setMaxResults(pageInfo.getPageSize())
										 .getResultList(); 
		
		return new Page<T>(resultList, pageInfo.getPageNum(), pageInfo.getPageSize(), count());
	} 
	
	/**
	 * 刪除給定的Entity (須與資料庫的該筆資料完全相符 , 包含Id)
	 * @param entity
	 */
	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	/**
	 * 使用給定的Entity Id刪除該筆對應資料
	 * @param id 該Entity主鍵值(@Id)
	 */
	public void deleteById(ID id) {
		T entity = findById(id);
		
		if(entity != null)
			delete(entity);
	}
	
	/**
	 * 刪除對應該Entity的Table的所有資料
	 */
	public void deleteAll() {
		List<T> entities = findAll();
		
		for(T entity : entities) {
			delete(entity);
		}
	}
	
	/**
	 * 刪除對應該Entity的Table的所有符合輸入Id的資料
	 * @param ids 該Entity主鍵值的List
	 */
	public void deleteAll(Iterable<ID> ids) {
		List<T> entities = findAllByIds(ids);
		
		for(T entity : entities) {
			delete(entity);
		}
	}
	
	/**
	 * 檢查此Entity Id的資料是否存在
	 * @param id 該Entity主鍵值(@Id)
	 * @return true(存在) / false(不存在)
	 */
	public boolean existsById(ID id) {
		T entity = findById(id);
		
		if(entity != null) {
			getSession().detach(entity);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 取得目前該Entity對應的Table的資料筆數
	 * @return 資料筆數
	 */
	public long count() {
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(criteriaBuilder.count(root));
		
		return getSession().createQuery(criteriaQuery).getSingleResult();
	}
	
	
}
