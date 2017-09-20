package com.ugent.eventplanner.tasks;

import interfaces.GetEventCallerInterface;

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

import com.ugent.eventplanner.models.Confirmation;
import com.ugent.eventplanner.models.Event;
import com.ugent.eventplanner.models.Message;
import com.ugent.eventplanner.models.Person;

public class EventTask extends AsyncTask<Void, Void, Void>{
	private boolean error = false;
	private GetEventCallerInterface caller = null;
	private Event event = null;
	private String url = null;
	private String confirmationUrl = null;
	private String messageUrl = null;
	
	public EventTask(GetEventCallerInterface caller, String url){
		super();
		this.caller = caller;
		this.url = url;
		if(url == null) {
			System.out.println("url is null!!");
		}
	}
	
	public Event getEvent() {
		return event;
	}
	
	public String getConfirmationUrl() {
		return this.confirmationUrl;
	}

	public String getMessageUrl() {
		return this.messageUrl;
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
			
			// Params
			event = new Event();
			event.setTitle(responseObject.getString("title"));
			event.setDescription(responseObject.getString("description"));
			event.setStart(responseObject.getString("start"));
			event.setEnd(responseObject.getString("end"));
			event.setUrl(responseObject.getString("url"));
			
			//Set confirmations url
			this.confirmationUrl = responseObject.getJSONObject("confirmations").getString("url");
			this.messageUrl = responseObject.getJSONObject("messages").getString("url");
			
			// Confirmations list
			JSONArray confirmationListJSON = responseObject
					.getJSONObject("confirmations")
					.getJSONArray("list");
			List<Confirmation> confirmationsList = new ArrayList<Confirmation>();
			
			for(int i=0; i<confirmationListJSON.length(); i++){
				JSONObject confirmationJSON = (JSONObject) confirmationListJSON.get(i);

				JSONObject personJSON = (JSONObject) confirmationJSON.get("person");
				Person person = new Person();
				person.setName(personJSON.getString("name"));
				person.setUrl(personJSON.getString("url"));
				
				boolean status = confirmationJSON.getBoolean("going");
				
				confirmationsList.add(new Confirmation(person, status));
			}
			event.setConfirmations(confirmationsList);
			
			// Messages list
			JSONArray messageListJSON = responseObject
					.getJSONObject("messages")
					.getJSONArray("list");
			List<Message> messagesList = new ArrayList<Message>();
			
			for(int i=0; i<messageListJSON.length(); i++){
				JSONObject messageJSON = (JSONObject) messageListJSON.get(i);
				
				Person person = new Person();
				JSONObject personJSON = (JSONObject) messageJSON.get("person");
				person.setName(personJSON.getString("name"));
				person.setUrl(personJSON.getString("url"));
				
				String text = messageJSON.getString("text");
				String created_at = messageJSON.getString("created_at");
				
				messagesList.add(new Message(text, person, created_at));
			}
			
			event.setMessages(messagesList);
			
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
		caller.onEventTaskExecuted(this);
	}
}