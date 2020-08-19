package com.biddingapp.dao;

import java.sql.Timestamp;
import java.util.List;

import com.biddingapp.pojo.Product;

public interface ProductDAO {

	public String addProduct(Product product) throws Exception;

	public void deleteProduct(String pid) throws Exception;

	public void approveProduct(String pid) throws Exception;

	public void updateStatus(String pid, String status) throws Exception;

	public void rejectProduct(String pid) throws Exception;

	public List<Product> getproducts() throws Exception;
	public List<Product> getApprovedproducts() throws Exception;

	public List<Product> getproducts(String email, String role) throws Exception;

	public Product getProduct(String pid) throws Exception;

	public void readd(String pid, Double baseprice, Timestamp deadline) throws Exception;

}
