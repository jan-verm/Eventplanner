package com.ugent.eventplanner.models;

import java.util.List;

public class Person {

	private String name;
	private String email;
	private String birth_date;
	private String created_at;
	private String updated_at;
	private String url;
	private String index;
	
	public Person(){}

    public Person(String name, String url) {
        super();
        this.name = name;
        this.url = url;
    }
	
	/*public Person(String name, String email, String birth_date,
			String created_at, String updated_at, String url, String index,
			List<Event> events) {
		super();
		this.name = name;
		this.email = email;
		this.birth_date = birth_date;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.url = url;
		this.index = index;
		this.events = events;
	}*/
	
	public String toString() {
		return name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	private List<Event> events;
	
}
