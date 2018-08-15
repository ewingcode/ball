package com.ewing.order.core.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter(filterName = "AccessFilter", urlPatterns = { "/site/*" })
public class AccessFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger("accessLog");

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResposne, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResposne;
		String reqUrl = request.getRequestURL().toString();
		String method = request.getMethod();
		if (request.getQueryString() != null)
			reqUrl += request.getQueryString();
		logger.info("{} {} {} {} {}",
				new Object[] { request.getRemoteAddr(), method, reqUrl,
						response.getStatus(), response.getBufferSize() });
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		System.out.println(String.format("a?b?c?", "1", 2, 3));
	}
}
