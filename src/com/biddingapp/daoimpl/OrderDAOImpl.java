package com.biddingapp.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.biddingapp.pojo.Order;
import com.biddingapp.util.MySQLUtility;

public class OrderDAOImpl {

	public void placeOrder(Order order) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("insert into ORDERS values (?,?,?,?,?) ");
			ps.setString(1, order.getPid());
			ps.setString(2, order.getBuyerid());
			ps.setString(3, order.getSellerid());
			ps.setDouble(4, order.getAmount());
			ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	public List<Order> getPlacedOrdersForSeller(String seller_id) throws Exception {
		Connection con = null;
		List<Order> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement()
					.executeQuery("select * from ORDERS where seller_id='" + seller_id + "' ");
			while (rs.next()) {
				Order o = new Order();
				o.setPid(rs.getString("pid"));
				o.setAmount(rs.getDouble("amount"));
				o.setBuyerid(rs.getString("buyer_id"));
				o.setEntrytime(rs.getTimestamp("entrytime"));
				o.setSellerid(rs.getString("seller_id"));
				result.add(o);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			con.close();
		}

	}

	public List<Order> getPlacedOrdersForBuyer(String buyer_id) throws Exception {
		Connection con = null;
		List<Order> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement()
					.executeQuery("select * from ORDERS where buyer_id='" + buyer_id + "' ");
			while (rs.next()) {
				Order o = new Order();
				o.setPid(rs.getString("pid"));
				o.setAmount(rs.getDouble("amount"));
				o.setBuyerid(rs.getString("buyer_id"));
				o.setEntrytime(rs.getTimestamp("entrytime"));
				o.setSellerid(rs.getString("seller_id"));
				result.add(o);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			con.close();
		}

	}

}
