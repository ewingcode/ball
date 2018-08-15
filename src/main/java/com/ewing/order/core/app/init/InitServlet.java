package com.ewing.order.core.app.init;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewing.order.core.app.bean.SysParamFactory;

@WebServlet(loadOnStartup = 1, urlPatterns = "/sys/InitServlet")
public class InitServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	/**
	 * Logger for this class
	 */
	protected static Logger logger = LoggerFactory.getLogger(InitServlet.class);

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void destroy() {
		super.destroy();
	}

	public void init() throws ServletException {
		try {
			SysParamFactory.WEB_CONTEXT_PATH = super.getServletContext()
					.getContextPath(); 
			logger.info("init successfully");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}