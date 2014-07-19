package com.belikeastamp.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }
	
	public void startTutorialAdminActivity(View v) {
		Intent intent = new Intent(MainActivity.this,
				TutorialAdminActivity.class);
		startActivity(intent);
	}
	
	public void startWorkshopAdminActivity(View v) {
		Intent intent = new Intent(MainActivity.this,
				WorkshopAdminActivity.class);
		startActivity(intent);
	}
	
	public void startProjectAdminActivity(View v) {
		Intent intent = new Intent(MainActivity.this,
				ProjectAdminActivity.class);
		startActivity(intent);
	}
	
	public void startUserAdminActivity(View v) {
		Intent intent = new Intent(MainActivity.this,
				UserAdminActivity.class);
		startActivity(intent);
	}
	
	public void startRegistrationAdminActivity(View v) {
		Intent intent = new Intent(MainActivity.this,
				RegistrationAdminActivity.class);
		startActivity(intent);
	}
		
}
