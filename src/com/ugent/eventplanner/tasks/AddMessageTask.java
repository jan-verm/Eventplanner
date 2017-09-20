package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.ugent.eventplanner.AddMessageActivity;
import com.ugent.eventplanner.models.Person;

public class AddMessageTask extends AsyncTask<Void, Void, Void> {

	private AddMessageActivity caller;
	private String messageUrl;
	private Person person;
	private String text;
	private boolean error;
	
	public AddMessageTask(AddMessageActivity caller, String messageUrl, Person person, String text) {
		this.caller = caller;
		this.messageUrl = messageUrl;
		this.person = person;
		this.text = text;
		error = false;
	}
	
	@Override
	protected Void doInBackground(Void... params) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(messageUrl);

		StringEntity jsonEntity = null;
		try {
			String json = "{\"message\":{\"text\": \""+text+"\", \"person\":{\"url\":\""+person.getUrl()+"\"} } }";
			jsonEntity = new StringEntity(json);
			
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.setEntity(jsonEntity);
		
			HttpResponse response = client.execute(request);
			
			if(response.getStatusLine().getStatusCode() != 200) {
				System.out.println("Invalid status code");
				this.error = true;
			}
			
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
		caller.onAddMessageTaskExecuted(this);
	}
	
}
