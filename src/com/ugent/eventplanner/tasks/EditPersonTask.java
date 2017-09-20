package com.ugent.eventplanner.tasks;

import java.io.IOException;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.ugent.eventplanner.EditPersonActivity;

public class EditPersonTask extends AsyncTask<Void, Void, Void> {
	private boolean error = false;
	private EditPersonActivity caller = null;
	private String personUrl;
	private String name, email, bday;

	public EditPersonTask(EditPersonActivity caller, String personUrl,
			String name, String email, String bday) {

		this.personUrl = personUrl;
		this.caller = caller;
		this.name = name;
		this.email = email;
		this.bday = bday;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPut request = new HttpPut(personUrl);

		StringEntity jsonEntity = null;
		try {
			String json = "{\"person\":{\"name\":\"" + name + "\", \"email\":\""
					+ email	+ "\", \"birth_date\":\"" + bday + "\"}}";
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