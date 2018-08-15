package com.ewing.order.common.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanson lin
 * 
 * @create:2012-2-26
 * @description:在EXT构建TREE的公用对象
 */
public class MenuObject {

    private String text;
    private Boolean expanded;
    private Boolean leaf;
    private Integer id;
    private String hrefTarget;
    private String iconCls;
    private String level;
    private Integer parentId;
    private List<MenuObject> children = new ArrayList<MenuObject>();

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<MenuObject> getChildren() {
        return children;
    }

    public void setChildren(List<MenuObject> children) {
        this.children = children;
    }

    public String getHrefTarget() {
        return hrefTarget;
    }

    public void setHrefTarget(String hrefTarget) {
        this.hrefTarget = hrefTarget;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
