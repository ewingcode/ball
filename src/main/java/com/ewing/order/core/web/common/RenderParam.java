package com.ewing.order.core.web.common;

/**
 * 页面渲染的参数
 * 
 * @author tansonlam
 * @create 2016年12月30日
 * 
 */
public class RenderParam {
	/**
	 * 值
	 */
	private String value;
	/**
	 * 显示内容
	 */
	private String text;
	/**
	 * 扩展字段
	 */
	private String extend;

	public RenderParam(String text, String value) {
		super();
		this.value = value;
		this.text = text;
	}

	public RenderParam(String text, String value, String extend) {
		super();
		this.value = value;
		this.text = text;
		this.extend = extend;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

}
