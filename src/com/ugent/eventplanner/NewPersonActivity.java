package com.ugent.eventplanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ugent.eventplanner.tasks.NewPersonTask;

public class NewPersonActivity extends Activity {

	protected String url;
	protected EditText nameEditText, emailEditText, bdayEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_person);

		nameEditText = (EditText) findViewById(R.id.nameEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		bdayEditText = (EditText) findViewById(R.id.bdayEditText);
		
		Intent intent = getIntent();
		this.url = intent.getStringExtra("url");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.new_person, menu);
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
	
	public void submitPerson(View view) {
		String name = nameEditText.getText().toString();
		String email = emailEditText.getText().toString();
		String bday = bdayEditText.getText().toString();
		
		new NewPersonTask(this, this.url, name, email, bday).execute();
	}

	public void onTaskExecuted(NewPersonTask task) {
		if(task.hasExecutionError()) {
			Toast.makeText(NewPersonActivity.this, "Could not reach server.", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent(this, PersonActivity.class);
		intent.putExtra("url", task.getNewPersonUrl());
    	startActivity(intent);
    	this.finish();
	}
}
