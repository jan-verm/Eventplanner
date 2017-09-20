package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.ugent.eventplanner.EditEventActivity;

public class EditEventTask extends AsyncTask<Void, Void, Void> {

	private boolean error = false;
	private EditEventActivity caller;
	private String url, title, description, start, end;

	public EditEventTask(EditEventActivity caller, String url,
			String title, String description, String start, String end) {
		
		this.caller = caller;
		this.url = url;
		this.title = title;
		this.description = description;
		this.start = start;
		this.end = end;
		
	}

	@Override
	protected Void doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(url);

		StringEntity jsonEntity = null;
		try {
			String json = "{\"title\":\"" + title + "\", \"description\":\""
					+ description + "\", \"start\":\"" + start
					+ "\", \"end\":\"" + end + "\"}";
			jsonEntity = new StringEntity(json);
			
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.setEntity(jsonEntity);
		
			client.execute(request);
			
		} catch (IOException e) {
			this.error = true;
			e.printStackTrace();
		}
		
		return null;
	}

	public boolean hasExecutionError() {
		return this.error;
	}

	@Override
	protected void onPostExecute(Void arg) {
		caller.onEditTaskExecuted(this);
	}

}
