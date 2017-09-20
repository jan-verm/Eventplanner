package com.ugent.eventplanner;

import interfaces.GetEventCallerInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ugent.eventplanner.models.Event;
import com.ugent.eventplanner.tasks.EditEventTask;
import com.ugent.eventplanner.tasks.EventTask;

public class EditEventActivity extends NewEventActivity implements GetEventCallerInterface {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new EventTask(this, super.url).execute();
	}

	@Override
	public void onEventTaskExecuted(EventTask task) {
		Event e = task.getEvent();
		
		titleField.setText(e.getTitle());
		descriptionField.setText(e.getDescription());
		
		System.out.println(e.getStart());
		System.out.println(e.getEnd());
		
		// Start date and time
		String[] startDate = (e.getStart().split("T"))[0].split("-");
		String[] startTime = (e.getStart().split("T"))[1].split(":");
		startDatePicker.updateDate(Integer.parseInt(startDate[0]), 
					Integer.parseInt(startDate[1]), Integer.parseInt(startDate[2]));
		startTimePicker.setCurrentHour(Integer.parseInt(startTime[0]));
		startTimePicker.setCurrentMinute(Integer.parseInt(startTime[1]));
		
		// End date and time
		String[] endDate = (e.getEnd().split("T"))[0].split("-");
		String[] endTime = (e.getEnd().split("T"))[1].split(":");
		endDatePicker.updateDate(Integer.parseInt(endDate[0]), 
					Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2]));
		endTimePicker.setCurrentHour(Integer.parseInt(endTime[0]));
		endTimePicker.setCurrentMinute(Integer.parseInt(endTime[1]));
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
		
		new EditEventTask(this, url, title, description, start, end).execute();
	}
	
	public void onEditTaskExecuted(EditEventTask editEventTask) {
		if(editEventTask.hasExecutionError()) {
			Toast.makeText(EditEventActivity.this, "Could not reach server.", Toast.LENGTH_LONG).show();
			return;
		}
		
    	this.finish();
	}
	
}
