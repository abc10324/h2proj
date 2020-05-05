package com.sam.h2proj.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.sam.h2proj.model")
public class SpringConfig {
	
	/**
	 * 連線至嵌入式記憶體資料庫H2 (測試用)
	 * URL連線字串加入DB_CLOSE_DELAY=-1以避免H2DB預設行為(斷連線後自動清空DB)
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dmds = new DriverManagerDataSource();
		dmds.setDriverClassName("org.h2.Driver");
		dmds.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
		dmds.setUsername("sa");
		dmds.setPassword("");
		
		return dmds;
	}
	
	@Bean
	public SessionFactory sessionFactory() {
		return new LocalSessionFactoryBuilder(dataSource())
				.configure("hibernate.cfg.xml").buildSessionFactory();
	}
	
	@Bean
	public PlatformTransactionManager transactionManagement() {
		return new HibernateTransactionManager(sessionFactory());
		
	}
	
}
