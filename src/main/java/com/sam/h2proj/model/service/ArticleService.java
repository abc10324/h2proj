package com.sam.h2proj.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sam.h2proj.model.Article;
import com.sam.h2proj.model.dao.ArticleDao;
import com.sam.h2proj.util.Page;
import com.sam.h2proj.util.PageInfo;

@Service
@Transactional
public class ArticleService {

	@Autowired
	private ArticleDao articleDao;
	
	public void addArticle(Article article) {
		articleDao.save(article);
	}
	
	public void updateArticle(Article article) {
		if(articleDao.existsById(article.getId())) {
			articleDao.save(article);
		}
	}
	
	public void deleteArticle(Integer id) {
		articleDao.deleteById(id);
	}
	
	public Article getArticle(Integer id) {
		return articleDao.findById(id);
	}
	
	public List<Article> getAllArticle() {
		return articleDao.findAll();
	}
	
	public List<Article> getArticleList(Map<String,Object> paramMap){
		return articleDao.findAllByParams(paramMap);
	}
	
	public Page<Article> getArticleListWithPages(Map<String,Object> paramMap){
		return articleDao.findAllByParams(paramMap,getPageInfo(paramMap));
	}
	
	private PageInfo getPageInfo(Map<String,Object> paramMap) {
		return new PageInfo(paramMap);
	}
	
	
}
