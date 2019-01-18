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
	
	public static void main(String[] args) throws ScriptException {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("js");
		String packageCode = "wentu_86411698122-SDO100676198053"; 
		String packageCodeStr ="'"+packageCode+"'";
		String str2 ="packageCode.replace('wentu','wt')";
		str2 = str2.replace("packageCode", packageCodeStr);
		System.out.println(se.eval(str2));
		String str3 ="packageCode.substring(packageCode.indexOf('-')+1)";
		str3 = str3.replace("packageCode", packageCodeStr);
		System.out.println(se.eval(str3));
	}
}
