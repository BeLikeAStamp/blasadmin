package com.belikeastamp.admin;

import java.util.ArrayList;
import java.util.List;

import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.WorkshopController;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GetAllWorkshopActivity extends Activity {
	private ListView list;
	private List<String> listsName = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewworkshop);

		list = (ListView) findViewById(R.id.workshoplist);

		Request request = new Request();
		request.execute();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listsName);
		list.setAdapter(adapter);
	}

	private class Request extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			WorkshopController c = new WorkshopController();
			List lists = new ArrayList<Workshop>();

			try {
				lists = c.getAllWorkshops();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (lists != null) {
				for (Object o : lists) {
					if (o != null && o instanceof Workshop) {
						Workshop ws = (Workshop) o;
						listsName.add(ws.getTheme() + " " + ws.getTown());
					}
				}
			}
			return null;
		}

	}

}
