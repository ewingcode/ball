package com.ewing.order.busi.ball.ddl;

import javax.persistence.Column;

public class RollGameCompute {
	@Column(name="gid")
	private String gid;
	@Column(name="minRatioR")
	private Float minRatioR;
	@Column(name="maxRatioR")
	private Float maxRatioR;
	@Column(name="minRatioRou")
	private Float minRatioRou;
	@Column(name="maxRatioRou")
	private Float maxRatioRou;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Float getMinRatioR() {
		return minRatioR;
	}

	public void setMinRatioR(Float minRatioR) {
		this.minRatioR = minRatioR;
	}

	public Float getMaxRatioR() {
		return maxRatioR;
	}

	public void setMaxRatioR(Float maxRatioR) {
		this.maxRatioR = maxRatioR;
	}

	public Float getMinRatioRou() {
		return minRatioRou;
	}

	public void setMinRatioRou(Float minRatioRou) {
		this.minRatioRou = minRatioRou;
	}

	public Float getMaxRatioRou() {
		return maxRatioRou;
	}

	public void setMaxRatioRou(Float maxRatioRou) {
		this.maxRatioRou = maxRatioRou;
	}

}
