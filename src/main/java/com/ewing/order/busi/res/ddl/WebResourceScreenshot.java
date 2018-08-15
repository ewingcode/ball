package com.ewing.order.busi.res.ddl;
 
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 资源介绍图片
 *
 * @author tanson lam
 * @creation 2017年1月11日
 *
 */
@Entity
@Table(name = "web_resource_screenshot")
public class WebResourceScreenshot implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Integer id;
	private int resourceId;
	private String name;
	private String imageUrl;
	private String size;
	private String shortDesc;
	private int rank;
	private String iseff;
	private Date createTime;
	private Date lastUpdate;

	public WebResourceScreenshot() {
	}

	public WebResourceScreenshot(int resourceId, String name, String imageUrl, String shortDesc, int rank, String iseff,
			Date createTime, Date lastUpdate) {
		this.resourceId = resourceId;
		this.name = name;
		this.imageUrl = imageUrl;
		this.shortDesc = shortDesc;
		this.rank = rank;
		this.iseff = iseff;
		this.createTime = createTime;
		this.lastUpdate = lastUpdate;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getShortDesc() {
		return this.shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getIseff() {
		return this.iseff;
	}

	public void setIseff(String iseff) {
		this.iseff = iseff;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}
