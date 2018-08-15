package com.ewing.order.ball.event;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptTest {
	public static void main(String[] args) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine se = manager.getEngineByName("js");
		String str = "13.2<13";
		boolean result;
		try {
			result = (Boolean) se.eval(str);
			System.out.println(result);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}
}
