/**
 * 
 */
package com.ewing.order.busi.weixin.service;

import java.util.Date;

import javax.annotation.Resource;

import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewing.order.busi.weixin.dao.AccessUserDao;
import com.ewing.order.busi.weixin.ddl.AccessUser;
import com.ewing.order.busi.weixin.dto.WxWebAuthorizeReq;
import com.ewing.order.common.prop.WeixinProp;
import com.ewing.order.util.BeanCopy;
import com.ewing.order.util.UUIDUtils;
import com.ewing.order.weixin.api.UserAPI;
import com.ewing.order.weixin.dto.WebAuthorizationDto;
import com.ewing.order.weixin.dto.WebAuthorizationUserInfo;
import com.ewing.order.weixin.service.WxUserService;

/**
 * @author liangjie
 *
 * @create 2017年2月8日
 */
@Component("wxAuthService")
public class WxAuthService {

	@Resource(name = "wxUserService")
	private WxUserService wxUserService;

	@Resource(name = "accessUserDao")
	private AccessUserDao accessUserDao;

	private Logger log = LoggerFactory.getLogger(WxAuthService.class);

	public WebAuthorizationDto getWebAuthorization(WxWebAuthorizeReq req) {

		try {
			WebAuthorizationDto webAuthorizationDto = wxUserService.getWebAccessToken(WeixinProp.appId,
					WeixinProp.appSecret, req.getCode());
			if (webAuthorizationDto == null) {
				log.error("获取验证信息错误webAuthorizationDto == null");
				return null;
			}
			return webAuthorizationDto;
		} catch (Exception e) {
			log.error("获取验证的信息发生异常", e);
			return null;
		}
	}

	public WebAuthorizationUserInfo getWxUserInfo(String accessToken, String openId) {

		try {
			WebAuthorizationUserInfo userInfo = wxUserService.getWebUserInfo(accessToken, openId, UserAPI.LANG_CN);

			if (userInfo == null) {
				log.error("获取公众号关注者信息错误");
				return null;
			}

			log.info("获取关注者信息【{}】", Json.toJson(userInfo));
			doInsertDB(userInfo);
			return userInfo;

		} catch (Exception e) {
			log.error("获取关注者信息发生异常", e);
			return null;
		}

	}

	/**
	 * 保存用户信息到数据库
	 * 
	 * @param userInfo
	 */
	private void doInsertDB(WebAuthorizationUserInfo userInfo) {

		AccessUser accessUser = new AccessUser();
		accessUser.setCity(userInfo.getCity());
		accessUser.setCountry(userInfo.getCountry());
		accessUser.setCreateTime(new Date());
		accessUser.setHeadImgUrl(userInfo.getHeadImgUrl());
		accessUser.setNickname(userInfo.getNickName());
		accessUser.setOpenId(userInfo.getOpenId());
		accessUser.setPlatform(1);
		accessUser.setProvince(userInfo.getProvince());
		accessUser.setSex(userInfo.getSex());
		String userId = UUIDUtils.getNumUUID(8);
		accessUser.setUserId(userId);

		accessUserDao.insert(accessUser);

	}

	/**
	 * 通过openId获取实体
	 * 
	 * @param openId
	 * @return
	 */
	public WebAuthorizationUserInfo getByOpenId(String openId) {

		AccessUser accessUser = accessUserDao.getOne(openId);
		if (accessUser == null) {
			return null; 
		}
		WebAuthorizationUserInfo userInfo = new WebAuthorizationUserInfo();
		BeanCopy.copy(userInfo, accessUser, true);
		return userInfo;

	}

}
