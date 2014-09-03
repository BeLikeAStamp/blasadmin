package com.belikeastamp.admin;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
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

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.belikeastamp.admin.model.Tutorial;
import com.belikeastamp.admin.util.CustomMultiPartEntity;
import com.belikeastamp.admin.util.DatePickerDialogFragment;
import com.belikeastamp.admin.util.EngineConfiguration;
import com.belikeastamp.admin.util.TutorialController;

public class AddTutorialActivity extends Activity {
	private static final int PICKFILE_RESULT_CODE = 1;
	private static final int ENTRY_OK = 0;
	private static final int ILLEGAL_CHAR = 1;
	private static final int EMPTY = 2;
	
	private EditText title;
	private Button date, choose_file;
	private RadioGroup availability;
	private ImageView image;
	private Button btn;
	private Long tutorialId = Long.valueOf(-1);
	int selectedId;
	File file =  null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtutorial);

		title = (EditText) findViewById(R.id.title);
		date = (Button) findViewById(R.id.date);
		choose_file = (Button) findViewById(R.id.choose_file);
		availability = (RadioGroup) findViewById(R.id.availability);
		image = (ImageView) findViewById(R.id.image);
		btn = (Button) findViewById(R.id.add);
		
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
				
				if(checkedId == R.id.available) {
					selectedId = R.id.available;
				}
				else
				{
					selectedId = R.id.not_available;
				}
			}
		});

		choose_file.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/jpeg"); 
				startActivityForResult(intent, PICKFILE_RESULT_CODE);
			}
		});


		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String msg = checkEntries();
				if(msg.length() == 0) {
					//addTutorial();

					Tutorial tuto = new Tutorial(title.getText().toString(), 
							(selectedId == R.id.available ? true :  false) ,
							"tuto_"+title.getText().toString(), date.getText().toString(), 0);
					try {
						Toast.makeText(getApplicationContext(), "Please wait..", Toast.LENGTH_LONG).show();
						btn.setEnabled(false);
						tutorialId = new AddTutorialTask().execute(tuto).get();
						if(!tutorialId.equals(Long.valueOf(-1)) && file != null) {
							new SendHttpRequestTask(getApplicationContext()).execute(file);
						}
						else
						{
							Log.e("AJOUT BLOB","echec ajout tuto ?? ou pas dispo");
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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
						"tuto_"+title.getText().toString(), date.getText().toString(), 0);

				final TutorialController c = new TutorialController();
				try {

					c.create(tuto);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				// Recuperation de l'id du tutoriel


				/*final Intent intent = new Intent(AddTutorialActivity.this,
						GetAllWorkshopActivity.class);
				startActivity(intent);*/
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

		int ret = checkTitle(title.getText().toString());
		if(ret == EMPTY) {
			sb.append("-Il faut un titre de tutoriel\n");
		}
		else if(ret == ILLEGAL_CHAR)
		{
			sb.append("-Il y a des caractères interdis dans le titre\n");
		}

		if(selectedId == R.id.available && (file == null)) {
			sb.append("-Un tuto disponible = fichier attaché\n");
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
		
		switch(requestCode){
		case PICKFILE_RESULT_CODE:
			if(resultCode==RESULT_OK){
				String FilePath = data.getData().getPath();
				File f = new File(FilePath);

				double bytes = f.length();
				double kilobytes = (bytes / 1024);
				double megabytes = (kilobytes / 1024);
				Log.w("TEST", "file length : "+megabytes);

				if(megabytes < 1) {
					Bitmap bm = BitmapFactory.decodeFile(data.getData().getPath());
					image.setImageBitmap(bm); 
					file = new File(data.getData().getPath());
				}
				else
				{
					Toast.makeText(getApplicationContext(), "La taille du fichier doit être inférieur à 1Mo", Toast.LENGTH_SHORT).show();
				}
			}
			break;		
		}

	}


	public class AddTutorialTask extends AsyncTask<Tutorial, Integer, Long> {

		@Override
		protected Long doInBackground(Tutorial... params) {
			Tutorial tuto = params[0];

			final TutorialController c = new TutorialController();
			try {
				c.create(tuto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return c.getTutorialId(tuto.getTitle());
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
			pd = new ProgressDialog(AddTutorialActivity.this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage("Uploading Picture...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		protected String doInBackground(File... params) {

			File file=params[0];

			try { 
				String url = EngineConfiguration.path + "upload?type=tutorial&correspondance="+tutorialId;
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

	private int checkTitle(String s) {
		int ret = ENTRY_OK;
		if(!(s.matches("[a-zA-Z0-9_]*"))) ret = ILLEGAL_CHAR;
		if (s.length() == 0) ret = EMPTY;
		return ret;		
	}
}

