package com.biddingapp.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SenderAuthentication extends Authenticator {

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return (new PasswordAuthentication(
				"aklc2017@gmail.com",
				"ashok"));
	}
}
