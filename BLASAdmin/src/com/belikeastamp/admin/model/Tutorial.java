package com.belikeastamp.admin.model;

import java.io.Serializable;

public class Tutorial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1778891610597448685L;
	
	private Long id;
	String title;
	String file;
	Boolean availabale;
	String date;
	Integer onDemand;
	
	public Tutorial(){}
	
	public Tutorial(String title, Boolean avail, String file, String date, Integer demand) {
		super();
		this.title = title;
		this.availabale = avail;
		this.file = file;
		this.date = date;
		this.onDemand = demand;
	}
	
	public String getTitle() {
		return title;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Boolean getAvailabale() {
		return availabale;
	}

	public void setAvailabale(Boolean availabale) {
		this.availabale = availabale;
	}

	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getOnDemand() {
		return onDemand;
	}

	public void setOnDemand(Integer onDemand) {
		this.onDemand = onDemand;
	}

	@Override
	public String toString() {
		return "Tutorial [title=" + title + ", file=" + file + ", availabale="
				+ availabale + ", date=" + date + ", demand=" + onDemand + "]";
	}

	
	
	
	
	

}
