package com.belikeastamp.admin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

import com.belikeastamp.admin.model.Project;
import com.belikeastamp.admin.util.CustomMultiPartEntity;
import com.belikeastamp.admin.util.EngineConfiguration;
import com.belikeastamp.admin.util.ProjectController;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditProjectActivity extends Activity {

	private static final int PICKFILE_RESULT_CODE = 1;
	private static final int PROTO_DISPO = 3;
	private static final int PROJECT_DISPO = 5;

	private TextView name, type, perso, subdate, orderdate, colors ;
	private Spinner status;
	private Button maj, upload, back, save;
	final ProjectController c = new ProjectController();
	private Project p;
	private String[] statusList;
	boolean updateStatus = false;
	boolean updateProto = false;
	int selected_status;
	private File file = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editproject);
		statusList = getResources().getStringArray(R.array.status_arrays);

		p = (Project) getIntent().getSerializableExtra("project");
		Log.i("EditProjectActivity", "Project = "+p);
		name = (TextView) findViewById(R.id.name);
		type = (TextView) findViewById(R.id.type);
		perso = (TextView) findViewById(R.id.perso);
		subdate = (TextView) findViewById(R.id.submitdate);
		orderdate = (TextView) findViewById(R.id.orderdate);
		colors = (TextView) findViewById(R.id.colors);
		status = (Spinner) findViewById(R.id.status);

		setData();

		maj = (Button) findViewById(R.id.maj);
		upload = (Button) findViewById(R.id.upload);
		back =  (Button) findViewById(R.id.back);
		save =  (Button) findViewById(R.id.save);

		maj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditProjectActivity.this,
						android.R.layout.simple_spinner_item, Arrays.asList(statusList));
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				status.setAdapter(dataAdapter);

				status.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						updateStatus = true;
						selected_status = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						updateStatus = false;

					}
				});
			}
		});

		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateProto = true;
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/jpeg"); 
				startActivityForResult(intent, PICKFILE_RESULT_CODE);

			}

		});


		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(updateStatus) {
					new UpdateStatusTask().execute(p);
					Toast.makeText(getApplicationContext(), "Mise à jour prise en compte", Toast.LENGTH_SHORT).show();
					String userPhoneNumer = "";

					try {
						userPhoneNumer = new GetUserPhoneNumberTask().execute(p).get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if(userPhoneNumer.length() > 0) {
						if(selected_status == PROTO_DISPO) {
							sendSMS(userPhoneNumer, "[PROTO]prototype disponible pour le projet "+p.getName());
						}
						else if(selected_status == PROJECT_DISPO) {
							sendSMS(userPhoneNumer, "[REAL]réalisation disponible pour le projet "+p.getName());
						}
					}
				}
				
				if(updateProto && file != null) {
					new SendHttpRequestTask(getApplicationContext()).execute(file);
					Toast.makeText(getApplicationContext(), "Prototype envoyé", Toast.LENGTH_SHORT).show();
				}
			}

		});

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(EditProjectActivity.this, GetAllProjectActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			}
		});

	}

	private void setData() {
		// TODO Auto-generated method stub
		name.setText(getResources().getString(R.string.project_name)+":"+p.getName());
		type.setText(getResources().getString(R.string.project_type)+":"+p.getType());
		perso.setText(getResources().getString(R.string.perso)+":"+p.getPrintableDetails());
		subdate.setText(getResources().getString(R.string.submit_date)+":"+p.getSubDate());
		orderdate.setText(getResources().getString(R.string.order_date)+":"+p.getOrderDate());
		colors.setText(getResources().getString(R.string.selected_colors)+":"+p.getColors());
		List<String> currentStatus = new ArrayList<String>();
		currentStatus.add(statusList[p.getStatus()]);
		selected_status = p.getStatus();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, currentStatus);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		status.setAdapter(dataAdapter);
	}


	protected class UpdateStatusTask extends AsyncTask<Project, Void, Void> {

		@Override
		protected Void doInBackground(Project... params) {

			Project proj = params[0];
			Log.d("UpdateStatusTask", "Project = "+proj);
			proj.setStatus(selected_status);
			try {
				c.update(proj, proj.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			

			return null;
		}

	}

	protected class GetUserPhoneNumberTask extends AsyncTask<Project, Void, String> {

		@Override
		protected String doInBackground(Project... params) {

			Project proj = params[0];
			String phoneNumber = "";
			Log.d("UpdateStatusTask", "Project = "+proj);

			try {
				phoneNumber = c.getUserPhoneNumber(proj.getUserId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			

			return phoneNumber;
		}

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case PICKFILE_RESULT_CODE:
			if(resultCode==RESULT_OK){
				String FilePath = data.getData().getPath();
				File f = new File(FilePath);

				double bytes = f.length();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				Log.w("TEST", "file length : "+megabytes);

				if(megabytes > 1) 
					Toast.makeText(getApplicationContext(), "La taille du fichier doit être inférieur à 1Mo", Toast.LENGTH_SHORT).show();
				else
					file = new File(data.getData().getPath());
			}
			break;		
		}

	}


	public class SendHttpRequestTask extends AsyncTask<File, Integer, String> {

		int serverResponseCode=0;
		//for uploading..// 
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		ProgressDialog pd;
		long totalSize;

		private Context con;
		StringBuffer buffer=new StringBuffer();

		public SendHttpRequestTask(Context con){
			this.con=con;
		} 


		@Override
		protected void onPreExecute()
		{
			pd = new ProgressDialog(EditProjectActivity.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Uploading Picture...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		protected String doInBackground(File... params) {

			File file=params[0];

			try { 
				String url = EngineConfiguration.path + "upload?type=prototype&correspondance="+p.getId();
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(url);

				MultipartEntityBuilder builder =MultipartEntityBuilder.create();        
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
				ContentBody cbFile = new FileBody(file);
				builder.addPart("file", cbFile);
				builder.addTextBody("name", "uploadedFile");
				totalSize = builder.build().getContentLength();
				Log.i("totalSize", "totalSize : "+totalSize);

				CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(builder, new CustomMultiPartEntity.ProgressListener() {

					@Override
					public void transferred(long num) {
						// TODO Auto-generated method stub
						publishProgress((int) ((num / (float) totalSize) * 100));
					}
				});

				HttpEntity mpEntity = multipartContent.getEntity();

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

			return null; 
		} 

		@Override
		protected void onProgressUpdate(Integer... progress)
		{
			pd.setProgress((int) (progress[0]));
		}

		@Override
		protected void onPostExecute(String s)
		{
			pd.dismiss();
		}
	}

	private void sendSMS(final String phoneNumber, final String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					ContentValues values = new ContentValues();

					values.put("address", phoneNumber);// txtPhoneNo.getText().toString());
		values.put("body", message);

		getContentResolver().insert(
				Uri.parse("content://sms/sent"), values);
		Toast.makeText(getBaseContext(), "SMS sent",
				Toast.LENGTH_SHORT).show();
		break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}
}
