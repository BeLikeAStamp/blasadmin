package com.belikeastamp.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TutorialAdminActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);

		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(TutorialAdminActivity.this, AddTutorialActivity.class);
				startActivity(i);
			}
		});

		Button get = (Button) findViewById(R.id.get);
		get.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(TutorialAdminActivity.this, GetAllTutorialActivity.class);
				startActivity(i);
			}
		});


	}
}
