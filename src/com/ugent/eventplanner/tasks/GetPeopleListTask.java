package com.ugent.eventplanner.tasks;

import interfaces.GetPeopleListCallerInterface;

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

import com.ugent.eventplanner.models.Person;

public class GetPeopleListTask extends AsyncTask<Void, Void, Void> {
	
	private GetPeopleListCallerInterface caller;
	private String homeUrl;
	private boolean error;
	private List<Person> personList;
	
	public GetPeopleListTask(GetPeopleListCallerInterface caller, String homeUrl) {
		this.caller = caller;
		this.homeUrl = homeUrl;
		error = false;
	}
	
	@Override
	protected Void doInBackground(Void... params) {

		DefaultHttpClient client = new DefaultHttpClient();
		
		// Get people url
		String peopleUrl = null;
		HttpGet urlRequest = new HttpGet(homeUrl);
		urlRequest.setHeader("Accept", "application/json");
		try {
			HttpResponse response = client.execute(urlRequest);
			String responseJson = EntityUtils.toString(response.getEntity());
			JSONObject responseObject = new JSONObject(responseJson);
			peopleUrl = responseObject.getString("people");
		} catch (IOException | JSONException e) {
			System.out.println("Http request error");
			this.error=true;
			urlRequest.abort();
		}
		
		// Get people list
		HttpGet request = new HttpGet(peopleUrl);
		request.setHeader("Accept", "application/json");
		try {
			HttpResponse response = client.execute(request);
			String responseJson = EntityUtils.toString(response.getEntity());
			JSONObject responseObject = new JSONObject(responseJson);
			JSONArray peopleJson = (JSONArray) responseObject.get("people");
			personList = new ArrayList<>();
			for (int i = 0; i < peopleJson.length(); i++) {
				JSONObject personJson = (JSONObject) peopleJson.get(i);
				Person p = new Person();
				p.setName(personJson.getString("name"));
				p.setUrl(personJson.getString("url"));
				personList.add(p);
			}
		} catch (IOException | JSONException e) {
			System.out.println("Http request error");
			this.error=true;
			request.abort();
		}
		
		return null;
	}
	
	public boolean hasExecutionError() {
		return this.error;
	}

	@Override
	protected void onPostExecute(Void arg) {
		caller.onGetPeopleListTaskExecuted(this);
	}

	public List<Person> getPersonList() {
		return personList;
	}
}
