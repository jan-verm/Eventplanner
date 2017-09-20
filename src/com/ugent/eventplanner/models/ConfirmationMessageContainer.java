package com.ugent.eventplanner.models;

public class ConfirmationMessageContainer {

	private Confirmation confirmation = null;
	private Message message = null;
	private int type; // confirmation if 0, message if 1
	
	public ConfirmationMessageContainer(int type, Confirmation confirmation, Message message) throws Exception {
		this.type = type;
		if (type == 0 ) {
			this.confirmation = confirmation;
		} else if(type == 1){
			this.message = message;
		} else {
			throw new Exception("Parameter type must be 0 or 1!");
		}
	}
	
	public String getTitle() {
		if (type == 0) {
			return confirmation.getPerson().getName();
		} else {
			return message.getPerson().getName();
		} 
	}
	
	public String getSubtitle(){
		if (type == 0) {
			return confirmation.getStatus() ? "going" : "not going";
		} else {
			return message.getText()+"\nCreated at: "+message.getCreated_at();
		}
	}
	
	public int getType() {
		return type;
	}
	
}
