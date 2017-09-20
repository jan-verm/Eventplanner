package com.ugent.eventplanner.models;

import java.util.List;

public class Event {

	private String title, description, start, end, created_at, updated_at, url, index, messages_url;
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	private List<Confirmation> confirmations = null;
	private List<Message> messages = null;
	
	public Event(){}
		
	public Event(String title, String start, String url) {
		this.title = title;
		this.start = start;
		this.url = url;
	}
	
	// All getters and setters below
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getMessages_url() {
		return messages_url;
	}

	public void setMessages_url(String messages_url) {
		this.messages_url = messages_url;
	}

	public List<Confirmation> getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(List<Confirmation> confirmations) {
		this.confirmations = confirmations;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public String toString() {
		return title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
