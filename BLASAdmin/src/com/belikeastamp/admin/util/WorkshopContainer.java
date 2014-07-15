package com.belikeastamp.admin.util;

import java.util.ArrayList;
import java.util.List;

import com.belikeastamp.admin.model.Workshop;


public class WorkshopContainer {
	
	public List<Workshop> wkList;
	
	
	public WorkshopContainer() {
		this.wkList = new ArrayList<Workshop>();
	}
	
	public WorkshopContainer(List<Workshop> wkList) {
		this.wkList = wkList;
	}

	public List<Workshop> getWkList() {
		return this.wkList;
	}
	
	public void setWkList(List<Workshop> list) {
		this.wkList = list;
	}

}
