package com.belikeastamp.admin;

import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.WorkshopController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddWorkshopActivity extends Activity {
	private EditText theme, address, hostname, town, date, cap,reg ;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addworkshop);

		theme = (EditText) findViewById(R.id.theme);
		address = (EditText) findViewById(R.id.address);
		hostname = (EditText) findViewById(R.id.hostname);
		town = (EditText) findViewById(R.id.town);
		date = (EditText) findViewById(R.id.date);
		cap = (EditText) findViewById(R.id.capacity);
		reg = (EditText) findViewById(R.id.registered);
		Button btn = (Button) findViewById(R.id.add);


		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(theme.getEditableText().length() > 0 
						&& address.getEditableText().length() > 0 
						&& hostname.getEditableText().length() > 0
						&& town.getEditableText().length() > 0
						&& date.getEditableText().length() > 0
						&& cap.getEditableText().length() > 0
						&& reg.getEditableText().length() > 0) {
					if(Integer.valueOf(cap.getText().toString()) < 1) 
						Toast.makeText(getApplicationContext(), "Capacité inférieure à 1 ???...", Toast.LENGTH_SHORT).show();
					else
						addWorkshop();
				}
				else {
					// TOAST
					Toast.makeText(getApplicationContext(), "Il manque des infos...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	final void addWorkshop() {
		final Thread checkUpdate = new Thread() {
			public void run() {


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
						GetAllWorkshopActivity.class);
				startActivity(intent);
			}
		};
		checkUpdate.start();



	}
}
