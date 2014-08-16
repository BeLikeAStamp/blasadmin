package com.belikeastamp.admin;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.belikeastamp.admin.model.Tutorial;
import com.belikeastamp.admin.model.Workshop;
import com.belikeastamp.admin.util.DatePickerDialogFragment;
import com.belikeastamp.admin.util.TutorialController;
import com.belikeastamp.admin.util.WorkshopController;

public class AddTutorialActivity extends Activity {
	private static final int PICKFILE_RESULT_CODE = 1;

	private EditText title;
	private Button date, upload;
	private RadioGroup availability;
	private TextView filename;
	private boolean getFile = false;
	int selectedId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtutorial);

		title = (EditText) findViewById(R.id.title);
		date = (Button) findViewById(R.id.date);
		upload = (Button) findViewById(R.id.upload);
		availability = (RadioGroup) findViewById(R.id.availability);
		filename = (TextView) findViewById(R.id.filename);
		Button btn = (Button) findViewById(R.id.add);

		date.setOnClickListener(new View.OnClickListener() {  
			@Override  
			public void onClick(View v) {  
				DialogFragment newFragment = new DatePickerDialogFragment(callback);  
				newFragment.show(getFragmentManager(), "datePicker");
			}  
		});

		selectedId = availability.getCheckedRadioButtonId();
		availability.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.available) {
					selectedId = R.id.available;
				}
				else
				{
					selectedId = R.id.not_available;
				}
			}
		});

		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("application/pdf"); 
				startActivityForResult(intent, PICKFILE_RESULT_CODE);
			}
		});


		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String msg = checkEntries();
				if(msg.length() == 0) {
					addTutorial();
				}
				else {
					// TOAST
					Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	final void addTutorial() {
		final Thread checkUpdate = new Thread() {
			public void run() {

				Tutorial tuto = new Tutorial(title.getText().toString(), 
						(selectedId == R.id.available ? true :  false) ,
						filename.getText().toString(), date.getText().toString(), 0);

				final TutorialController c = new TutorialController();
				try {

					c.create(tuto);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				final Intent intent = new Intent(AddTutorialActivity.this,
						GetAllWorkshopActivity.class);
				startActivity(intent);
			}
		};
		checkUpdate.start();



	}

	OnDateSetListener callback = new OnDateSetListener() {  

		@Override  
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {  
			Calendar c = Calendar.getInstance();  
			c.set(year, monthOfYear, dayOfMonth);
			setDate(c.getTime().getTime());  

		}  
	}; 


	private String checkEntries() {
		StringBuffer sb = new StringBuffer("");

		if(title.getText().length() == 0) {
			sb.append("- Il faut un titre de tutoriel\n");
		}

		if(selectedId == R.id.available && (!getFile)) {
			sb.append("- Un tuto disponible = fichier attaché\n");
		}

		if(date.getText().toString().equals(getResources().getString(R.string.upload_date))) {
			sb.append("-Jour la date de depôt ?\n");
		}

		return sb.toString();
	}


	private void setDate(long millisecond){  
		int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR  
				| DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH  
				| DateUtils.FORMAT_ABBREV_WEEKDAY;  
		String dateString = DateUtils.formatDateTime(this,millisecond, flags);
		date.setText(dateString);  

	}

	private String getDate(long millisecond){  
		int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR  
				| DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH  
				| DateUtils.FORMAT_ABBREV_WEEKDAY;  
		String dateString = DateUtils.formatDateTime(this,millisecond, flags);  
		return dateString;  
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case PICKFILE_RESULT_CODE:
			if(resultCode==RESULT_OK){
				String FilePath = data.getData().getPath();
				filename.setText(FilePath);
				
			}
			break;

		}
	}

}
