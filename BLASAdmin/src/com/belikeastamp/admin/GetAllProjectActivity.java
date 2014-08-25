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

import com.belikeastamp.admin.model.Project;
import com.belikeastamp.admin.util.ProjectAdapter;
import com.belikeastamp.admin.util.ProjectController;

public class GetAllProjectActivity extends Activity {
	private ListView listview;
	private List<Project> listsName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlist);

		listview = (ListView) findViewById(R.id.itemlist);

		Request request = new Request();
		try {
			listsName = (List<Project>) request.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		String[] status = getResources().getStringArray(R.array.status_arrays);
		ArrayAdapter<Project> adapter = new ProjectAdapter(getApplicationContext(), listsName, status);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	RelativeLayout layout = (RelativeLayout)view;
            	TextView data = (TextView) layout.getChildAt(0);
            	Project project = (Project) data.getTag();
                Log.i("GetAllProjectActivity", "Project = "+project);
                Intent i = new Intent(GetAllProjectActivity.this, EditProjectActivity.class);
                i.putExtra("project", project);
                startActivity(i);
 
            }
        });
	}

	private class Request extends AsyncTask<Void, Void, List<Project>> {
		
		@SuppressWarnings("unchecked")
		@Override
		protected List<Project> doInBackground(Void... params) {
			ProjectController c = new ProjectController();
			@SuppressWarnings("rawtypes")
			List lists = new ArrayList<Project>();

			try {
				lists = c.getAllProjects();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lists;
		}

	}
}
