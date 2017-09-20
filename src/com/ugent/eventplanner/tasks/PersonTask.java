package com.ugent.eventplanner.tasks;

import android.os.AsyncTask;

import com.ugent.eventplanner.models.Event;
import com.ugent.eventplanner.models.Person;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import interfaces.GetPersonCallerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PersonTask extends AsyncTask<Void, Void, Void> {

    private boolean error = false;
    private GetPersonCallerInterface caller = null;
    private Person person = null;
    private String url = null;

    public PersonTask(GetPersonCallerInterface caller, String url){
        super();
        this.caller = caller;
        this.url = url;
    }

    public Person getPerson() { return person; }

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
            person = new Person();
            person.setName(responseObject.getString("name"));
            person.setEmail(responseObject.getString("email"));
            person.setBirth_date(responseObject.getString("birth_date"));
            person.setUrl(responseObject.getString("url"));

            // Attending Events list
            JSONArray eventsJSON = responseObject.getJSONArray("events");
            List<Event> events = new ArrayList<Event>();

            for(int i=0; i<eventsJSON.length(); i++){
                JSONObject eventJSON = (JSONObject) eventsJSON.get(i);

                Event event = new Event();
                event.setTitle(eventJSON.getString("title"));
                event.setUrl(eventJSON.getString("url"));
                events.add(event);
            }
            person.setEvents(events);

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

    public boolean hasExecutionError() { return this.error; }

    @Override
    protected void onPostExecute(Void arg) { caller.onPersonTaskExecuted(this); }
}