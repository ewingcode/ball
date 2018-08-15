package com.ewing.order.busi.weixin.dto;
/**
 * 
 * @author liangjie
 *
 * 2017年2月7日
 */

public class WxUserInfo {

	//用户在公众号的唯一标识
	private String openId;

	//昵称
	private String nickname;

	//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private Integer sex;

	//省份
	private String province;

	//城市
	private String city;

	//国家
	private String country;

	//用户头像url
	private String headimgurl;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
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

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

}
