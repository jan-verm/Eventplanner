package com.ugent.eventplanner;

import interfaces.GetEventCallerInterface;

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

import com.ugent.eventplanner.adapters.ConfirmationsMessagesListAdapter;
import com.ugent.eventplanner.models.Confirmation;
import com.ugent.eventplanner.models.ConfirmationMessageContainer;
import com.ugent.eventplanner.models.Event;
import com.ugent.eventplanner.models.Message;
import com.ugent.eventplanner.tasks.DeleteEventTask;
import com.ugent.eventplanner.tasks.EventTask;

public class EventActivity extends Activity implements GetEventCallerInterface{

	private static final int VISIBLE = 0;
	private static final int INVISIBLE = 4;

	private TextView titleView;
	private TextView descriptionView;
	private TextView startView;
	private TextView startLabel;
	private TextView endView;
	private TextView endLabel;
	private ExpandableListView confirmationsMessagesList;
	private String eventUrl;
	private String homeUrl;
	private String confirmationUrl;
	private String messageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		titleView = (TextView) findViewById(R.id.titleView);
		descriptionView = (TextView) findViewById(R.id.descriptionView);
		startView = (TextView) findViewById(R.id.startView);
		startLabel = (TextView) findViewById(R.id.startLabel);
		endView = (TextView) findViewById(R.id.endView);
		endLabel = (TextView) findViewById(R.id.endLabel);
		confirmationsMessagesList = (ExpandableListView) findViewById(R.id.confirmationsMessagesList);

		setVisibility(INVISIBLE);

		Intent intent = getIntent();
		this.eventUrl = intent.getStringExtra("eventUrl");
		this.homeUrl = intent.getStringExtra("homeUrl");

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new EventTask(this, eventUrl).execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.event, menu);
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
		descriptionView.setVisibility(visible);
		startView.setVisibility(visible);
		startLabel.setVisibility(visible);
		endView.setVisibility(visible);
		endLabel.setVisibility(visible);
		confirmationsMessagesList.setVisibility(visible);
	}

	public void deleteEvent(View view) {
		new DeleteEventTask(this, eventUrl).execute();
	}
	
	public void editEvent(View view) {
		Intent intent = new Intent(this, EditEventActivity.class);
		intent.putExtra("url", eventUrl);
    	startActivity(intent);
	}

	public void addAttendant(View view) {
		Intent intent = new Intent(this, AddAttendantActivity.class);
		intent.putExtra("url", this.eventUrl);
		intent.putExtra("homeUrl", this.homeUrl);
		intent.putExtra("confirmationUrl", this.confirmationUrl);
    	startActivity(intent);
	}
	
	public void addMessage(View view) {
		Intent intent = new Intent(this, AddMessageActivity.class);
		intent.putExtra("url", this.eventUrl);
		intent.putExtra("homeUrl", this.homeUrl);
		intent.putExtra("messageUrl", this.messageUrl);
    	startActivity(intent);
	}
	
	public void onDeleteTaskExecuted(DeleteEventTask task) {
		if (task.hasExecutionError()) {
			Toast.makeText(EventActivity.this,
					"An error occurred deleting this event.", Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		this.finish();
	}

	public void onEventTaskExecuted(EventTask task) {
		if (task.hasExecutionError()) {
			Toast.makeText(EventActivity.this,
					"Could not fetch data from server.", Toast.LENGTH_LONG)
					.show();
			return;
		}

		setVisibility(VISIBLE);

		Event event = task.getEvent();
		titleView.setText(event.getTitle());
		descriptionView.setText(event.getDescription());
		startView.setText(event.getStart());
		endView.setText(event.getEnd());

		this.confirmationUrl = task.getConfirmationUrl();
		this.messageUrl = task.getMessageUrl();
		
		// populate expandable list header
		List<String> listDataHeader = new ArrayList<String>();
		listDataHeader.add("Confirmations");
		listDataHeader.add("Comments");

		// populate expandable list children
		Map<String, List<ConfirmationMessageContainer>> listDataChild = new HashMap<String, List<ConfirmationMessageContainer>>();
		List<ConfirmationMessageContainer> confirmations = new ArrayList<ConfirmationMessageContainer>();

		for (Confirmation c : event.getConfirmations()) {
			ConfirmationMessageContainer cmc = null;
			try {
				cmc = new ConfirmationMessageContainer(0, c, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			confirmations.add(cmc);
		}

		List<ConfirmationMessageContainer> messages = new ArrayList<ConfirmationMessageContainer>();

		for (Message m : event.getMessages()) {
			ConfirmationMessageContainer cmc = null;
			try {
				cmc = new ConfirmationMessageContainer(1, null, m);
			} catch (Exception e) {
				e.printStackTrace();
			}
			messages.add(cmc);
		}

		listDataChild.put(listDataHeader.get(0), confirmations); // Header,
																	// Child
																	// data
		listDataChild.put(listDataHeader.get(1), messages);

		ConfirmationsMessagesListAdapter listAdapter = new ConfirmationsMessagesListAdapter(
				this, listDataHeader, listDataChild);

		confirmationsMessagesList.setAdapter(listAdapter);
	}

}
