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

import com.belikeastamp.admin.model.User;
import com.belikeastamp.admin.model.Workshop;

public class UserController {
	// JSON Node names
	private static final String TAG_WS = "workshops";
	private static final String TAG_ID = "id";
	private static final String TAG_THEME = "theme";
	private static final String TAG_HOSTNAME = "hostname";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_TOWN = "town";
	private static final String TAG_DATE = "date";
	private static final String TAG_CAPACITY = "capacity";
	private static final String TAG_REGISTERED = "registered";


	public final ClientResource cr = new ClientResource(EngineConfiguration.path + "rest/user");

	public UserController() {
		EngineConfiguration.getInstance();
		cr.setRequestEntityBuffering(true);
		Log.i("WorkshopController", "initialisation ok !");
	}

	public void create(User u) throws Exception {
		try {
			buildPostURL(u);
			Log.i("WorkshopController", "Creation success !");
		} catch (Exception e) {
			Log.i("WorkshopController", "Creation failed !");
			throw e;
		}
	}
	

	public void update(User u) throws Exception {
		try {
			buildPutURL(u);
			Log.i("WorkshopController", "Update success !");
		} catch (Exception e) {
			Log.i("WorkshopController", "Update failed !");
			throw e;
		}
	}
	 
	public void delete(Long id)  throws Exception {
		try {
			buildDeleteURL(id);
			Log.i("WorkshopController", "Delete success !");
		} catch (Exception e) {
			Log.i("WorkshopController", "Delete failed !");
			throw e;
		}
		
	}

	@SuppressWarnings("rawtypes")
	public List getAllUsers() {
		InputStream inputStream = getInputStreamFromUrl(EngineConfiguration.path + "rest/user");
		List<Workshop> list = null;
		try { 
			String jsonStr = convertInputStreamToString(inputStream);
			Log.d("Response: ", "> " + jsonStr);
			list = JSON2Workshop(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	private List<Workshop> JSON2Workshop(String json) {
		// TODO Auto-generated method stub
		List<Workshop> workshops = new ArrayList<Workshop>();

		if (json != null) {
			try {
				json = "{\""+TAG_WS+"\": "+json+"}";
				//JSONObject jsonObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
				JSONObject jsonObj = new JSONObject(json);
				JSONArray ws = null;
				// Getting JSON Array node
				ws = jsonObj.getJSONArray(TAG_WS);

				// looping through All Contacts
				for (int i = 0; i < ws.length(); i++) {
					JSONObject c = ws.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String theme = c.getString(TAG_THEME);
					String hostname = c.getString(TAG_HOSTNAME);
					String address = c.getString(TAG_ADDRESS);
					String town = c.getString(TAG_TOWN);

					// Phone node is JSON Object
					String date = c.getString(TAG_DATE);
					String capacity = c.getString(TAG_CAPACITY);
					String registered = c.getString(TAG_REGISTERED);

					// tmp hashmap for single contact
					HashMap<String, String> hash = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					hash.put(TAG_ID, id);
					hash.put(TAG_THEME, theme);
					hash.put(TAG_HOSTNAME, hostname);
					hash.put(TAG_ADDRESS, address);
					hash.put(TAG_TOWN, town);
					hash.put(TAG_DATE, date);
					hash.put(TAG_REGISTERED, registered);
					hash.put(TAG_CAPACITY, capacity);

					// adding workshop to contact workshops list
					Workshop w = new Workshop(theme, address, hostname, town, date, Integer.valueOf(capacity), Integer.valueOf(registered));
					w.setId(Long.valueOf(id));
					workshops.add(w);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}


		return workshops;
	}

	private void buildPostURL(User u) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/user"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("firstname", u.getFirstname()));
			params.add(new BasicNameValuePair("name", u.getName()));
			params.add(new BasicNameValuePair("address", u.getAddress()));
			params.add(new BasicNameValuePair("host", u.getIsHost().toString()));
			params.add(new BasicNameValuePair("email", u.getEmail()));
			params.add(new BasicNameValuePair("phone", u.getPhone()));
			params.add(new BasicNameValuePair("partener", u.getIsPartener().toString()));

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

	private void buildPutURL(User u) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/user"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("PUT");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", ""+u.getId()));
			params.add(new BasicNameValuePair("firstname", u.getFirstname()));
			params.add(new BasicNameValuePair("name", u.getName()));
			params.add(new BasicNameValuePair("address", u.getAddress()));
			params.add(new BasicNameValuePair("host", u.getIsHost().toString()));
			params.add(new BasicNameValuePair("email", u.getEmail()));
			params.add(new BasicNameValuePair("phone", u.getPhone()));
			params.add(new BasicNameValuePair("partener", u.getIsPartener().toString()));

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
			URL url = new URL(EngineConfiguration.path + "rest/user?id="+id); 
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
