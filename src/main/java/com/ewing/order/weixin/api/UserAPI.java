package com.ewing.order.weixin.api;

/**
 * 微信用户信息接口
 * 
 * @author 凡梦星尘(elkan1788@gmail.com)
 * @since 2.0
 */
public interface UserAPI {
  /** 简体 **/
  public static final String LANG_CN = "zh_CN";
  /** 繁体 **/
  public static final String LANG_TW = "zh_TW";
  /** 英文 **/
  public static final String LANG_EN = "en";
  /** snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）**/
  public static final String SCOPE_SNSAPI_BASE = "snsapi_base";
  /** snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息） **/
  public static final String SCOPE_SNSAPI_USERINFO = "snsapi_userinfo";

    /**
     * 设置备注名地址
     */
    static String user_remark = "/user/info/updateremark?access_token=";

    /**
     * 用户列表地址
     */
    static String user_list = "/user/get?access_token=%s&next_openid=%s";

    /**
     * 用户基本信息地址
     */
    static String user_info = "/user/info?access_token=%s&openid=%s&lang=%s";

    /**
     * 批量用户基本信息地址
     */
    static String batch_user_info = "/user/info/batchget?access_token=";
    
    /**
     * 网页授权获取用户基本信息 --第一步：用户同意授权，获取code
     */
    static String web_authorize_1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";

    /**
     * 网页授权获取用户基本信息 --第二步：通过code换取网页授权access_token
     */
    static String web_authorize_2 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    
    /**
     * 网页授权获取用户基本信息 --第三步:刷新access_token（如果需要）
     */
    static String web_authorize_3 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";
    /**
     * 网页授权获取用户基本信息 --第三步:刷新access_token（如果需要）
     */
    static String web_authorize_4 = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s";
    
}
