package com.hy.zookeeper.config.model;

import java.io.Serializable;

/**
 * 流向导图连线数据模型
 * @author jianweng
 *
 */
public class LinkDataModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4959088443426932141L;
	
	private String from;
	private String to;
	private String fromPoint;
	private String toPoint;
	
	private String id;
	private String source;
	private String target;
	
	private String color;
	
	public LinkDataModel() {
		super();
	}
	
	public LinkDataModel(String from, String to, String fromPoint,
			String toPoint, String color) {
		super();
		this.from = from;
		this.to = to;
		this.fromPoint = fromPoint;
		this.toPoint = toPoint;
		this.color = color;
	}

	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFromPoint() {
		return fromPoint;
	}
	public void setFromPoint(String fromPoint) {
		this.fromPoint = fromPoint;
	}
	public String getToPoint() {
		return toPoint;
	}
	public void setToPoint(String toPoint) {
		this.toPoint = toPoint;
	}
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
