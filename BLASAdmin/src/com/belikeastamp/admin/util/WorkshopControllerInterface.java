package com.belikeastamp.admin.util;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.belikeastamp.admin.model.Workshop;

public interface WorkshopControllerInterface {
	 @Put
	 void create(Workshop ws);

	 @Get
	 WorkshopContainer getAllWorkshops();
}
