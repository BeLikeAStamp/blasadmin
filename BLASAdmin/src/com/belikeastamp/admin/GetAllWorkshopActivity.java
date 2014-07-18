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
import android.widget.Toast;

import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.WorkshopAdapter;
import com.belikeastamp.admin.util.WorkshopController;

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

		
		ArrayAdapter<Workshop> adapter = new WorkshopAdapter(getApplicationContext(), listsName);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	RelativeLayout layout = (RelativeLayout)view;
            	TextView data = (TextView) layout.getChildAt(1);
                Log.i("Get all WK", "ID=" + data.getTag());
                Workshop w = (Workshop)data.getTag();
                Intent i = new Intent(GetAllWorkshopActivity.this, EditWorkshopActivity.class);
                i.putExtra("workshop", w);
                startActivity(i);
                // Ouverture nouvelle activité avec les details du workshop = layout addws
                // data non modifiable sauf si press bouton Edit + apparition bouton enregistrer OU 
                // bouton delete => creation entree @DELETE et @POST correspondante dans restlet (serveur)
                // second temps possibilite recherche de ws en fonction critere
 
            }
        });
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
