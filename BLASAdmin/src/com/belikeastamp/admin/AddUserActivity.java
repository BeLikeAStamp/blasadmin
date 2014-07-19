package com.belikeastamp.admin;

import com.belikeastamp.admin.model.User;
import com.belikeastamp.admin.util.UserController;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class AddUserActivity extends Activity {
	private EditText firstname, address, name, email, phone;
	private CheckBox partener, host ;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adduser);

		firstname = (EditText) findViewById(R.id.firstname);
		address = (EditText) findViewById(R.id.address);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		partener = (CheckBox) findViewById(R.id.partener);
		host = (CheckBox) findViewById(R.id.host);
		
		Button btn = (Button) findViewById(R.id.add);

		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(firstname.getEditableText().length() > 0
						&& name.getEditableText().length() > 0
						&& email.getEditableText().length() > 0
						&& phone.getEditableText().length() > 0
						) {
						addUser();
				}
				else {
					// TOAST
					Toast.makeText(getApplicationContext(), "Il manque des infos...", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	final void addUser() {
		final Thread checkUpdate = new Thread() {
			public void run() {

				User u = new User(firstname.getText().toString(),name.getText().toString(),
						phone.getText().toString(), email.getText().toString());
				u.setAddress( (address.getEditableText().length() > 0) ? address.getText().toString() : "");
				u.setIsPartener(partener.isChecked());
				u.setIsHost(host.isChecked());
				
				final UserController c = new UserController();
				try {
					c.create(u);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				final Intent intent = new Intent(AddUserActivity.this,
						GetAllUserActivity.class);
				startActivity(intent);
			}
		};
		checkUpdate.start();



	}
}
