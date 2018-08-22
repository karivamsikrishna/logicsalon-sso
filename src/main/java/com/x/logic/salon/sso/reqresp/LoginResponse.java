package com.x.logic.salon.sso.reqresp;

import com.x.logic.salon.sso.message.Message;
import com.x.logic.salon.sso.modal.UserDetails;

public class LoginResponse {

	private UserDetails user;
	private Message message;

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
