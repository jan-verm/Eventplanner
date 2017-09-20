package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.ugent.eventplanner.NewPersonActivity;

public class NewPersonTask extends AsyncTask<Void, Void, Void> {
	private boolean error = false;
	private NewPersonActivity caller = null;
	private String peopleUrl;
	private String name, email, bday;
	private String newPersonUrl;

	public NewPersonTask(NewPersonActivity caller, String peopleUrl,
			String name, String email, String bday) {

		this.peopleUrl = peopleUrl;
		this.caller = caller;
		this.name = name;
		this.email = email;
		this.bday = bday;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(peopleUrl);

		StringEntity jsonEntity = null;
		try {
			String json = "{\"name\":\"" + name + "\", \"email\":\""
					+ email	+ "\", \"birth_date\":\"" + bday + "\"}";
			jsonEntity = new StringEntity(json);
			
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.setEntity(jsonEntity);
		
			HttpResponse response = client.execute(request);
			String responseJson = EntityUtils.toString(response.getEntity());
			
			JSONObject responseObject = new JSONObject(responseJson);
			this.newPersonUrl = responseObject.getString("url");
			
		} catch (IOException | JSONException e) {
			this.error = true;
			e.printStackTrace();
		}

		return null;
	}
	
	public String getNewPersonUrl() {
		return newPersonUrl;
	}

	public boolean hasExecutionError() {
		return this.error;
	}

	@Override
	protected void onPostExecute(Void arg) {
		caller.onTaskExecuted(this);
	}

}