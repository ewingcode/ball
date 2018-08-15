package com.ewing.order.core.app.action;

/**
 * @author tanson lin
 * 
 * @create:2012-2-23
 * @description:
 */
public class ActionException extends Exception {

	private static final long serialVersionUID = 1L;

	public ActionException() {
		super();
	}

	public ActionException(String message) {
		super(message);
	}

	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionException(Throwable cause) {
		super(cause);
	}
}
