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

import com.ugent.eventplanner.adapters.EventListAdapter;
import com.ugent.eventplanner.models.Event;
import com.ugent.eventplanner.tasks.GetEventListTask;

public class EventsListActivity extends ListActivity {

    private String eventsUrl = null;
    private String homeUrl = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events_list);
		
		Intent intent = getIntent();
		this.eventsUrl = intent.getStringExtra("url");
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		// In case events were deleted, update view
		new GetEventListTask(this, eventsUrl).execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.events, menu);
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

	public void onTaskExecuted(GetEventListTask task) {
		if(task.hasExecutionError()) {
			Toast.makeText(EventsListActivity.this, "Could not fetch data from server.", Toast.LENGTH_LONG).show();
			return;
		}
			
		List<Event> eventsList = task.getEventsList();
		this.homeUrl = task.getHomeUrl();
		
		EventListAdapter myAdapter = new EventListAdapter(this,
				R.layout.title_view, R.id.title, eventsList);
		
		setListAdapter(myAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		Event selectedItem = (Event) getListView().getItemAtPosition(position);
		Intent intent = new Intent(this, EventActivity.class);
		intent.putExtra("eventUrl", selectedItem.getUrl());
		intent.putExtra("homeUrl", this.homeUrl);
    	startActivity(intent);
	}
	
	public void createNewEvent(View view) {
		if(eventsUrl != null) {
	    	Intent intent = new Intent(this, NewEventActivity.class);
	    	intent.putExtra("url", eventsUrl);
	    	intent.putExtra("homeUrl", this.homeUrl);
	    	startActivity(intent);
	    } else {
			Toast.makeText(this, "Activity contains missing data!", Toast.LENGTH_SHORT).show();
		}
	}
}
