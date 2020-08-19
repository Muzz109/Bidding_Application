package com.biddingapp.pojo;

import java.sql.Timestamp;

public class Responses {

	String topicid;
	String responder;
	String responsetext;
	Timestamp entrytime;

	public String getTopicid() {
		return topicid;
	}

	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}

	public String getResponder() {
		return responder;
	}

	public void setResponder(String responder) {
		this.responder = responder;
	}

	public String getResponsetext() {
		return responsetext;
	}

	public void setResponsetext(String responsetext) {
		this.responsetext = responsetext;
	}

	public Timestamp getEntrytime() {
		return entrytime;
	}

	public void setEntrytime(Timestamp entrytime) {
		this.entrytime = entrytime;
	}

}
