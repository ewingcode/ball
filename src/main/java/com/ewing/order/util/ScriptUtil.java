package com.ewing.order.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author tansonlam
 * @create 2018年8月1日
 */
public class ScriptUtil {
	private static final Log logger = LogFactory.getLog(ScriptUtil.class);

	public static boolean eval(String str) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("js");
		try {
			return (Boolean) se.eval(str);
		} catch (ScriptException e) {
			e.printStackTrace();
			return false;
		}

	}
}
