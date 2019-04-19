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
		 returnStr() ;
	
	}
	
	public static void returnStr()  {
		try { 
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName( "JavaScript" );
			System.out.println( engine.getClass().getName() );
			String function="function f(code) "
					+ "{ "
					+ "if(code.indexOf('#')>-1){ "
					+ "var str = code.substring(code.indexOf('#')+1); "
					+ "var effStr = str.substring(0,4);"
					+ " if( effStr=='FZ01'){"
				    + "	  return str;"
					+ "}else{ "
				    + "   return str.substring(4,8)" 
				    + "}"
					+ "}else{"
					+ "return 'error' "
					+ "}"
					+ "}; "
					+ "f('3344#FZ08890909');" ;
			System.out.println( "Result:" + engine.eval(function));
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
