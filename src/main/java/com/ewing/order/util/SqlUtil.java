package com.ewing.order.util;

import java.util.Collection;

/**
 * Sql工具类
 */
public class SqlUtil {
	/**
	 * 字符数组转成in查询条件
	 * 
	 * @param array
	 * @return
	 */
	public static <T> String array2InCondition(Collection<T> array) {
		StringBuffer sb = new StringBuffer();
		for (T str : array) {
			if (str instanceof String)
				sb.append("'").append(str).append("'").append(",");
			else
				sb.append(str).append(",");

		}
		return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();
	}

	/**
	 * 整形数组转成in查询条件
	 * 
	 * @param array
	 * @return
	 */
	public static String array2InCondition(Object[] array) {
		StringBuffer sb = new StringBuffer();
		for (Object str : array) {
			 if (str instanceof String)  
				sb.append("'").append(str).append("'").append(",");
			else 
				sb.append(str).append(",");

		}
		return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1).toString();
	}

	/**
	 * 生成查询总数的sql
	 * 
	 * @param sql
	 * @return
	 */
	public static String generateCountSql(String sql) {
		sql = sql.toLowerCase();
		return "select count(*) as count   " + sql.substring(sql.indexOf("from"));
	}
}
