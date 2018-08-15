package com.ewing.order.core.jpa;

import java.util.List;

import com.ewing.order.core.jpa.util.PageBean;
 

public interface BaseDao {
	public void delete(Object entity);

	public <T> void delete(Integer id, Class<T> entityClass);

	public <T> void delete(List<?> ids, Class<T> entityClass);

	public int executeSql(String sql);

	public <T> List<T> findAll(Class<T> entityClass);

	public <T> List<T> find(String condition, Class<T> entityClass);

	public <T> T findOne(Integer id, Class<T> entityClass);

	public <T> List<T> findByIds(List<?> ids, Class<T> clazz);

	public <T> T findOne(String condition, Class<T> entityClass);

	public <T> PageBean<T> pageQuery(String condition, String orderBy, Integer page, Integer pageSize,
			Class<T> entityClass);

	public void save(Object entity);

	public void update(Object entity);

	public <T> PageBean<T> noMappedObjectPageQuery(String sql, Class<T> beanClass, Integer page, Integer pageSize);

	public <T> List<T> noMappedObjectQuery(String sql, Class<T> beanClass);

	public Integer noMappedObjectCountQuery(String sql);

}