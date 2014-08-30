package com.belikeastamp.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.belikeastamp.admin.model.Inscription;
import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.InscriptionAdapter;
import com.belikeastamp.admin.util.InscriptionController;

public class GetAllRegistrationActivity extends Activity {

	private ListView listview;
	private List<Inscription> listsName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlist);

		listview = (ListView) findViewById(R.id.itemlist);

		Request request = new Request();
		try {
			listsName = (List<Inscription>) request.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		ArrayAdapter<Inscription> adapter = new InscriptionAdapter(getApplicationContext(), listsName);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RelativeLayout layout = (RelativeLayout)view;
				TextView date = (TextView) layout.getChildAt(0);
				TextView name = (TextView) layout.getChildAt(1);
				Inscription ins = (Inscription) date.getTag();
				Workshop ws = (Workshop) name.getTag();
				Intent i = new Intent(GetAllRegistrationActivity.this, EditRegistrationActivity.class);
				i.putExtra("inscription", ins);
				i.putExtra("workshop", ws);
				startActivity(i);

			}
		});
	}

	private class Request extends AsyncTask<Void, Void, List<Inscription>> {

		@SuppressWarnings("unchecked")
		@Override
		protected List<Inscription> doInBackground(Void... params) {
			InscriptionController c = new InscriptionController();
			@SuppressWarnings("rawtypes")
			List lists = new ArrayList<Inscription>();

			try {
				lists = c.getAllInscriptions();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lists;
		}

	}
}