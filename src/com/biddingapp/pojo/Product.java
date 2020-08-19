package com.biddingapp.pojo;

import java.sql.Timestamp;

public class Product {

	private String seller_id;
	private String pid;
	private String pname;
	private String pdesc;
	private String pimageurl;
	private double pbaseprice;
	private Timestamp deadline;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPdesc() {
		return pdesc;
	}

	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}

	public String getPimageurl() {
		return pimageurl;
	}

	public void setPimageurl(String pimageurl) {
		this.pimageurl = pimageurl;
	}

	public double getPbaseprice() {
		return pbaseprice;
	}

	public void setPbaseprice(double pbaseprice) {
		this.pbaseprice = pbaseprice;
	}

	public Timestamp getDeadline() {
		return deadline;
	}

	public void setDeadline(Timestamp deadline) {
		this.deadline = deadline;
	}

}
