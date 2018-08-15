package com.ewing.order.core.web.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 编辑页面封装的返回信息，包括修改的bean和页面需要渲染的参数
 * 
 * @author tansonlam
 * @create 2016年12月30日
 * 
 */
public class EditFormBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, List<RenderParam>> renderParams = Maps.newHashMap();
	private T resultBean;

	public EditFormBean(RenderParamContext renderParamContext) {
		super();
		this.renderParams = renderParamContext.getRenderParams();
	}

	public EditFormBean(RenderParamContext renderParamContext, T editData) {
		super();
		this.renderParams = renderParamContext.getRenderParams();
		this.resultBean = editData;
	}

	public Map<String, List<RenderParam>> getRenderParams() {
		return renderParams;
	}

	public void setRenderParams(Map<String, List<RenderParam>> renderParams) {
		this.renderParams = renderParams;
	}

	public T getData() {
		return resultBean;
	}

	public void setData(T data) {
		this.resultBean = data;
	}
}
