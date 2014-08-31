package com.belikeastamp.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.belikeastamp.admin.model.User;
import com.belikeastamp.admin.util.UserAdapter;
import com.belikeastamp.admin.util.UserController;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GetAllUserActivity extends Activity {
	private ListView listview;
	private List<User> listsName;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemlist);

		listview = (ListView) findViewById(R.id.itemlist);

		Request request = new Request();
		try {
			listsName = (List<User>) request.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		
		ArrayAdapter<User> adapter = new UserAdapter(getApplicationContext(), listsName);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {           
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LinearLayout layout = (LinearLayout)view;
            	TextView data = (TextView) layout.getChildAt(0);
                Log.i("Get all Users", "ID=" + data.getTag());
                User w = (User)data.getTag();
                Intent i = new Intent(GetAllUserActivity.this, EditUserActivity.class);
                i.putExtra("user", w);
                startActivity(i);
				// TODO Auto-generated method stub
				
			}
        });
	}

	private class Request extends AsyncTask<Void, Void, List<User>> {

		@SuppressWarnings("unchecked")
		@Override
		protected List<User> doInBackground(Void... params) {
			UserController c = new UserController();
			@SuppressWarnings("rawtypes")
			List lists = new ArrayList<User>();

			try {
				lists = c.getAllUsers();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return lists;
		}

	}
}
