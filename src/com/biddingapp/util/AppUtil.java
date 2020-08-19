package com.biddingapp.util;

public class AppUtil {

	public static String generateProductID() {
		return "PRODUCT-" + String.valueOf(System.currentTimeMillis());
	}
	public static String generateTopicID() {
		return "TOPIC-" + String.valueOf(System.currentTimeMillis());
	}
}
