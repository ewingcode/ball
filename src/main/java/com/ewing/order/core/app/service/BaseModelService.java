package com.ewing.order.core.app.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.ewing.order.core.jpa.BaseDao;
import com.ewing.order.core.jpa.DaoException;
import com.ewing.order.core.jpa.util.PageBean;

@Repository("baseModelService")
public class BaseModelService {
	private final static String MYSQL_FORMAT_FUNCTION = "date_format";
	private final static String MYSQL_DATE_PATTERN = "%Y-%m-%d";

	@Resource
	public BaseDao baseDao;

	@Transactional
	public void save(Object entity) throws DaoException {
		baseDao.save(entity);
	}

	@Transactional
	public void update(Object entity) throws DaoException {
		baseDao.update(entity);
	}

	@Transactional
	public void delete(Object entity) throws DaoException {
		baseDao.delete(entity);
	}

	@Transactional
	public <T> void delete(Integer id, Class<T> entityClass) throws DaoException {
		baseDao.delete(id, entityClass);
	}

	public <T> PageBean<T> pageQuery(String condition, String orderBy, Integer page,
			Integer pageSize, Class<T> entityClass) {
		return baseDao.pageQuery(condition, orderBy, page, pageSize, entityClass);
	}

	public <T> List<T> find(String queryString, Class<T> entityClass) throws DaoException {
		return baseDao.find(queryString, entityClass);
	}

	public <T> T findOne(Integer id, Class<T> entityClass) throws DaoException {
		return baseDao.findOne(id, entityClass);
	}

	public <T> T findOne(String condition, Class<T> entityClass) {
		return baseDao.findOne(condition, entityClass);
	}

	public String buildSQLWhere(Map<String, String> map) {
		String table = "";
		if (table.length() > 0) {
			table = table.concat(".");
		}
		StringBuffer sb = new StringBuffer(256);
		String[][] orstr = new String[10][];
		int[] orcnt = new int[10];
		for (int i = 0; i < 10; i++) {
			orcnt[i] = 0;
		}

		for (Iterator<String> itor = map.keySet().iterator(); itor.hasNext();) {

			String keystr = itor.next();
			if (keystr.length() < 3) {
				continue;
			}
			String keytype = null;
			String symbol = null;
			String orflag = null;
			String key = null;
			if (keystr.startsWith("_or") && keystr.length() > 9
			// && DIGITS.indexOf(keystr.substring(3, 4)) >= 0
					&& keystr.substring(4, 5).equals("_") && keystr.substring(6, 7).equals("_")) {
				key = keystr.substring(6);
				keytype = keystr.substring(6, 9);
				symbol = keystr.substring(8);
				orflag = keystr.substring(3, 4);
			} else {
				key = keystr;
				keytype = keystr.substring(0, 3);
				symbol = keystr.substring(2);
				orflag = null;
			}

			String value = null;
			if (keystr.equalsIgnoreCase("class")) {
				continue;
			}

			if (key.startsWith("_")) {
				if (key.length() > 6 && ("_eq_,_le_,_ge_,_lt_,_gt_,_ne_,_in_,_ni_")
						.indexOf(key.substring(2, 6)) >= 0) {
					value = (String) map.get(keystr);
					key = key.substring(6);
				} else if (key.length() > 9 && ("_exist_,_llike_,_rlike_,_nlike_,_likes_")
						.indexOf(key.substring(2, 9)) >= 0) {
					value = (String) map.get(keystr);
					key = key.substring(9);
				} else if (key.length() > 10 && ("_nexist_").indexOf(key.substring(2, 10)) >= 0) {
					value = (String) map.get(keystr);
					key = key.substring(10);
				} else if (key.length() > 8 && ("_like_").indexOf(key.substring(2, 8)) >= 0) {
					value = (String) map.get(keystr);
					key = key.substring(8);
				} else if (key.length() > 10 && ("_isnull_").indexOf(key.substring(2, 10)) >= 0) {
					value = (String) map.get(keystr);
					key = key.substring(10);
				}
			} else {
				continue;
			}

			if (value != null && value == "null" && symbol.startsWith("_isnull_")) {
				sb.append(" AND ").append(table).append(key);
				sb.append(" is null");
				continue;
			}

			if (value == null || (keytype.equals("_n_") && value == "null")) {
				continue;
			}
			if (keytype.equalsIgnoreCase("_s_") || keytype.equalsIgnoreCase("_n_")
					|| keytype.equalsIgnoreCase("_d_")) {
				if (orflag == null) {
					boolean flag = false;
					sb.append(" AND ").append(table).append(key);
					if (symbol.startsWith("_eq_")) {
						sb.append(" = ");
						flag = true;
					} else if (symbol.startsWith("_le_")) {
						sb.append(" <= ");
						flag = true;
					} else if (symbol.startsWith("_ge_")) {
						sb.append(" >= ");
						flag = true;
					} else if (symbol.startsWith("_lt_")) {
						sb.append(" < ");
						flag = true;
					} else if (symbol.startsWith("_gt_")) {
						sb.append(" > ");
						flag = true;
					} else if (symbol.startsWith("_ne_")) {
						sb.append(" <> ");
						flag = true;
					} else if (symbol.startsWith("_in_")) {
						sb.append(" in ( ");
					} else if (symbol.startsWith("_ni_")) {
						sb.append(" not in ( ");
					} else if (symbol.startsWith("_nexist_")) {
						sb.append(" not exist ( ");
					} else if (symbol.startsWith("_exist_")) {
						sb.append(" exist ( ");
					} else if (symbol.startsWith("_llike_")) {
						sb.append(" like '%");
					} else if (symbol.startsWith("_rlike_")) {
						sb.append(" like '");
					} else if (symbol.startsWith("_nlike_")) {
						sb.append(" not like '%");
					} else if (symbol.startsWith("_like_")) {
						sb.append(" like '%");
					} else if (symbol.startsWith("_likes_")) {
						sb.append(" like '");
					}

					if (flag == true && keytype.equalsIgnoreCase("_s_")) {
						sb.append("'");
					} else if (flag == true && keytype.equalsIgnoreCase("_d_")) {
						sb.append(MYSQL_FORMAT_FUNCTION).append("('");
					}
					sb.append(value);
					if (flag == true && keytype.equalsIgnoreCase("_s_")) {
						sb.append("'");
					} else if (flag == true && keytype.equalsIgnoreCase("_d_")) {
						sb.append("','" + MYSQL_DATE_PATTERN + "') ");
					}
					if (symbol.startsWith("_in_") || symbol.startsWith("_ni_")) {
						sb.append(")");
					} else if (symbol.startsWith("_nexist_") || symbol.startsWith("_exist_")) {
						sb.append(")");
					} else if (symbol.startsWith("_llike_")) {
						sb.append("'");
					} else if (symbol.startsWith("_rlike_") || symbol.startsWith("_nlike_")
							|| symbol.startsWith("_like_")) {
						sb.append("%'");
					} else if (symbol.startsWith("_likes_")) {
						sb.append("'");
					}
				} else {
					int k = Integer.parseInt(orflag);
					int len = orcnt[k];
					orcnt[k]++;
					boolean flag = false;
					if (orstr[k] == null) {
						orstr[k] = new String[100];
					}
					orstr[k][len] = table + key;
					if (symbol.startsWith("_eq_")) {
						orstr[k][len] += " = ";
						flag = true;
					} else if (symbol.startsWith("_le_")) {
						orstr[k][len] += " <= ";
						flag = true;
					} else if (symbol.startsWith("_ge_")) {
						orstr[k][len] += " >= ";
						flag = true;
					} else if (symbol.startsWith("_lt_")) {
						orstr[k][len] += " < ";
						flag = true;
					} else if (symbol.startsWith("_gt_")) {
						orstr[k][len] += " > ";
						flag = true;
					} else if (symbol.startsWith("_ne_")) {
						orstr[k][len] += " <> ";
						flag = true;
					} else if (symbol.startsWith("_in_")) {
						orstr[k][len] += " in ( ";
					} else if (symbol.startsWith("_ni_")) {
						orstr[k][len] += " not in ( ";
					} else if (symbol.startsWith("_nexist_")) {
						orstr[k][len] += " not exist ( ";
					} else if (symbol.startsWith("_exist_")) {
						orstr[k][len] += " exist ( ";
					} else if (symbol.startsWith("_llike_")) {
						orstr[k][len] += " like '%";
					} else if (symbol.startsWith("_rlike_")) {
						orstr[k][len] += " like '";
					} else if (symbol.startsWith("_nlike_")) {
						orstr[k][len] += " not like '%";
					} else if (symbol.startsWith("_like_")) {
						orstr[k][len] += " like '%";
					} else if (symbol.startsWith("_likes_")) {
						orstr[k][len] += " like '";
					}
					if (flag == true && keytype.equalsIgnoreCase("_s_")) {
						orstr[k][len] += "'";
					} else if (flag == true && keytype.equalsIgnoreCase("_d_")) {
						orstr[k][len] += "to_date('";
					}
					orstr[k][len] += value;
					if (flag == true && keytype.equalsIgnoreCase("_s_")) {
						orstr[k][len] += "'";
					} else if (flag == true && keytype.equalsIgnoreCase("_d_")) {
						orstr[k][len] += "','" + MYSQL_DATE_PATTERN + "') ";
					}
					if (symbol.startsWith("_in_") || symbol.startsWith("_ni_")) {
						orstr[k][len] += ")";
					} else if (symbol.startsWith("_nexist_") || symbol.startsWith("_exist_")) {
						orstr[k][len] += ")";
					} else if (symbol.startsWith("_llike_")) {
						orstr[k][len] += "'";
					} else if (symbol.startsWith("_rlike_") || symbol.startsWith("_nlike_")
							|| symbol.startsWith("_like_")) {
						orstr[k][len] += "%'";
					} else if (symbol.startsWith("_likes_")) {
						orstr[k][len] += "'";
					}
				}
			} else {
				continue;
			}
		}
		for (int i = 0; i < 10; i++) {
			if (orcnt[i] <= 0) {
				continue;
			}
			for (int j = 0; j < orcnt[i]; j++) {
				if (j == 0) {
					sb.append(" AND ( ");
				} else {
					sb.append(" OR ");
				}
				sb.append(orstr[i][j]).append(" ");
				if (j == orcnt[i] - 1) {
					sb.append(" ) ");
				}
			}
		}
		return sb.toString();
	}

	public <T> PageBean<T> noMappedObjectPageQuery(String sql, Class<T> beanClass, Integer page,
			Integer pageSize) {
		return baseDao.noMappedObjectPageQuery(sql, beanClass, page, pageSize);
	}

	public <T> List<T> noMappedObjectQuery(String sql, Class<T> beanClass) {
		return baseDao.noMappedObjectQuery(sql, beanClass);
	}
}
