package interfaces;

import com.ugent.eventplanner.tasks.EventTask;

public interface GetEventCallerInterface {

	abstract void onEventTaskExecuted(EventTask task);
	
}
