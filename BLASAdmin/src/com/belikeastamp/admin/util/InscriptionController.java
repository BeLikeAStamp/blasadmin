package com.belikeastamp.admin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

import com.belikeastamp.admin.model.Inscription;

import android.util.Log;

public class InscriptionController {
	// JSON Node names
	private static final String TAG_INSC = "Inscriptions";
	private static final String TAG_ID = "id";
	private static final String TAG_WID = "workshopId";
	private static final String TAG_NAME = "name";
	private static final String TAG_PHONE = "phoneNumber";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_EXPERT = "expertise";
	private static final String TAG_DATE = "inscriptionDate";
	private static final String TAG_STATUS = "inscriptionStatus";
	private static final String TAG_PART = "partcipants";

	public final ClientResource cr = new ClientResource(EngineConfiguration.path + "rest/inscription");

	public InscriptionController() {
		EngineConfiguration.getInstance();
		cr.setRequestEntityBuffering(true);
		Log.i("InscriptionController", "initialisation ok !");
	}

	public void create(Inscription ins) throws Exception {
		try {
			buildPostURL(ins);
			Log.i("InscriptionController", "Creation success !");
		} catch (Exception e) {
			Log.i("InscriptionController", "Creation failed !");
			throw e;
		}
	}


	public void update(Inscription ins) throws Exception {
		try {
			buildPutURL(ins);
			Log.i("InscriptionController", "Update success !");
		} catch (Exception e) {
			Log.i("InscriptionController", "Update failed !");
			throw e;
		}
	}

	public void delete(Long id)  throws Exception {
		try {
			buildDeleteURL(id);
			Log.i("InscriptionController", "Delete success !");
		} catch (Exception e) {
			Log.i("InscriptionController", "Delete failed !");
			throw e;
		}

	}

	@SuppressWarnings("rawtypes")
	public List getAllInscriptions() {
		InputStream inputStream = getInputStreamFromUrl(EngineConfiguration.path + "rest/inscription");
		List<Inscription> list = null;
		try { 
			String jsonStr = convertInputStreamToString(inputStream);
			Log.d("Response: ", "> " + jsonStr);
			list = JSON2Inscription(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	private List<Inscription> JSON2Inscription(String json) {
		// TODO Auto-generated method stub
		List<Inscription> inscriptions = new ArrayList<Inscription>();

		if (json != null) {
			try {
				json = "{\""+TAG_INSC+"\": "+json+"}";
				//JSONObject jsonObj = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
				JSONObject jsonObj = new JSONObject(json);
				JSONArray ins = null;
				// Getting JSON Array node
				ins = jsonObj.getJSONArray(TAG_INSC);

				// looping through All Contacts
				for (int i = 0; i < ins.length(); i++) {
					JSONObject c = ins.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String wid = c.getString(TAG_WID);
					String name = c.getString(TAG_NAME);
					String email = c.getString(TAG_EMAIL);
					String phone = c.getString(TAG_PHONE);
					String part = c.getString(TAG_PART);
					String expert = c.getString(TAG_EXPERT);
					String date = c.getString(TAG_DATE);
					String status = c.getString(TAG_STATUS);
					
					// tmp hashmap for single contact
					HashMap<String, String> hash = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					hash.put(TAG_ID, id);
					hash.put(TAG_WID, wid);
					hash.put(TAG_NAME, name);
					hash.put(TAG_EMAIL, email);
					hash.put(TAG_PHONE, phone);
					hash.put(TAG_PART, part);
					hash.put(TAG_EXPERT, expert);
					hash.put(TAG_DATE, date);
					hash.put(TAG_STATUS, status);
					
					// adding Inscription to contact Inscriptions list
					Inscription new_ins = new Inscription(Long.valueOf(wid), name, phone, email, expert, date, Integer.valueOf(part));
					new_ins.setId(Long.valueOf(id));
					new_ins.setInscriptionStatus(status);
					inscriptions.add(new_ins);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}


		return inscriptions;
	}

	private void buildPostURL(Inscription ins) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/inscription"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_EMAIL, ins.getEmail()));
			params.add(new BasicNameValuePair(TAG_EXPERT, ins.getExpertise()));
			params.add(new BasicNameValuePair(TAG_NAME, ins.getName()));
			params.add(new BasicNameValuePair(TAG_PART, ""+ins.getPartcipants()));
			params.add(new BasicNameValuePair(TAG_PHONE, ins.getPhoneNumber()));
			params.add(new BasicNameValuePair(TAG_WID, ins.getWorkshopId().toString()));
			params.add(new BasicNameValuePair(TAG_DATE, ins.getInscriptionDate()));
			params.add(new BasicNameValuePair(TAG_STATUS, ins.getInscriptionStatus()));
			
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

	private void buildPutURL(Inscription ins) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/inscription"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("PUT");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", ""+ins.getId()));
			params.add(new BasicNameValuePair(TAG_EMAIL, ins.getEmail()));
			params.add(new BasicNameValuePair(TAG_EXPERT, ins.getExpertise()));
			params.add(new BasicNameValuePair(TAG_NAME, ins.getName()));
			params.add(new BasicNameValuePair(TAG_PART, ""+ins.getPartcipants()));
			params.add(new BasicNameValuePair(TAG_PHONE, ins.getPhoneNumber()));
			params.add(new BasicNameValuePair(TAG_WID, ins.getWorkshopId().toString()));
			params.add(new BasicNameValuePair(TAG_DATE, ins.getInscriptionDate()));
			params.add(new BasicNameValuePair(TAG_STATUS, ins.getInscriptionStatus()));
			
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
			URL url = new URL(EngineConfiguration.path + "rest/inscription?id="+id); 
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
