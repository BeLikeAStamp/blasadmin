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

import com.belikeastamp.admin.model.Project;
import com.fasterxml.jackson.databind.Module.SetupContext;

import android.util.Log;


public class ProjectController {
	// JSON Node names
	private static final String TAG_PJ = "projects";
	private static final String TAG_ID = "id";
	private static final String TAG_UID = "userId";
	private static final String TAG_SDATE = "subDate";
	private static final String TAG_NAME = "name";
	private static final String TAG_TYPE = "type";
	private static final String TAG_DETAIL = "detail";
	private static final String TAG_ODATE = "orderDate";
	private static final String TAG_PERSO = "perso";
	private static final String TAG_STATUS = "status";
	private static final String TAG_QUANTITY = "quantity";
	private static final String TAG_COLORS = "colors";

	public final ClientResource cr = new ClientResource(EngineConfiguration.path + "rest/project");

	public ProjectController() {
		EngineConfiguration.getInstance();
		cr.setRequestEntityBuffering(true);
		Log.i("ProjectController", "initialisation ok !");
	}

	public void create(Project p, Long id) throws Exception {
		try {
			buildPostURL(p,id);
			Log.i("ProjectController", "Creation success !");
		} catch (Exception e) {
			Log.i("ProjectController", "Creation failed !");
			throw e;
		}
	}
	
	public void update(Project p, Long id) throws Exception {
		try {
			buildPutURL(p);
			Log.i("ProjectController", "Update success !");
		} catch (Exception e) {
			Log.i("ProjectController", "Update failed !");
			throw e;
		}
	}
	 
	public void delete(Long id)  throws Exception {
		try {
			buildDeleteURL(id);
			Log.i("ProjectController", "Delete success !");
		} catch (Exception e) {
			Log.i("ProjectController", "Delete failed !");
			throw e;
		}
		
	}

	@SuppressWarnings("rawtypes")
	public List getAllProjects() {
		InputStream inputStream = getInputStreamFromUrl(EngineConfiguration.path + "rest/project");
		List<Project> list = null;
		try { 
			String jsonStr = convertInputStreamToString(inputStream);
			Log.d("Response: ", "> " + jsonStr);
			list = JSON2Project(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public Long getProjectId(String name, Long userId) {
		InputStream inputStream = getInputStreamFromUrl(EngineConfiguration.path + "rest/project?name="+name+"&userid="+userId);
		List<Project> list = new ArrayList<Project>();
		try { 
			String jsonStr = convertInputStreamToString(inputStream);
			Log.d("Response: ", "> " + jsonStr);
			list = JSON2Project(jsonStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return (list.size() > 0) ? list.get(0).getId() : Long.valueOf(-1);
	}
	
	public String getUserPhoneNumber(Long userId) {
		return new UserController().getPhone(userId);
	}
	
	private List<Project> JSON2Project(String json) {
		
		List<Project> projects = new ArrayList<Project>();

		if (json != null) {
			try {
				json = "{\""+TAG_PJ+"\": "+json+"}";
				JSONObject jsonObj = new JSONObject(json);
				JSONArray ws = null;
				// Getting JSON Array node
				ws = jsonObj.getJSONArray(TAG_PJ);

				// looping through All Contacts
				for (int i = 0; i < ws.length(); i++) {
					JSONObject c = ws.getJSONObject(i);

					String id = c.getString(TAG_ID);
					String uid = c.getString(TAG_UID);
					String subDate = c.getString(TAG_SDATE);
					String name = c.getString(TAG_NAME);
					String detail = c.getString(TAG_DETAIL);
					String type = c.getString(TAG_TYPE);
					String orderDate = c.getString(TAG_ODATE);
					String perso = c.getString(TAG_PERSO);
					String status = c.getString(TAG_STATUS);
					String quantity = c.getString(TAG_QUANTITY);
					String colors = c.getString(TAG_COLORS);
					
					// tmp hashmap for single contact
					HashMap<String, String> hash = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					hash.put(TAG_ID, id);
					hash.put(TAG_UID, uid);
					hash.put(TAG_SDATE, subDate);
					hash.put(TAG_NAME, name);
					hash.put(TAG_DETAIL, detail);
					hash.put(TAG_TYPE, type);
					hash.put(TAG_ODATE, orderDate);
					hash.put(TAG_PERSO, perso);
					hash.put(TAG_STATUS, status);
					hash.put(TAG_QUANTITY, quantity);
					hash.put(TAG_COLORS, colors);
					
					// adding project to contact project list
					Project p = new Project(Long.valueOf(uid), name, subDate, Integer.valueOf(status),
							detail, type, orderDate, Integer.valueOf(quantity), perso);
					p.setColors(colors);
					p.setId(Long.valueOf(id));
					projects.add(p);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			Log.e("ServiceHandler", "Couldn't get any data from the url");
		}


		return projects;
	}

	private void buildPostURL(Project p, Long userId) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/project"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", ""+userId));
			params.add(new BasicNameValuePair("name", p.getName()));
			params.add(new BasicNameValuePair("subdate", p.getSubDate()));
			params.add(new BasicNameValuePair("detail", p.getDetail()));
			params.add(new BasicNameValuePair("type", p.getType()));
			params.add(new BasicNameValuePair("orderdate", p.getOrderDate()));
			params.add(new BasicNameValuePair("perso", p.getPerso()));
			params.add(new BasicNameValuePair("status", ""+p.getStatus()));
			params.add(new BasicNameValuePair("quantity", ""+p.getQuantity()));
			params.add(new BasicNameValuePair("colors", ""+p.getColors()));
			
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


	
	private void buildPutURL(Project p) {

		try {
			URL url = new URL(EngineConfiguration.path + "rest/project"); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("PUT");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userid", ""+p.getUserId()));
			params.add(new BasicNameValuePair("id", ""+p.getId()));
			params.add(new BasicNameValuePair("name", p.getName()));
			params.add(new BasicNameValuePair("subdate", p.getSubDate()));
			params.add(new BasicNameValuePair("detail", p.getDetail()));
			params.add(new BasicNameValuePair("type", p.getType()));
			params.add(new BasicNameValuePair("orderdate", p.getOrderDate()));
			params.add(new BasicNameValuePair("perso", p.getPerso()));
			params.add(new BasicNameValuePair("status", ""+p.getStatus()));
			params.add(new BasicNameValuePair("quantity", ""+p.getQuantity()));
			params.add(new BasicNameValuePair("colors", ""+p.getColors()));
			
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
			URL url = new URL(EngineConfiguration.path + "rest/project?id="+id); 
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
