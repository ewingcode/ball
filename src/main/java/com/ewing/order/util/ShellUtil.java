package com.ewing.order.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ShellUtil {
	private static final Log logger = LogFactory.getLog(ShellUtil.class);
 
	public static void main(String[] args) {

	}

	public static boolean ExeShell(String shellPath) {
		String cmd = "sh " + shellPath;
		System.out.println(cmd);
		Runtime run = Runtime.getRuntime();
		String result = "";
		BufferedReader br = null;
		BufferedInputStream in = null;
		try {
			Process p = run.exec(cmd);
			if (p.waitFor() != 0) {
				result += "没有进程号";
				return false;
			}
			in = new BufferedInputStream(p.getInputStream());
			br = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = br.readLine()) != null) {
				result += lineStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			logger.info("ShellUtil.ExeShell=>" + result);
		}
		return true;
	}
}
