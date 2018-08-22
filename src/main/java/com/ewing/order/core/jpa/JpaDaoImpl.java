package com.ewing.order.core.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.query.spi.sql.NativeSQLQuerySpecification;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ewing.order.core.jpa.util.BeanListProcessHandler;
import com.ewing.order.core.jpa.util.Count;
import com.ewing.order.core.jpa.util.PageBean;
import com.ewing.order.util.PageUtil;
import com.ewing.order.util.SqlUtil;

@Repository("baseDao")
public class JpaDaoImpl implements BaseDao {
	@PersistenceContext
	private EntityManager entityManager;
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory.getLogger(JpaDaoImpl.class);

	private void bulidEntityTime(Object entity, boolean isNew) {
		Object oldentity = null;
		try {
			Field id = entity.getClass().getDeclaredField("id");
			id.setAccessible(true);
			oldentity = findOne(Integer.valueOf(id.get(entity).toString()), entity.getClass());
		} catch (Exception e1) {
			// logger.warn("error find id in " + entity.getClass().toString());
		}
		try {
			Field createtime = entity.getClass().getDeclaredField("createTime");
			createtime.setAccessible(true);

			if (isNew) {
				if (createtime.get(entity) == null)
					createtime.set(entity,
							new java.sql.Timestamp((new java.util.Date()).getTime()));
			} else {
				if (oldentity != null) {
					createtime = oldentity.getClass().getDeclaredField("createTime");
					createtime.setAccessible(true);
					createtime.set(entity, createtime.get(oldentity));
				}
			}
		} catch (Exception e) {
			// logger.warn("error in bulid createtime in " +
			// entity.getClass().toString());
		}
		try {

			Field last_update = entity.getClass().getDeclaredField("lastUpdate");
			if (last_update != null) {
				last_update.setAccessible(true);
				last_update.set(entity, new java.sql.Timestamp((new java.util.Date()).getTime()));
			}
		} catch (Exception e) {
			// logger.warn("error bulid last_update in " +
			// entity.getClass().toString());
		}
	}

	@Override
	public void delete(Object entity) {
		if (null == entity) {
			return;
		}
		try {
			Method method = entity.getClass().getMethod("getId");
			Object primaryId = method.invoke(entity);
			if (primaryId == null)
				throw new DaoException("the primary key should not be null");
			Object object = findOne((Integer) primaryId, entity.getClass());
			entityManager.remove(object);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public int executeUpdate(String sql, QueryParameters queryParameters) {
		SessionImplementor session = null;
		try {
			session = getSession();
			return session.executeNativeUpdate(new NativeSQLQuerySpecification(sql, null, null),
					queryParameters);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new DaoException(e);
		}
	}

	@Override
	public int executeUpdate(String sql) {
		return executeUpdate(sql, new QueryParameters());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> find(String condition, Class<T> entityClass) {
		try {
			Query query = entityManager.createNativeQuery(generateQuerySql(condition, entityClass),
					entityClass);
			List<T> list = query.getResultList();
			return list;
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public <T> T findOne(Integer id, Class<T> entityClass) {
		List<T> list = find("id=" + id, entityClass);
		if (!list.isEmpty())
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T findOne(String condition, Class<T> entityClass) {
		try {
			Query query = entityManager.createNativeQuery(generateQuerySql(condition, entityClass),
					entityClass);
			List<T> list = query.getResultList();
			if (!list.isEmpty())
				return list.get(0);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 根本查询条件创建SQL
	 * 
	 * @param condition
	 * @param entityClass
	 * @return
	 */
	private <T> String generateQuerySql(String condition, Class<T> entityClass) {
		return generateQuerySql(condition, "", entityClass);
	}

	/**
	 * 根本查询条件创建SQL
	 * 
	 * @param condition
	 * @param entityClass
	 * @return
	 */
	private <T> String generateQuerySql(String condition, String orderBy, Class<T> entityClass) {
		Table tableAnnotation = entityClass.getAnnotation(Table.class);
		if (tableAnnotation == null)
			throw new DaoException("the Annotation of Table must be declared in entity class.");
		String tableName = tableAnnotation.name();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(tableName);
		orderBy = orderBy != null ? " " + orderBy : "";
		if (!StringUtils.isEmpty(condition)) {
			if (condition.trim().startsWith("select"))
				return condition + orderBy;
			if (condition.trim().startsWith("from"))
				return condition + orderBy;

			sql.append(" where 1=1 ");
			if (!(condition.trim().startsWith("and") || condition.trim().startsWith("AND"))) {
				sql.append(" and ");
			}
			sql.append(condition);
		}
		sql.append(orderBy);
		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> PageBean<T> pageQuery(final String condition, String orderBy, final Integer page,
			final Integer pageSize, Class<T> entityClass) {

		final Integer limit = PageUtil.getLimit(page, pageSize);
		final Integer start = PageUtil.getOffset(page, pageSize);

		Query rowCountQuery = entityManager
				.createNativeQuery(generateQuerySql(condition, entityClass), entityClass);
		int totalCount = rowCountQuery.getResultList().size();
		Query pageQuery = entityManager
				.createNativeQuery(generateQuerySql(condition, orderBy, entityClass), entityClass)
				.setMaxResults(limit).setFirstResult(start);
		PageBean<T> ps = new PageBean<T>(page, pageSize, totalCount, pageQuery.getResultList());
		return (PageBean<T>) ps;
	}

	@Override
	public void save(Object entity) {
		if (null == entity) {
			return;
		}
		bulidEntityTime(entity, true);
		entityManager.persist(entity);
	}

	@Override
	public void update(Object entity) {
		if (null == entity) {
			return;
		}
		bulidEntityTime(entity, false);
		entityManager.merge(entity);
	}

	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		return find(null, entityClass);
	}

	private SessionImplementor getSession() throws SQLException {
		return entityManager.unwrap(SessionImplementor.class);

		/*
		 * session.getJdbcConnectionAccess().releaseConnection(connection);
		 * return session.getJdbcConnectionAccess().obtainConnection();
		 */
	}

	public <T> PageBean<T> noMappedObjectPageQuery(String sql, Class<T> beanClass, Integer page,
			Integer pageSize) {
		SessionImplementor session = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			final Integer limit = PageUtil.getLimit(page, pageSize);
			final Integer start = PageUtil.getOffset(page, pageSize);
			String pageSql = sql + " limit " + start + "," + limit;
			session = getSession();
			connection = session.getJdbcConnectionAccess().obtainConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(pageSql);
			BeanListProcessHandler beanListProcessHandler = new BeanListProcessHandler();
			List<T> list = beanListProcessHandler.toBeanList(rs, beanClass);
			Integer count = noMappedObjectCountQuery(sql);
			return PageBean.newPageBean(page, pageSize, count, list);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			closeIO(session, connection, stmt, rs);
		}
	}

	public <T> List<T> noMappedObjectQuery(String sql, Class<T> beanClass) {
		SessionImplementor session = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			session = getSession();
			connection = session.getJdbcConnectionAccess().obtainConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			BeanListProcessHandler beanListProcessHandler = new BeanListProcessHandler();
			return beanListProcessHandler.toBeanList(rs, beanClass);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			closeIO(session, connection, stmt, rs);
		}

	}

	public Integer noMappedObjectCountQuery(String sql) {
		SessionImplementor session = null;
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String coutSql = SqlUtil.generateCountSql(sql);
			session = getSession();
			connection = session.getJdbcConnectionAccess().obtainConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(coutSql);
			BeanListProcessHandler beanListProcessHandler = new BeanListProcessHandler();
			List<Count> count = beanListProcessHandler.toBeanList(rs, Count.class);
			if (CollectionUtils.isEmpty(count) || count.get(0).getCount() == null)
				return 0;
			return count.get(0).getCount();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			closeIO(session, connection, stmt, rs);
		}

	}

	private void closeIO(SessionImplementor session, Connection connection, Statement stmt,
			ResultSet rs) {
		try {
			if (stmt != null)
				stmt.close();
			if (rs != null)
				rs.close();
			if (session != null && connection != null)
				session.getJdbcConnectionAccess().releaseConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> List<T> findByIds(List<?> ids, Class<T> clazz) {
		if (CollectionUtils.isEmpty(ids) || null == clazz) {
			return Collections.emptyList();
		}
		return find("id in (" + SqlUtil.array2InCondition(ids) + ")", clazz);
	}

	@Override
	public <T> void delete(Integer id, Class<T> entityClass) {
		T entity = findOne(id, entityClass);
		if (entity != null)
			delete(entity);
	}

	@Override
	public <T> void delete(List<?> ids, Class<T> entityClass) {
		List<T> entityList = findByIds(ids, entityClass);
		if (CollectionUtils.isNotEmpty(entityList)) {
			for (T entity : entityList)
				entityManager.remove(entity);
		}

	}
}
