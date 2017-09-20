package com.ugent.eventplanner.models;

public class Message {

	private String text;
	private String created_at;
	private Person person;
	
	public Message(String text, Person person, String created_at) {
		this.text = text;
		this.created_at = created_at;
		this.person = person;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
}
