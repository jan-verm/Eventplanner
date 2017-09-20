package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.ugent.eventplanner.PersonActivity;

public class DeletePersonTask extends AsyncTask<Void, Void, Void>{

	private PersonActivity caller;
	private String personUrl;
	private boolean error;

	public DeletePersonTask(PersonActivity caller, String personUrl) {
		this.caller = caller;
		this.personUrl = personUrl;
		error = false;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(personUrl);
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
			System.out.println("Wrong status code :(");
			this.error = true;
		}
		
		return null;
	}

	public boolean hasExecutionError() {
		return this.error;
	}

	@Override
	protected void onPostExecute(Void result) {
		caller.onDeleteTaskExecuted(this);
	}
		
}
