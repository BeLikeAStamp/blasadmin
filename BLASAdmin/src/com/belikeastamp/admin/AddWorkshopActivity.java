package com.belikeastamp.admin;

import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.WorkshopController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddWorkshopActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addworkshop);

		Button btn = (Button) findViewById(R.id.add);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addWorkshop();
			}
		});
	}

	final void addWorkshop() {
		final Thread checkUpdate = new Thread() {
			public void run() {
				EditText theme = (EditText) findViewById(R.id.theme);
				EditText address = (EditText) findViewById(R.id.address);
				EditText hostname = (EditText) findViewById(R.id.hostname);
				EditText town = (EditText) findViewById(R.id.town);
				EditText date = (EditText) findViewById(R.id.date);
				EditText cap = (EditText) findViewById(R.id.capacity);
				EditText reg = (EditText) findViewById(R.id.registered);
				
				Workshop ws = new Workshop();
				ws.setAddress(address.getText().toString());
				ws.setCapacity(Integer.valueOf(cap.getText().toString()));
				ws.setRegistered(Integer.valueOf(reg.getText().toString()));
				ws.setDate(date.getText().toString());
				ws.setHostname(hostname.getText().toString());
				ws.setTheme(theme.getText().toString());
				ws.setTown(town.getText().toString());

				final WorkshopController c = new WorkshopController();
				try {
					c.create(ws);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				final Intent intent = new Intent(AddWorkshopActivity.this,
						MainActivity.class);
				startActivity(intent);
			}
		};
		checkUpdate.start();



	}
}
