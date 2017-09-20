package com.ugent.eventplanner.tasks;

import android.os.AsyncTask;

import com.ugent.eventplanner.PersonListActivity;
import com.ugent.eventplanner.models.Person;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListPeopleTask extends AsyncTask<Void, Void, Void> {
    private boolean error = false;
    private PersonListActivity caller = null;
    private List<Person> peopleList = null;
    private String url = null;

    public ListPeopleTask(PersonListActivity caller, String url){
        super();
        peopleList = new ArrayList<Person>();
        this.caller = caller;
        this.url = url;
    }

    public List<Person> getPeoplesList() {
        return peopleList;
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
            JSONArray people = (JSONArray) responseObject.get("people");
            peopleList.clear();
            for (int i = 0; i < people.length(); i++) {
                JSONObject person = (JSONObject) people.get(i);
                peopleList.add(new Person(person.getString("name"),
                        person.getString("url")));
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
