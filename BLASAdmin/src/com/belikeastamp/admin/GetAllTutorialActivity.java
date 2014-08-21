package com.belikeastamp.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.belikeastamp.admin.model.Tutorial;
import com.belikeastamp.admin.util.TutorialAdapter;
import com.belikeastamp.admin.util.TutorialController;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class GetAllTutorialActivity extends Activity {
	private ListView listview;
	private List<Tutorial> listsName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlist);

		listview = (ListView) findViewById(R.id.itemlist);

		Request request = new Request();
		try {
			listsName = (List<Tutorial>) request.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		
		ArrayAdapter<Tutorial> adapter = new TutorialAdapter(getApplicationContext(), listsName);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	RelativeLayout layout = (RelativeLayout)view;
            	TextView data = (TextView) layout.getChildAt(1);
                Log.i("Get all TUTOS", "ID=" + data.getTag());
                Tutorial t = (Tutorial)data.getTag();
                /*Intent i = new Intent(GetAllTutorialActivity.this, EditWorkshopActivity.class);
                i.putExtra("Tutorial", t);
                startActivity(i);*/
 
            }
        });
	}

	private class Request extends AsyncTask<Void, Void, List<Tutorial>> {
		ProgressDialog dialog;
		
		@SuppressWarnings("unchecked")
		@Override
		protected List<Tutorial> doInBackground(Void... params) {
			TutorialController c = new TutorialController();
			@SuppressWarnings("rawtypes")
			List lists = new ArrayList<Tutorial>();

			try {
				lists = c.getAllTutorials();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lists;
		}

	}

}
