package com.biddingapp.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.biddingapp.util.MySQLUtility;

public class FeedbackDAO {

	public void writeFeedback(String buyer_id, String pid, String feedback) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("insert into feedback values (?,?,?) ");
			ps.setString(1, pid);
			ps.setString(2, buyer_id);
			ps.setString(3, feedback);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	public Map<String, List<String>> getFeedbackByPid(String pid) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement().executeQuery("select * from feedback where pid='" + pid + "' ");
			Map<String, List<String>> feedback = new HashMap<>();

			while (rs.next()) {
				String buyer_id = rs.getString("buyer_id");
				String fb = rs.getString("feedback");
				List<String> farr = new ArrayList<>();
				if (feedback.containsKey(buyer_id)) {
					farr = feedback.get(buyer_id);
				}
				farr.add(fb);
				feedback.put(buyer_id, farr);
			}

			return feedback;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	public Map<String, String> getFeedbackByBuyerID(String buyer_id) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement()
					.executeQuery("select * from feedback where buyer_id='" + buyer_id + "' ");
			Map<String, String> feedback = new LinkedHashMap<>();

			int i = 0;
			while (rs.next()) {
				i++;
				String pid = rs.getString("pid");
				String ppid = pid;
				if (feedback.containsKey(pid)) {
					for (int j = 0; j <= i; j++) {
						ppid += " ";
					}
				}
				feedback.put(ppid, rs.getString("feedback"));
			}

			return feedback;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}
}
