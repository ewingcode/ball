package com.ewing.order.busi.ball.action;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ewing.order.ball.BallMember;
import com.ewing.order.ball.RequestTool;
import com.ewing.order.ball.login.LoginResp;
import com.ewing.order.core.web.base.BaseRest;
import com.ewing.order.core.web.common.RequestJson;
import com.ewing.order.core.web.common.RestResult;

/**
 * 登陆接口
 * 
 * @author tanson lam
 * @creation 2017年1月11日
 * 
 */
@RestController
public class LoginRest extends BaseRest {
	@Resource
	private BallMember ballMember;

	public interface InputParameter {
		public final static String SHOPID = "shopId";
	}

	/**
	 * 登陆接口
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/ball/login.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<LoginResp> login() throws Exception {
		RequestJson requestJson = getRequestJson();
		String account = requestJson.getString("account");
		String pwd = requestJson.getString("pwd");
		checkRequired(account, "账号");
		checkRequired(pwd, "密码");
		LoginResp loginResp = RequestTool.login(account, pwd);
		//ballMember.addBkListener(account, loginResp.getUid());
		loginResp.setAccount(account);
		return RestResult.successResult(loginResp);
	}

	@RequestMapping(value = "/ball/heartBeat.op", method = RequestMethod.POST)
	@ResponseBody
	public RestResult<Boolean> heartBeat() throws Exception {
		RequestJson requestJson = getRequestJson();
		String uid = requestJson.getString("uid");
		checkRequired(uid, "uid");
		RequestTool.heartBeat(uid);
		return RestResult.successResult(true);
	}

}
