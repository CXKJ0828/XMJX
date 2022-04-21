package com.zhjh.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是列表树形式显示的实体,
 * 这里的字段是在前台显示所有的,可修改
 * @author lanyuan
 * Email：mmm333zzz520@163.com
 * date：2014-11-20
 */
public class TreeWarehouseObject {
	private Integer id;
	private Integer parentId;
	private String name;
	private String parentName;
	private String address;
	private String property;
	private String contact;
	private String warehouseId;
	private List<TreeWarehouseObject> children = new ArrayList<TreeWarehouseObject>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public List<TreeWarehouseObject> getChildren() {
		return children;
	}

	public void setChildren(List<TreeWarehouseObject> children) {
		this.children = children;
	}
}
