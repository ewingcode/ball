package com.ewing.order.busi.weixin.ddl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author 47652
 *
 * @since 2017年2月17日
 */
@Entity
@Table(name = "pay_history")
public class PayHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;
	/** 业务流水 */
	private String bizId;
	/** 外部系统流水 **/
	private String outBizId;
	/** 业务类型 1:订单 2:退款 **/
	private String busiType;
	/** 业务ID,表order_info主键,退货为order_refund主键 **/
	private Integer busiId;
	/** 支付方式 0:微信 1:支付宝 **/
	private Integer payType;
	/** 消费者ID **/
	private Integer customerId;
	/** 接入用户ID **/
	private String userId;
	/** 交易费用 **/
	private BigDecimal payFee;
	/** 交易状态 0:发起 1:成功 2:失败 **/
	private Integer status;
	/** 备注 */
	private String comment;
	private String prePayId;
	private String returnCode;
	private String returnMsg;
	private Date createTime;
	private Date lastUpdate;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public PayHistory() {
		super();
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getPrePayId() {
		return prePayId;
	}

	public void setPrePayId(String prePayId) {
		this.prePayId = prePayId;
	}

	public String getOutBizId() {
		return outBizId;
	}

	public void setOutBizId(String outBizId) {
		this.outBizId = outBizId;
	}

	public BigDecimal getPayFee() {
		return payFee;
	}

	public void setPayFee(BigDecimal payFee) {
		this.payFee = payFee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public Integer getBusiId() {
		return busiId;
	}

	public void setBusiId(Integer busiId) {
		this.busiId = busiId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
