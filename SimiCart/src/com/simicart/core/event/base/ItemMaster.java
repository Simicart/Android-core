package com.simicart.core.event.base;

public class ItemMaster implements Comparable<ItemMaster> {
	String name = null;
	String method = null;
	String package_name;
	String class_name = null;
	String sku = null;
	int order = -1;

	public String getSku() {
		return this.sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return this.package_name;
	}

	public void setPackageName(String package_name) {
		this.package_name = package_name;
	}

	public String getClassName() {
		return class_name;
	}

	public void setClassName(String class_name) {
		this.class_name = class_name;

	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(String order) {
		try {
			int i_order = Integer.parseInt(order);
			this.order = i_order;
		} catch (Exception e) {
			this.order = -1;
		}
	}

	@Override
	public int compareTo(ItemMaster another) {
		int another_order = another.getOrder();
		if (order > another_order) {
			return 1;
		} else if (order < another_order) {
			return -1;
		} else {
			return 0;
		}
	}

}
