package com.ugent.eventplanner;

import interfaces.GetPeopleListCallerInterface;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.ugent.eventplanner.adapters.PersonListAdapter;
import com.ugent.eventplanner.models.Person;
import com.ugent.eventplanner.tasks.AddAttendantTask;
import com.ugent.eventplanner.tasks.GetPeopleListTask;

public class AddAttendantActivity extends Activity implements GetPeopleListCallerInterface {

	private String homeUrl, confirmationUrl;
	private Spinner personSpinner;
	private CheckBox goingCheckBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_attendant);
		
		Intent intent = getIntent();
		this.homeUrl = intent.getStringExtra("homeUrl");
		this.confirmationUrl = intent.getStringExtra("confirmationUrl");
		
		personSpinner = (Spinner) findViewById(R.id.personSpinner);
		goingCheckBox = (CheckBox) findViewById(R.id.goingCheckBox);
		
		new GetPeopleListTask(this, homeUrl).execute();
	}

	public void onGetPeopleListTaskExecuted(GetPeopleListTask task) {
		List<Person> personList = task.getPersonList();
		System.out.println(personList.size());
		
		PersonListAdapter adapter = new PersonListAdapter(this,
				android.R.layout.simple_spinner_item, personList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		personSpinner.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_attendant, menu);
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
	
	public void addAttendant(View view) {
		Person person = (Person) personSpinner.getSelectedItem();
		boolean checked = goingCheckBox.isChecked();
		
		new AddAttendantTask(this, this.confirmationUrl, person, checked).execute();
	}

	public void onAddAttendantTaskExecuted(AddAttendantTask task) {
		if (task.hasExecutionError()) {
			Toast.makeText(AddAttendantActivity.this,
					"An error occurred adding this attendant.", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		this.finish();
	}
	
}
