package com.ewing.order.weixin.dto;

import org.nutz.json.JsonField;

/**
 * 网页授权获取用户基本信息 - 第二步获取通过code换取网页授权access_token返回dto
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @time 2016年1月23日
 * @since 3.0
 */
public class WebAuthorizationDto {
  /**
   * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
   */
  @JsonField(value = "access_token")
  private String accessToken;

  /**
   * access_token接口调用凭证超时时间，单位（秒）
   */
  @JsonField(value = "expires_in")
  private String expiresIn;

  /**
   * 用户刷新access_token
   */
  @JsonField(value = "refresh_token")
  private String refreshToken;

  /**
   * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
   */
  @JsonField(value = "openid")
  private String openId;

  /**
   * 用户授权的作用域，使用逗号（,）分隔
   */
  @JsonField(value = "scope")
  private String scope;
  /**
   * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
   */
  @JsonField(value = "unionid")
  private String unionId;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(String expiresIn) {
    this.expiresIn = expiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  @Override
  public String toString() {
    return "WebAuthotizationDto2 [accessToken=" + accessToken + ", expiresIn=" + expiresIn
        + ", refreshToken=" + refreshToken + ", openid=" + openId + ", scope=" + scope
        + ", unionId=" + unionId + "]";
  }

}
