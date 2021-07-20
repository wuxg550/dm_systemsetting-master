package com.hy.zookeeper.config.model;


public class MapDataModel{

	private Object data;
	
	public MapDataModel(Object data) {
		super();
		this.data = data;
	}

	public MapDataModel() {
		super();
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
