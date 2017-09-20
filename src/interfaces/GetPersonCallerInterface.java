package interfaces;

import com.ugent.eventplanner.tasks.PersonTask;

public interface GetPersonCallerInterface {

	abstract void onPersonTaskExecuted(PersonTask task);
	
}
