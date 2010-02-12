package com.pyxis.petstore.domain.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.pyxis.petstore.domain.Item;

public class InMemoryDatabase {

	private HibernateTemplate hibernateTemplate;

	public InMemoryDatabase() {
		hibernateTemplate = new HibernateTemplate(sessionFactoryBean().getObject());
	}

	protected AnnotationSessionFactoryBean sessionFactoryBean() {
		AnnotationSessionFactoryBean sessionFactoryBean = new AnnotationSessionFactoryBean();
		sessionFactoryBean.setDataSource(datasource());
		sessionFactoryBean.setPackagesToScan(new String[] { Item.class.getPackage().getName() });
		sessionFactoryBean.setSchemaUpdate(true);
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		try {
			sessionFactoryBean.afterPropertiesSet();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sessionFactoryBean;
	}

	@SuppressWarnings("serial")
	protected Properties hibernateProperties() {
		return new Properties() {{
				put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
				put("hibernate.show_sql", "true");
			}};
	}

	protected DataSource datasource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:mem:petstore");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}

	public HibernateTemplate hibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void save(Object entity) {
		this.hibernateTemplate.save(entity);
	}

}
