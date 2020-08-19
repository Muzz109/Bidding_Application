package com.biddingapp.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.biddingapp.pojo.BidHistory;
import com.biddingapp.util.Constants;
import com.biddingapp.util.MySQLUtility;

public class BiddingDAOImpl {

	public void perfomr_bid(String pid, String buyer_id, double amt) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("insert into BIDDING values (?,?,?) ");
			ps.setString(1, pid);
			ps.setString(2, buyer_id);
			ps.setDouble(3, amt);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	public List<BidHistory> getBidsByBuyerID(String buyer_id) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			List<BidHistory> result = new ArrayList<>();

			ResultSet rs = con.createStatement()
					.executeQuery("select distinct(pid) from bidding where buyer_id='" + buyer_id + "'");
			while (rs.next()) {
				BidHistory bh = new BidHistory();
				String pid = rs.getString("pid");
				bh.setPid(pid);
				ResultSet rs2 = con.createStatement()
						.executeQuery("select * from bidding where pid='" + pid + "'  order by bidamt desc");
				int i = 0;
				List<Double> amt = new ArrayList<>();
				while (rs2.next()) {
					i++;
					if (i == 1) {
						bh.setResult(rs2.getString("buyer_id"));
					}
					if (buyer_id.equals(rs2.getString("buyer_id"))) {
						amt.add(rs2.getDouble("bidamt"));
					}
				}
				bh.setAmt(amt);
				result.add(bh);
			}

			return result;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	public List<String> getBidsByProductID(String pid) throws Exception {
		Connection con = null;
		System.out.println("pid .. "+pid);
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement()
					.executeQuery("select * from bidding where pid='"+pid+"' order by bidamt desc");
			List<String> result = new ArrayList<>();
			while (rs.next()) {
				result.add(rs.getString("buyer_id") + Constants.DELIMITER + String.valueOf(rs.getDouble("bidamt")));

			}
			System.out.println("Result .. "+result.size());
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			con.close();
		}

	}

	public void deleteAllBids(String pid) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("delete from bidding where pid=?");
			ps.setString(1, pid);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}
		
	}

}
