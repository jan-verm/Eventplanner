package com.ugent.eventplanner;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ugent.eventplanner.adapters.PersonListAdapter;
import com.ugent.eventplanner.models.Person;
import com.ugent.eventplanner.tasks.ListPeopleTask;

public class PersonListActivity extends ListActivity {

	private String url;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        Intent intent = getIntent();
        this.url = intent.getStringExtra("url");
    }

    @Override
    protected void onResume() {
    	super.onResume();
        new ListPeopleTask(this, url).execute();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.people, menu);
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

    public void onTaskExecuted(ListPeopleTask task) {
        if(task.hasExecutionError()) {
            Toast.makeText(PersonListActivity.this, "Could not fetch data from server.", Toast.LENGTH_LONG).show();
            return;
        }

        List<Person> peopleList = task.getPeoplesList();

        PersonListAdapter adapter = new PersonListAdapter(this,
				android.R.layout.simple_spinner_item, peopleList);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        Person selectedItem = (Person) getListView().getItemAtPosition(position);
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra("url", selectedItem.getUrl());
        startActivity(intent);
    }
    
    public void creatNewPerson(View view) {
    	Intent intent = new Intent(this, NewPersonActivity.class);
        intent.putExtra("url", this.url);
        startActivity(intent);
    }
}
