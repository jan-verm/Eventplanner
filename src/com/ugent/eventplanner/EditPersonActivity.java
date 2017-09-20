package com.ugent.eventplanner;

import interfaces.GetPersonCallerInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ugent.eventplanner.tasks.EditPersonTask;
import com.ugent.eventplanner.tasks.PersonTask;

public class EditPersonActivity extends NewPersonActivity implements GetPersonCallerInterface{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new PersonTask(this, super.url).execute();
	}

	@Override
	public void onPersonTaskExecuted(PersonTask task) {
		nameEditText.setText(task.getPerson().getName());
		emailEditText.setText(task.getPerson().getEmail());
		bdayEditText.setText(task.getPerson().getBirth_date());
	}
	
	public void submitPerson(View view) {
		String name = nameEditText.getText().toString();
		String email = emailEditText.getText().toString();
		String bday = bdayEditText.getText().toString();
		
		new EditPersonTask(this, this.url, name, email, bday).execute();
	}
	
	public void onEditTaskExecuted(EditPersonTask task) {
		if(task.hasExecutionError()) {
			Toast.makeText(EditPersonActivity.this, "Could not reach server.", Toast.LENGTH_LONG).show();
			return;
		}
		
    	this.finish();
	}
	
}
