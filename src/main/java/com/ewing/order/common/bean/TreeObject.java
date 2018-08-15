package com.ewing.order.common.bean;

/**
 * 资源分类对象
 * 
 * @author tanson lam
 * @creation 2015年5月13日
 */
public class TreeObject {

	@Override
	public String toString() {
		return "TreeObject [id=" + id + ", pId=" + pId + ", name=" + name
				+ ", open=" + open + ", file=" + file + "]";
	}

	private String id;
	private String pId;
	private String name;
	private Boolean open;
	private String file;

	public TreeObject(String id, String pId, String name, Boolean open,
			String file) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = open;
		this.file = file;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
