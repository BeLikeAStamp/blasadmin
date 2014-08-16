package com.belikeastamp.admin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.ClientResource;

import android.util.Log;

import com.belikeastamp.admin.model.Tutorial;

public class TutorialController {
	// JSON Node names
	private static final String TAG_TUTO = "tutorials";
	private static final String TAG_ID = "id";
	private static final String TAG_TITLE = "title";
	private static final String TAG_FILE = "file";
	private static final String TAG_AVAIL = "availability";
	private static final String TAG_DATE = "date";
	private static final String TAG_DEMAND = "demand";

	public final ClientResource cr = new ClientResource(EngineConfiguration.path + "rest/tutorial");

	public TutorialController() {
		EngineConfiguration.getInstance();
		cr.setRequestEntityBuffering(true);
		Log.i("TutorialController", "initialisation ok !");
	}

	public void create(Tutorial tuto) throws Exception {
		try {
			buildPostURL(tuto);
			Log.i("TutorialController", "Creation success !");
		} catch (Exception e) {
			Log.i("TutorialController", "Creation failed !");
			throw e;
		}
	}
	

	public void update(Tutorial tuto) throws Exception {
		try {
			buildPutURL(tuto);
			Log.i("TutorialController", "Update success !");
		} catch (Exception e) {
			Log.i("TutorialController", "Update failed !");
			throw e;
		}
	}
	 
	public void delete(Long id)  throws Exception {
		try {
			buildDeleteURL(id);
			Log.i("TutorialController", "Delete success !");
		} catch (Exception e) {
			Log.i("TutorialController", "Delete failed !");
			throw e;
		}
		
	}

	@SuppressWarnings("rawtypes")
	public List getAllTutorials() {
		InputStream inputStream = getInputStreamFromUrl(EngineConfiguration.path + "rest/tutorial");
		List<Tutorial> list = null;
		try { 
			String jsonStr = convertInputStreamToString(inputStream);
			Log.d("Response: ", "> " + jsonStr);
			list = JSON2Tutorial(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	private List<Tutorial> JSON2Tutorial(String json) {
		// TODO Auto-generated method stub
		List<Tutorial> tutorials = new ArrayList<Tutorial>();

		if (json != null) {
			try {
				json = "{\""+TAG_TUTO+"\": "+json+"}";
				//JSONObject jsonObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
				JSONObject jsonObj = new JSONObject(json);
				JSONArray tuto = null;
				// Getting JSON Array node
				tuto = jsonObj.getJSONArray(TAG_TUTO);

				// looping through All Contacts
				for (int i = 0; i < tuto.length(); i++) {
					JSONObject c = tuto.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String title = c.getString(TAG_TITLE);
					String avail = c.getString(TAG_AVAIL);
					String file = c.getString(TAG_FILE);
					String date = c.getString(TAG_DATE);
					String demand = c.getString(TAG_DEMAND);
					
					// tmp hashmap for single contact
					HashMap<String, String> hash = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					hash.put(TAG_ID, id);
					hash.put(TAG_TITLE, title);
					hash.put(TAG_AVAIL, avail);
					hash.put(TAG_FILE, file);
					hash.put(TAG_DATE, date);
					hash.put(TAG_DEMAND, demand);

					// adding tutorial to contact tutorials list
					Tutorial t = new Tutorial(title, Boolean.valueOf(avail), file, date, Integer.valueOf(demand));
					t.setId(Long.valueOf(id));
					tutorials.add(t);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}


		return tutorials;
	}

	private void buildPostURL(Tutorial tuto) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/tutorial"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_TITLE, tuto.getTitle()));
			params.add(new BasicNameValuePair(TAG_AVAIL, tuto.getAvailabale().toString()));
			params.add(new BasicNameValuePair(TAG_FILE, tuto.getFile()));
			params.add(new BasicNameValuePair(TAG_DATE, tuto.getDate()));
			params.add(new BasicNameValuePair(TAG_DEMAND, tuto.getOnDemand().toString()));
			
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(params));
			writer.flush();
			writer.close();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.i("HttpURL","OK");
			} else {
				// Server returned HTTP error code.
				Log.i("HttpURL","ERROR");
			}

			os.close();

			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void buildPutURL(Tutorial tuto) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/tutorial"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("PUT");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", ""+tuto.getId()));
			params.add(new BasicNameValuePair(TAG_TITLE, tuto.getTitle()));
			params.add(new BasicNameValuePair(TAG_AVAIL, tuto.getAvailabale().toString()));
			params.add(new BasicNameValuePair(TAG_FILE, tuto.getFile()));
			params.add(new BasicNameValuePair(TAG_DATE, tuto.getDate()));
			params.add(new BasicNameValuePair(TAG_DEMAND, tuto.getOnDemand().toString()));
			
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(params));
			writer.flush();
			writer.close();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.i("HttpURL","OK");
			} else {
				// Server returned HTTP error code.
				Log.i("HttpURL","ERROR");
			}

			os.close();

			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void buildDeleteURL(Long id) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/tutorial?id="+id); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("DELETE");
			conn.setDoInput(true);
			//conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", ""+id));

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Log.i("HttpURL","OK");
			} else {
				// Server returned HTTP error code.
				Log.i("HttpURL","ERROR");
			}

			//os.close();

			conn.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)
		{
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	private InputStream getInputStreamFromUrl(String url) {
		InputStream content = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
		} catch (Exception e) {
			Log.i("[GET REQUEST]", "Network exception", e);
		}
		return content;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
	}
}
