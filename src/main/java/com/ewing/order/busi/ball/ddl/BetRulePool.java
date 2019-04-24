package com.ewing.order.busi.ball.ddl;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 投注规则池
 * 
 * @author tanson lam
 * @create 2016年9月6日
 */
@Entity
@Table(name = "bet_rule_pool")
public class BetRulePool implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BetRulePool() {
		super();
	} 
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;  
	@Column(name = "name")
	private String name;
	@Column(name = "long_desc")
	private String long_desc; 
	@Column(name = "param")
	private String param;
	@Column(name = "impl_code")
	private String impl_code; 
	@Column(name = "iseff")
	private String iseff;
	@Column(name = "createTime")
	private Timestamp createTime;
	@Column(name = "lastUpdate")
	private Timestamp lastUpdate;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLong_desc() {
		return long_desc;
	}
	public void setLong_desc(String long_desc) {
		this.long_desc = long_desc;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getImpl_code() {
		return impl_code;
	}
	public void setImpl_code(String impl_code) {
		this.impl_code = impl_code;
	}
	public String getIseff() {
		return iseff;
	}
	public void setIseff(String iseff) {
		this.iseff = iseff;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	} 
}