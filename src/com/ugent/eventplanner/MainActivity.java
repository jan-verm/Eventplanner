package com.ugent.eventplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ugent.eventplanner.tasks.MainTask;


public class MainActivity extends Activity {
	
	private String eventsLink = null;
	private String peopleLink = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new MainTask(this, "http://events.restdesc.org/").execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void handleEvents(View view) {
    	if(eventsLink != null) {
	    	Intent intent = new Intent(this, EventsListActivity.class);
	    	intent.putExtra("url", eventsLink);
	    	startActivity(intent);
    	} else {
    		Toast.makeText(this, "Could not reach server.", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void handlePeople(View view) {
    	if(peopleLink != null) {
	    	Intent intent = new Intent(this, PersonListActivity.class);
	    	intent.putExtra("url", peopleLink);
	    	startActivity(intent);
	    } else {
			Toast.makeText(this, "Could not reach server.", Toast.LENGTH_SHORT).show();
		}
    }
    
    public void onTaskExecuted(MainTask task) {
    	if(task.hasExecutionError()){
    		Toast.makeText(this, "Could not connect to the internet!.", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	this.eventsLink = task.getEventsLink();
    	this.peopleLink = task.getPeopleLink();
    }
}
