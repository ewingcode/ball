package com.ewing.order.ball.league;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author tansonlam
 * @create 2018年7月21日
 */ 
public class GameType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@XStreamAsAttribute()
	@XStreamAlias("id")
	private String id;
	/**
	 * 类型 FT：足球 BT:篮球
	 */
	private String gtype;
	/**
	 * 今日联赛总数
	 */
	private Integer FT_count;
	private Integer FS_FT_count;
	private Integer FU_count;
	private Integer FS_FU_count;
	/**
	 * 今日滚球联赛总数
	 */
	private Integer RB_count;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGtype() {
		return gtype;
	}

	public void setGtype(String gtype) {
		this.gtype = gtype;
	}

	public Integer getFT_count() {
		return FT_count;
	}

	public void setFT_count(Integer fT_count) {
		FT_count = fT_count;
	}

	public Integer getFS_FT_count() {
		return FS_FT_count;
	}

	public void setFS_FT_count(Integer fS_FT_count) {
		FS_FT_count = fS_FT_count;
	}

	public Integer getFU_count() {
		return FU_count;
	}

	public void setFU_count(Integer fU_count) {
		FU_count = fU_count;
	}

	public Integer getFS_FU_count() {
		return FS_FU_count;
	}

	public void setFS_FU_count(Integer fS_FU_count) {
		FS_FU_count = fS_FU_count;
	}

	public Integer getRB_count() {
		return RB_count;
	}

	public void setRB_count(Integer rB_count) {
		RB_count = rB_count;
	}

}
