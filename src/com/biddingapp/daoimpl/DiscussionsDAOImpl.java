package com.biddingapp.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biddingapp.pojo.DiscussionTopic;
import com.biddingapp.pojo.Responses;
import com.biddingapp.util.AppUtil;
import com.biddingapp.util.MySQLUtility;

public class DiscussionsDAOImpl {
	
	public void deleteTopic(String id) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			con.createStatement().execute("delete from discussions where topicId='"+id+"' ");
			con.createStatement().execute("delete from responses where topicId='"+id+"' ");
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}

	}

	public void createTopic(DiscussionTopic topic) throws SQLException {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("insert into discussions values (?,?,?,?,?) ");
			ps.setString(1, AppUtil.generateTopicID());
			ps.setString(2, topic.getSub());
			ps.setString(3, topic.getDescription());
			ps.setString(4, topic.getCreater());
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
	}

	public List<DiscussionTopic> getAllTopics() throws SQLException {
		Connection con = null;
		List<DiscussionTopic> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement().executeQuery("select * from discussions");
			while (rs.next()) {
				DiscussionTopic topic = new DiscussionTopic();
				topic.setCreater(rs.getString("creater"));
				topic.setDescription(rs.getString("description"));
				topic.setEntrytime(rs.getTimestamp("entrytime"));
				topic.setSub(rs.getString("sub"));
				topic.setTopicid(rs.getString("topicid"));

				result.add(topic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}

		return result;
	}

	public void writeResponse(Responses response) throws SQLException {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("insert into responses values (?,?,?,?) ");
			ps.setString(1, response.getTopicid());
			ps.setString(2, response.getResponder());
			ps.setString(3, response.getResponsetext());
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
	}

	public DiscussionTopic getTopicByID(String id) throws Exception {
		Connection con = null;
		DiscussionTopic topic = new DiscussionTopic();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement().executeQuery("select * from discussions where topicid='" + id + "'");
			rs.next();
			topic.setCreater(rs.getString("creater"));
			topic.setDescription(rs.getString("description"));
			topic.setEntrytime(rs.getTimestamp("entrytime"));
			topic.setSub(rs.getString("sub"));
			topic.setTopicid(rs.getString("topicid"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}

		return topic;

	}

	public List<Responses> getAllResponses(String topicid) throws SQLException {
		Connection con = null;
		List<Responses> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement()
					.executeQuery("select * from responses where topicid='" + topicid + "' ");
			while (rs.next()) {
				Responses response = new Responses();
				response.setEntrytime(rs.getTimestamp("entrytime"));
				response.setResponder(rs.getString("responder"));
				response.setResponsetext(rs.getString("responsetext"));
				response.setTopicid(rs.getString("topicid"));
				result.add(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return result;
	}

}
