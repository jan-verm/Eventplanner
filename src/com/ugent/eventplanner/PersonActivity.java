package com.ugent.eventplanner;

import interfaces.GetPersonCallerInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ugent.eventplanner.adapters.ConfirmedEventsListAdapter;
import com.ugent.eventplanner.models.Event;
import com.ugent.eventplanner.models.Person;
import com.ugent.eventplanner.tasks.DeletePersonTask;
import com.ugent.eventplanner.tasks.PersonTask;

public class PersonActivity extends Activity implements GetPersonCallerInterface {

    private static final int VISIBLE = 0;
    private static final int INVISIBLE = 4;

    private TextView nameView;
    private TextView emailView;
    private TextView birthView;
    private TextView birthLabel;
    private ExpandableListView confirmedEventsList;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        nameView = (TextView) findViewById(R.id.nameView);
        emailView = (TextView) findViewById(R.id.emailView);
        birthView = (TextView) findViewById(R.id.birthView);
        birthLabel = (TextView) findViewById(R.id.birthLabel);
        confirmedEventsList = (ExpandableListView) findViewById(R.id.confirmedEventsList);

        setVisibility(INVISIBLE);

        Intent intent = getIntent();
        this.url = intent.getStringExtra("url");

    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	new PersonTask(this, url).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setVisibility(int visible) {
        emailView.setVisibility(visible);
        birthView.setVisibility(visible);
        birthLabel.setVisibility(visible);
        confirmedEventsList.setVisibility(visible);
    }

    public void onPersonTaskExecuted(PersonTask task) {
        if (task.hasExecutionError()) {
            Toast.makeText(PersonActivity.this, "Could not fetch data from server.", Toast.LENGTH_LONG).show();
            return;
        }

        Person person = task.getPerson();
        nameView.setText(person.getName());
        emailView.setText(person.getEmail());
        birthView.setText(person.getBirth_date());
        setVisibility(VISIBLE);

        // populate expandable list header
        List<String> listDataHeader = new ArrayList<String>();
        listDataHeader.add("Attending Events");

        // populate expandable list children
        Map<String, List<Event>> listDataChild = new HashMap<String, List<Event>>();
        List<Event> confirmations = person.getEvents();

        listDataChild.put(listDataHeader.get(0), confirmations); // Header

        ConfirmedEventsListAdapter listAdapter =
                new ConfirmedEventsListAdapter(this, listDataHeader, listDataChild);

        confirmedEventsList.setAdapter(listAdapter);
    }
    
    public void deletePerson(View view){
    	new DeletePersonTask(this, this.url).execute();
    }
    
	public void onDeleteTaskExecuted(DeletePersonTask task) {
		if (task.hasExecutionError()) {
			Toast.makeText(PersonActivity.this,
					"An error occurred deleting this event.", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		this.finish();
	}
    
    public void editPerson(View view){
		Intent intent = new Intent(this, EditPersonActivity.class);
		intent.putExtra("url", this.url);
    	startActivity(intent);
    }
    
}
