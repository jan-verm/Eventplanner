package com.ugent.eventplanner.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.ugent.eventplanner.EventsListActivity;
import com.ugent.eventplanner.models.Event;

public class GetEventListTask extends AsyncTask<Void, Void, Void> {
	private boolean error = false;
	private EventsListActivity caller = null;
	private List<Event> eventsList = null;
	private String url = null;
	private String homeUrl = null;
	
	public GetEventListTask(EventsListActivity caller, String url){
		super();
		eventsList = new ArrayList<Event>();
		this.caller = caller;
		this.url = url;
	}
	
	public List<Event> getEventsList() {
		return eventsList;
	}
	
	public String getHomeUrl() {
		return homeUrl;
	}
	
	@Override
	protected Void doInBackground(Void... args) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.setHeader("Accept", "application/json");
		try {
			HttpResponse response = client.execute(request);
			String responseJson = EntityUtils.toString(response.getEntity());
			JSONObject responseObject = new JSONObject(responseJson);
			JSONArray events = (JSONArray) responseObject.get("events");
			this.homeUrl = responseObject.getString("home");
			eventsList.clear();
			for (int i = 0; i < events.length(); i++) {
				JSONObject event = (JSONObject) events.get(i);
				eventsList.add(new Event(event.getString("title"),
						event.getString("start"),
						event.getString("url")));
			}
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