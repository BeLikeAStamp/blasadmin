package com.belikeastamp.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProjectAdminActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);

		Button add = (Button) findViewById(R.id.add);
		add.setEnabled(false);
		
		Button get = (Button) findViewById(R.id.get);
		get.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(ProjectAdminActivity.this, GetAllProjectActivity.class);
				startActivity(i);
			}
		});
		
	}
}
