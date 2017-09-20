package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.ugent.eventplanner.MainActivity;

public class MainTask extends AsyncTask<Void, Void, Void> {
	private boolean error = false;
	private MainActivity caller = null;
	private String url = null;
	
	private String events = null;
	private String people = null;
	
	public MainTask(MainActivity caller, String url){
		super();
		this.caller = caller;
		this.url = url;
	}
	
	public String getEventsLink() {
		return events;
	}
	
	public String getPeopleLink() { return people; }
	
	@Override
	protected Void doInBackground(Void... args) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.setHeader("Accept", "application/json");
		try {
			HttpResponse response = client.execute(request);
			String responseJson = EntityUtils.toString(response.getEntity());
			JSONObject responseObject = new JSONObject(responseJson);
			events = responseObject.getString("events");
			people = responseObject.getString("people");
		} catch (IOException e) {
			System.out.println("IO error");
			this.error=true;
			request.abort();
		} catch (JSONException e) {
			System.out.println("JSON error");
			request.abort();
		}
		return null;
	}

	public boolean hasExecutionError() {
		return this.error;
	}
	
	@Override
	protected void onPostExecute(Void arg) {
		caller.onTaskExecuted(this);
	}
}