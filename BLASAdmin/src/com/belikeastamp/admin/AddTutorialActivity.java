package com.belikeastamp.admin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.belikeastamp.admin.model.Tutorial;
import com.belikeastamp.admin.util.DatePickerDialogFragment;
import com.belikeastamp.admin.util.EngineConfiguration;
import com.belikeastamp.admin.util.TutorialController;

public class AddTutorialActivity extends Activity {
	private static final int PICKFILE_RESULT_CODE = 1;

	private EditText title;
	private Button date, upload;
	private RadioGroup availability;
	private TextView filename;
	private Long tutorialId = Long.valueOf(123456789);
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
				intent.setType("image/jpeg"); 
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

				/*Bitmap bm = BitmapFactory.decodeStream(
						getContentResolver().openInputStream(data.getData()));
				Bitmap bm = BitmapFactory.decodeFile(data.getData().getPath());
				image.setImageBitmap(bm); 
				 */
				new SendHttpRequestTask(getApplicationContext()).execute(new File(data.getData().getPath()));
			}
			break;		
		}

	}



	public class SendHttpRequestTask extends AsyncTask<File, Void, String>{
		private ProgressDialog mProgressDialog;

		int serverResponseCode=0;
		//for uploading..// 
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";

		private Context con;
		StringBuffer buffer=new StringBuffer();

		public SendHttpRequestTask(Context con){
			this.con=con;
		} 
		/*
		@Override 
		protected void onPreExecute() { 
			mProgressDialog=new ProgressDialog(con);
			mProgressDialog.setMessage("Loading");
			mProgressDialog.show();
			super.onPreExecute();
		} */

		protected String doInBackground(File... params) {

			File file=params[0];

			try { 
				String url = EngineConfiguration.path + "upload?correspondance="+tutorialId;
				HttpClient client = new DefaultHttpClient();
		        HttpPost post = new HttpPost(url);
		        MultipartEntityBuilder builder =MultipartEntityBuilder.create();        
		        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		        ContentBody cbFile = new FileBody(file);
		        builder.addPart("file", cbFile);
		        builder.addTextBody("name", "uploadedFile");
		        HttpEntity mpEntity = builder.build();
		        post.setEntity(mpEntity);
		        
		        HttpResponse response = client.execute(post);
		       
		        HttpEntity resEntity = response.getEntity();
		        String Response=EntityUtils.toString(resEntity);


				Log.i("uploadFile", "HTTP Response is : "
						+ Response);

			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} 
			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			//mProgressDialog.dismiss();

			return null; 
		} 

	} 



}

