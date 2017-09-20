package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.ugent.eventplanner.AddAttendantActivity;
import com.ugent.eventplanner.models.Person;

public class AddAttendantTask extends AsyncTask<Void, Void, Void> {
	
	private AddAttendantActivity caller;
	private String confirmationUrl;
	private boolean error;
	private Person person;
	private boolean going;
	
	
	public AddAttendantTask(AddAttendantActivity caller, String confirmationUrl, Person person, boolean going) {
		this.caller = caller;
		this.confirmationUrl = confirmationUrl;
		this.person = person;
		this.going = going;
		error = false;
	}
	
	@Override
	protected Void doInBackground(Void... params) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(confirmationUrl);

		StringEntity jsonEntity = null;
		try {
			String json = "{\"confirmation\":{\"person\":{\"url\":\""+person.getUrl()+"\"}, \"going\": "+going+"}}";
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
		caller.onAddAttendantTaskExecuted(this);
	}
	
}
