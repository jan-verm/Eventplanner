package com.ugent.eventplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ugent.eventplanner.tasks.NewEventTask;

public class NewEventActivity extends Activity {

	protected EditText titleField;
	protected EditText descriptionField;
	protected TimePicker startTimePicker;
	protected DatePicker startDatePicker;
	protected TimePicker endTimePicker;
	protected DatePicker endDatePicker;
	
	protected String url;
	protected String homeUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);
		
		titleField = (EditText) findViewById(R.id.titleField);
		descriptionField = (EditText) findViewById(R.id.descriptionField);
				
		startDatePicker = (DatePicker) findViewById(R.id.startDatePicker);
		startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
		startTimePicker.setIs24HourView(true);

		endDatePicker = (DatePicker) findViewById(R.id.endDatePicker);
		endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
		endTimePicker.setIs24HourView(true);
		
		Intent intent = getIntent();
		this.url = intent.getStringExtra("url");
		this.homeUrl = intent.getStringExtra("homeUrl");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_event, menu);
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
	
	public void submitEvent(View view) {
		String title = titleField.getText().toString();
		String description = descriptionField.getText().toString();
		
		String start = startDatePicker.getYear()+"-"+startDatePicker.getMonth()+
				"-"+startDatePicker.getDayOfMonth()+"T"+startTimePicker.getCurrentHour()+
				":"+startTimePicker.getCurrentMinute()+":00.000Z";
		
		String end = endDatePicker.getYear()+"-"+endDatePicker.getMonth()+
				"-"+endDatePicker.getDayOfMonth()+"T"+endTimePicker.getCurrentHour()+
				":"+endTimePicker.getCurrentMinute()+":00.000Z";
		
		new NewEventTask(this, url, title, description, start, end).execute();
	}
	
	public void onTaskExecuted(NewEventTask task) {
		if(task.hasExecutionError()) {
			Toast.makeText(NewEventActivity.this, "Could not reach server.", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent(this, EventActivity.class);
		intent.putExtra("eventUrl", task.getNewEventUrl());
		intent.putExtra("homeUrl", homeUrl);
    	startActivity(intent);
    	this.finish();
		
	}
	
}
