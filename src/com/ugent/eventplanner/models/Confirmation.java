package com.ugent.eventplanner.models;

public class Confirmation {

	private Person person;
	private boolean status;
	
	public Confirmation(Person person, boolean status) {
		this.person = person;
		this.status = status;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
