package com.biddingapp.pojo;

import java.util.List;

public class BidHistory {

	String pid;
	List<Double> amt;
	String result;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<Double> getAmt() {
		return amt;
	}

	public void setAmt(List<Double> amt) {
		this.amt = amt;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
