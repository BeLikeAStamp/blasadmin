package com.belikeastamp.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserAdminActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);

		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserAdminActivity.this, AddUserActivity.class);
				startActivity(i);
			}
		});
		
		Button get = (Button) findViewById(R.id.get);
		get.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UserAdminActivity.this, GetAllUserActivity.class);
				startActivity(i);
			}
		});
		

	}
}
