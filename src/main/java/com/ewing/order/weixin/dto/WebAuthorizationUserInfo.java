package com.ewing.order.weixin.dto;

import java.util.List;

import org.nutz.json.JsonField;

/**
 * 网页授权获取用户基本信息 - 第四步：拉取用户信息(需scope为 snsapi_userinfo)返回结果
 * 
 * @author Joeson Chan<chenxuegui1234@163.com>
 * @time 2016年1月23日
 * @since 3.0
 */
public class WebAuthorizationUserInfo {
    /**
     * 用户的唯一标识
     */
    @JsonField(value = "openid")
    private String openId;

    /**
     * 微信用户昵称
     */
    @JsonField(value = "nickname")
    private String nickName;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    @JsonField(value = "sex")
    private String sex;

    /**
     * 用户个人资料填写的省份
     */
    @JsonField(value = "province")
    private String province;

    /**
     * 普通用户个人资料填写的城市
     */
    @JsonField(value = "city")
    private String city;
    /**
     * 国家，如中国为CN
     */
    @JsonField(value = "country")
    private String country;
    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    @JsonField(value = "headimgurl")
    private String headImgUrl;
    /**
     * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
     */
    @JsonField(value = "privilege")
    private List<String> privilege;
    /**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
     */
    @JsonField(value = "unionid")
    private String unionId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public List<String> getPrivilege() {
      return privilege;
    }

    public void setPrivilege(List<String> privilege) {
      this.privilege = privilege;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public String toString() {
        return "WebAuthotizationDto2 [openId=" + openId + ", nickName=" + nickName + ", sex=" + sex
                + ", province=" + province + ", city=" + city + ", country=" + country
                + ", headImgUrl=" + headImgUrl + ", privilege=" + privilege + ", unionId="
                + unionId + "]";
    }

}
