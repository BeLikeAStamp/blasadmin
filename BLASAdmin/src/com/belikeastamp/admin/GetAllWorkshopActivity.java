package com.belikeastamp.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.WorkshopController;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GetAllWorkshopActivity extends Activity {
	private ListView listview;
	private List<Workshop> listsName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewworkshop);

		listview = (ListView) findViewById(R.id.workshoplist);

		Request request = new Request();
		try {
			listsName = (List<Workshop>) request.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		ArrayAdapter<Workshop> adapter = new ArrayAdapter<Workshop>(this,
				android.R.layout.simple_list_item_1, listsName);
		listview.setAdapter(adapter);
	}

	private class Request extends AsyncTask<Void, Void, List<Workshop>> {

		@SuppressWarnings("unchecked")
		@Override
		protected List<Workshop> doInBackground(Void... params) {
			WorkshopController c = new WorkshopController();
			@SuppressWarnings("rawtypes")
			List lists = new ArrayList<Workshop>();

			try {
				lists = c.getAllWorkshops();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lists;
		}

	}

}
