package com.ugent.eventplanner;

import interfaces.GetPeopleListCallerInterface;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ugent.eventplanner.adapters.PersonListAdapter;
import com.ugent.eventplanner.models.Person;
import com.ugent.eventplanner.tasks.AddMessageTask;
import com.ugent.eventplanner.tasks.GetPeopleListTask;

public class AddMessageActivity extends Activity implements GetPeopleListCallerInterface {
	
	private Spinner personSpinner;
	private EditText messageEditText;
	private String homeUrl, messageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_comment);
		
		personSpinner = (Spinner) findViewById(R.id.personSpinner);
		messageEditText = (EditText) findViewById(R.id.messageEditText);
		
		Intent intent = getIntent();
		this.homeUrl = intent.getStringExtra("homeUrl");
		this.messageUrl = intent.getStringExtra("messageUrl");
		
		new GetPeopleListTask(this, homeUrl).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_comment, menu);
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

	@Override
	public void onGetPeopleListTaskExecuted(GetPeopleListTask task) {
		List<Person> personList = task.getPersonList();
		System.out.println(personList.size());
		
		PersonListAdapter adapter = new PersonListAdapter(this,
				android.R.layout.simple_spinner_item, personList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		personSpinner.setAdapter(adapter);
	}
	
	public void submitMessage(View view) {
		Person person = (Person) personSpinner.getSelectedItem();
		String msg = messageEditText.getText().toString();

		new AddMessageTask(this, messageUrl, person, msg).execute();
	}

	public void onAddMessageTaskExecuted(AddMessageTask task) {
		if (task.hasExecutionError()) {
			Toast.makeText(AddMessageActivity.this,
					"An error occurred adding this message.", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		this.finish();
	}
}
