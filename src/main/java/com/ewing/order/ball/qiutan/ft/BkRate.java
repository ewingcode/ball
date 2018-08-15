package com.ewing.order.ball.qiutan.ft;

public class BkRate {

	private String id;
	private String name;
	private String ratio;
	private String ior_RH;
	private String ior_RC;
	private String n_ratio;
	private String n_ior_RH;
	private String n_ior_RC;
	private String ratio_ou;
	private String ior_OUH;
	private String ior_OUC;
	private String n_ratio_ou;
	private String n_ior_OUH;
	private String n_ior_OUC;

	@Override
	public String toString() {
		return "BkRate id=" + id + ", name=" + name + ",开盘【主：" + ior_RH
				+ ",让球：" + ratio + ",客：" + ior_RC + "】,即时【 主：" + n_ior_RH
				+ ",让球：" + n_ratio + ",客：" + n_ior_RC + "】, 开盘【大:" + ior_OUH
				+ ",总分:" + ratio_ou + ",小：" + ior_OUC + "】,  即时【大：" + n_ior_OUH
				+ ",总分：" + n_ratio_ou + ",小：" + n_ior_OUC + "】";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getIor_RH() {
		return ior_RH;
	}

	public void setIor_RH(String ior_RH) {
		this.ior_RH = ior_RH;
	}

	public String getIor_RC() {
		return ior_RC;
	}

	public void setIor_RC(String ior_RC) {
		this.ior_RC = ior_RC;
	}

	public String getN_ratio() {
		return n_ratio;
	}

	public void setN_ratio(String n_ratio) {
		this.n_ratio = n_ratio;
	}

	public String getN_ior_RH() {
		return n_ior_RH;
	}

	public void setN_ior_RH(String n_ior_RH) {
		this.n_ior_RH = n_ior_RH;
	}

	public String getN_ior_RC() {
		return n_ior_RC;
	}

	public void setN_ior_RC(String n_ior_RC) {
		this.n_ior_RC = n_ior_RC;
	}

	public String getRatio_ou() {
		return ratio_ou;
	}

	public void setRatio_ou(String ratio_ou) {
		this.ratio_ou = ratio_ou;
	}

	public String getIor_OUH() {
		return ior_OUH;
	}

	public void setIor_OUH(String ior_OUH) {
		this.ior_OUH = ior_OUH;
	}

	public String getIor_OUC() {
		return ior_OUC;
	}

	public void setIor_OUC(String ior_OUC) {
		this.ior_OUC = ior_OUC;
	}

	public String getN_ratio_ou() {
		return n_ratio_ou;
	}

	public void setN_ratio_ou(String n_ratio_ou) {
		this.n_ratio_ou = n_ratio_ou;
	}

	public String getN_ior_OUH() {
		return n_ior_OUH;
	}

	public void setN_ior_OUH(String n_ior_OUH) {
		this.n_ior_OUH = n_ior_OUH;
	}

	public String getN_ior_OUC() {
		return n_ior_OUC;
	}

	public void setN_ior_OUC(String n_ior_OUC) {
		this.n_ior_OUC = n_ior_OUC;
	}
}
