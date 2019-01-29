package com.ewing.order.core.web.base;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

/**
 * 
 * @author tanson lam
 * @create:2012-2-23
 */

public class BaseController {
	protected static Logger logger = LoggerFactory.getLogger(BaseController.class);
	protected static final long serialVersionUID = 1L;
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;
	public String condition;

	protected final static Gson gson = new Gson();

	public BaseController() {

	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
		this.response.setContentType("text/html;charset=UTF-8");
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Integer getIntegerParameter(String key) {
		String value = request.getParameter(key);
		if (StringUtils.isEmpty(value) || (value != null && value.equals("undefined")))
			return null;
		return Integer.valueOf(value);
	}

	public ModelAndView renderWithHead(String template, Map<String, Object> dataModel) {
		String hl = request.getParameter("hl");
		if (hl != null && !Boolean.valueOf(hl))
			dataModel.put("hl", false);
		else
			dataModel.put("hl", true);
		return render(template, dataModel);
	}

	public Integer getPage() {
		String pageStr = request.getParameter("page");
		return StringUtils.isEmpty(pageStr) ? null : Integer.valueOf(pageStr);
	}

	public Integer getPageSize() {
		String pageSizeStr = request.getParameter("pageSize");
		return StringUtils.isEmpty(pageSizeStr) ? null : Integer.valueOf(pageSizeStr);
	}

	private String getContextPath(HttpServletRequest request) {
		String contextPath;
		String port = request.getServerPort() == 80 || request.getServerPort() == 0 ? ""
				: ":" + request.getServerPort();
		contextPath = request.getScheme() + "://" + request.getServerName() + port
				+ request.getContextPath();
		return contextPath;
	}

	public <T> ModelAndView render(String template) {
		return render(template, null);
	}

	/**
	 * 使用模板渲染
	 * 
	 * @param template
	 *            模板名稱
	 * @param dataModel
	 *            模板參數
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public <T> ModelAndView render(String template, Map<String, T> dataModel) {
		if (dataModel == null)
			dataModel = Maps.newHashMap();

		dataModel.put("contextPath", (T) getContextPath(request));
		// 模板中传入session值
		HttpSession session = request.getSession();
		String[] sessionName = session.getValueNames();
		for (String name : sessionName) {
			dataModel.put(name, (T) request.getSession().getAttribute(name));
		}
		// 模板中传入request值
		Map<?, ?> paramMap = request.getParameterMap();
		for (Iterator<?> itor = paramMap.keySet().iterator(); itor.hasNext();) {
			Object key = itor.next();
			Object object = paramMap.get(key);
			if (key instanceof String && object != null) {

				if (object.getClass().isArray()) {
					Object[] sValue = (Object[]) object;
					String value = sValue[0].toString();
					if (value.trim().isEmpty())
						continue;
					dataModel.put(key.toString(), (T) value);
				}
			}

		}
		return new ModelAndView(template, dataModel);
	}

	/**
	 * 分页url
	 * 
	 * @param paginationUrl
	 * @return
	 */
	public String getPaginationUrl(String paginationUrl) {
		String url = request.getContextPath()
				+ (paginationUrl.startsWith("/") ? paginationUrl : "/" + paginationUrl);
		return url.indexOf("?") > -1 ? url : url + "?aqwertyu=123";
	}
}
