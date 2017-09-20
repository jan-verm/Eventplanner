package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.ugent.eventplanner.EventActivity;

public class DeleteEventTask extends AsyncTask<Void, Void, Void> {

	private EventActivity caller;
	private String eventUrl;
	private boolean error;

	public DeleteEventTask(EventActivity caller, String eventUrl) {
		this.caller = caller;
		this.eventUrl = eventUrl;
		error = false;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(eventUrl);
		request.setHeader("Accept", "application/json");
		
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (IOException e) {
			System.out.println("error deleting object");
			this.error = true;
			request.abort();
		}

		if(response.getStatusLine().getStatusCode() != 204) {
			this.error = true;
		}
		
		return null;
	}

	public boolean hasExecutionError() {
		return this.error;
	}

	@Override
	protected void onPostExecute(Void arg) {
		caller.onDeleteTaskExecuted(this);
	}

}
