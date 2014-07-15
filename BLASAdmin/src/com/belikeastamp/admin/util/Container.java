package com.belikeastamp.admin.util;

import java.util.ArrayList;
import java.util.List;

public class Container {
	
	public List wkList;
	
	
	public Container() {
		this.wkList = new ArrayList();
	}
	
	public Container(List wkList) {
		this.wkList = wkList;
	}

	public List getWkList() {
		return this.wkList;
	}
	
	public void setWkList(List list) {
		this.wkList = list;
	}

}
