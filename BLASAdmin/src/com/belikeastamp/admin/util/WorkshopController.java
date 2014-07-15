package com.belikeastamp.admin.util;

import java.util.List;
import org.restlet.resource.ClientResource;
import android.util.Log;
import com.belikeastamp.admin.model.Workshop;

public class WorkshopController {
	public final ClientResource cr = new ClientResource(EngineConfiguration.path + "rest/workshop");


	public WorkshopController() {
		EngineConfiguration.getInstance();
		Log.i("WorkshopController", "initialisation ok !");
	}

	public void create(Workshop ws) throws Exception {
		final WorkshopControllerInterface c = cr.wrap(WorkshopControllerInterface.class);

		try {
			c.create(ws);
			Log.i("WorkshopController", "Creation success !");
		} catch (Exception e) {
			Log.i("WorkshopController", "Creation failed !");
			throw e;
		}
	}

	public List getAllWorkshops() {
		final WorkshopControllerInterface c = cr.wrap(WorkshopControllerInterface.class);
		Container content = c.getAllWorkshops();
		return content.getWkList();
	}

}
