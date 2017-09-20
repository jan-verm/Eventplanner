package interfaces;

import com.ugent.eventplanner.tasks.GetPeopleListTask;

public interface GetPeopleListCallerInterface {

	abstract void onGetPeopleListTaskExecuted(GetPeopleListTask task);
	
}
