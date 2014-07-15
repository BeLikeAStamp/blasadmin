package com.belikeastamp.admin.util;

import java.util.ArrayList;
import java.util.List;

import com.belikeastamp.admin.model.Workshop;


public class WorkshopContainer {
	public List<Workshop> list;

	public List<Workshop> getList() {
		return list;
	}

	public void setList(List<Workshop> list) {
		this.list = list;
	}

	public WorkshopContainer()
	{
		list = new ArrayList<Workshop>();
	}

	public WorkshopContainer(List<Workshop> list)
	{
		this.list = list;
	}
}
