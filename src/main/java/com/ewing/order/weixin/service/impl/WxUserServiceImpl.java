package com.ewing.order.weixin.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Lang;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewing.order.common.prop.WeixinProp;
import com.ewing.order.util.HttpTool;
import com.ewing.order.weixin.cache.AccessTokenMemoryCache;
import com.ewing.order.weixin.dto.AccessToken;
import com.ewing.order.weixin.dto.ApiResult;
import com.ewing.order.weixin.dto.FollowList;
import com.ewing.order.weixin.dto.Follower;
import com.ewing.order.weixin.dto.Follower2;
import com.ewing.order.weixin.dto.MPAccount;
import com.ewing.order.weixin.dto.MemoryCache;
import com.ewing.order.weixin.dto.WebAuthorizationDto;
import com.ewing.order.weixin.dto.WebAuthorizationUserInfo;
import com.ewing.order.weixin.exception.WechatApiException;
import com.ewing.order.weixin.service.WxUserService;

/**
 * 
 * @author liangjie
 *
 * @create 2017年2月7日
 */
@Component("wxUserService")
public class WxUserServiceImpl implements WxUserService {

	static int RETRY_COUNT = 3;

	static MemoryCache<AccessToken> _atmc;

	private static final Logger log = LoggerFactory.getLogger(WxUserServiceImpl.class);

	private MPAccount mpAct;

	public WxUserServiceImpl() {

	}

	public WxUserServiceImpl(MPAccount mpAct) {
		this.mpAct = mpAct;
		synchronized (this) {
			if (_atmc == null) {
				_atmc = new AccessTokenMemoryCache();
			}
			// if (_jstmc == null) {
			// _jstmc = new JSTicketMemoryCache();
			// }
		}
	}

	@Override
	public boolean updateRemark(String openId, String remark) {
		String url = mergeUrl(user_remark + getAccessToken());
		ApiResult ar = null;
		String data = "{\"openid\":\"" + openId + "\",\"remark\":\"" + remark + "\"}";
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.post(url, data));
			if (ar.isSuccess()) {
				return true;
			}

			log.error(String.format("Update mp[%s] user[%s] remark[%s] failed. There try %d items.", mpAct.getMpId(),
					openId, remark, i));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public FollowList getFollowerList(String nextOpenId) {
		String url = mergeUrl(user_list, getAccessToken(), Strings.sNull(nextOpenId, ""));
		ApiResult ar = null;
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.get(url));
			if (ar.isSuccess()) {
				FollowList fl = Json.fromJson(FollowList.class, ar.getJson());
				Map<String, Object> openid = (Map<String, Object>) ar.get("data");
				fl.setOpenIds(Json.fromJson(List.class, Json.toJson(openid.get("openid"))));
				return fl;
			}

			log.info(String.format("Get mp[%s] follow list failed. There try %d items.", mpAct.getMpId(), i));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	@Override
	public Follower getFollower(String openId, String lang) {
		String url = mergeUrl(user_info, getAccessToken(), openId, Strings.sNull(lang, "zh_CN"));
		ApiResult ar = null;
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.get(url));
			if (ar.isSuccess()) {
				return Json.fromJson(Follower.class, ar.getJson());
			}

			log.error(String.format("Get mp[%s] follower[%s] information failed. There try %d items.", mpAct.getMpId(),
					openId, i));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	@Override
	public List<Follower> getFollowers(Collection<Follower2> users) {
		String url = mergeUrl(batch_user_info + getAccessToken());
		ApiResult ar = null;
		String data = Json.toJson(Lang.map("user_list", users), JsonFormat.compact());
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.post(url, data));
			if (ar.isSuccess()) {
				return Json.fromJsonAsList(Follower.class, Json.toJson(ar.get("user_info_list")));
			}

			log.error(
					String.format("Get mp[%s] followers information failed. There try %d items.", mpAct.getMpId(), i));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	@Override
	public String getWebAuthorizationCode(String appId, String redirectUrl, String scope, String state) {
		return String.format(web_authorize_1, appId, redirectUrl, scope, state);
	}

	@Override
	public WebAuthorizationDto getWebAccessToken(String appId, String appsecret, String code) {
		String url = String.format(web_authorize_2, appId, appsecret, code);
		ApiResult ar = null;
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.get(url));
			if (ar.isSuccess()) {
				return Json.fromJson(WebAuthorizationDto.class, ar.getJson());
			}

			log.error("获取accessToken失败，appId【{}】,appsecret【{}】,code【{}】,返回信息【{}】", appId, appsecret, code,
					Json.toJson(ar));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	@Override
	public WebAuthorizationDto refreshWebAccessToken(String appId, String refreshToken) {
		String url = String.format(web_authorize_3, appId, refreshToken);
		ApiResult ar = null;
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.get(url));
			if (ar.isSuccess()) {
				return Json.fromJson(WebAuthorizationDto.class, ar.getJson());
			}

			log.error(String.format("Get mp[%s] refreshWebAccessToken on web failed. There try %d items.", appId, i));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	@Override
	public WebAuthorizationUserInfo getWebUserInfo(String accessToken, String openId, String lang) {
		String url = String.format(web_authorize_4, accessToken, openId, lang);
		ApiResult ar = null;
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.get(url));
			if (ar.isSuccess()) {
				log.info(ar.getJson());
				return Json.fromJson(WebAuthorizationUserInfo.class, ar.getJson());
			}

			log.error(String.format(
					"getWebUserInfo on web authorization failed accessToken[%s], openId[%s], lang[%s]. There try %d items.",
					accessToken, openId, lang, i));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	public String getAccessToken() {
		AccessToken at = _atmc.get(WeixinProp.appId);
		if (at == null || !at.isAvailable()) {
			refreshAccessToken();
			at = _atmc.get(WeixinProp.mpId);
		}
		return at.getAccessToken();
	}

	/**
	 * 强制刷新微信服务凭证
	 */
	private synchronized void refreshAccessToken() {
		String url = mergeUrl(web_authorize_3, WeixinProp.appId, WeixinProp.appSecret);
		AccessToken at = null;
		ApiResult ar = null;
		for (int i = 0; i < RETRY_COUNT; i++) {
			ar = ApiResult.create(HttpTool.get(url));
			if (ar.isSuccess()) {
				at = Json.fromJson(AccessToken.class, ar.getJson());
				_atmc.set(mpAct.getMpId(), at);
			}

			if (at != null && at.isAvailable()) {
				return;
			}

			log.error(String.format("Get mp[%s]access_token failed. There try %d items.", mpAct.getMpId(), i + 1));
		}

		throw Lang.wrapThrow(new WechatApiException(ar.getJson()));
	}

	private String mergeUrl(String url, Object... values) {
		if (!Lang.isEmpty(values)) {
			return wechatapi + String.format(url, values);
		}
		return wechatapi + url;
	}
}
