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

import com.ugent.eventplanner.NewEventActivity;

public class NewEventTask extends AsyncTask<Void, Void, Void> {
	private boolean error = false;
	private NewEventActivity caller = null;
	private String eventsUrl;
	private String title, description, start, end;
	private String newEventUrl;

	public NewEventTask(NewEventActivity caller, String eventsUrl,
			String title, String description, String start, String end) {

		this.eventsUrl = eventsUrl;
		this.caller = caller;
		this.title = title;
		this.description = description;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(eventsUrl);

		StringEntity jsonEntity = null;
		try {
			String json = "{\"title\":\"" + title + "\", \"description\":\""
					+ description + "\", \"start\":\"" + start
					+ "\", \"end\":\"" + end + "\"}";
			jsonEntity = new StringEntity(json);
			
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.setEntity(jsonEntity);
		
			HttpResponse response = client.execute(request);
			String responseJson = EntityUtils.toString(response.getEntity());
			
			JSONObject responseObject = new JSONObject(responseJson);
			this.newEventUrl = responseObject.getString("url");
			
		} catch (IOException | JSONException e) {
			this.error = true;
			e.printStackTrace();
		}

		return null;
	}
	
	public String getNewEventUrl() {
		return newEventUrl;
	}

	public boolean hasExecutionError() {
		return this.error;
	}

	@Override
	protected void onPostExecute(Void arg) {
		caller.onTaskExecuted(this);
	}

}
