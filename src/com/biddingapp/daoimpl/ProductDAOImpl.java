package com.biddingapp.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.biddingapp.dao.ProductDAO;
import com.biddingapp.pojo.Product;
import com.biddingapp.util.AppUtil;
import com.biddingapp.util.Constants;
import com.biddingapp.util.MySQLUtility;

public class ProductDAOImpl implements ProductDAO {

	@Override
	public String addProduct(Product product) throws Exception {
		Connection con = null;
		String pid = AppUtil.generateProductID();
		try {


				con = MySQLUtility.connect();
				PreparedStatement ps = con.prepareStatement("insert into Product values (?,?,?,?,?,?,?,? )");
				ps.setString(1, product.getSeller_id());
				ps.setString(2, pid);
				ps.setString(3, product.getPname());
				ps.setString(4, product.getPdesc());
				ps.setString(5, product.getPimageurl());
				ps.setDouble(6, product.getPbaseprice());
				ps.setTimestamp(7, product.getDeadline());
				ps.setString(8, Constants.STATUS_PENDING);
				ps.execute();


		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}
		return pid;

	}

	@Override
	public void deleteProduct(String pid) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("delete from product where pid=?");
			ps.setString(1, pid);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	@Override
	public void approveProduct(String pid) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("update Product set status=? where pid=?");
			ps.setString(1, Constants.STATUS_APPROVED);
			ps.setString(2, pid);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	@Override
	public void rejectProduct(String pid) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("update Product set status=? where pid=?");
			ps.setString(1, Constants.STATUS_REJECTED);
			ps.setString(2, pid);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	@Override
	public List<Product> getproducts() throws Exception {
		Connection con = null;
		List<Product> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement().executeQuery("select * from product");

			while (rs.next()) {
				Product p = new Product();
				p.setSeller_id(rs.getString("seller_id"));
				p.setPid(rs.getString("pid"));
				p.setDeadline(rs.getTimestamp("deadline"));
				p.setPbaseprice(rs.getDouble("pbaseprice"));
				p.setPdesc(rs.getString("pdesc"));
				p.setPimageurl(rs.getString("pimageurl"));
				p.setPname(rs.getString("pname"));
				p.setStatus(rs.getString("status"));
				result.add(p);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}
	}

	@Override
	public List<Product> getApprovedproducts() throws Exception {
		Connection con = null;
		List<Product> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement().executeQuery("select * from product where status='Approved'");

			while (rs.next()) {
				Product p = new Product();
				p.setSeller_id(rs.getString("seller_id"));
				p.setPid(rs.getString("pid"));
				p.setDeadline(rs.getTimestamp("deadline"));
				p.setPbaseprice(rs.getDouble("pbaseprice"));
				p.setPdesc(rs.getString("pdesc"));
				p.setPimageurl(rs.getString("pimageurl"));
				p.setPname(rs.getString("pname"));
				p.setStatus(rs.getString("status"));
				result.add(p);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}
	}

	@Override
	public List<Product> getproducts(String email, String role) throws Exception {
		Connection con = null;
		List<Product> result = new ArrayList<>();
		try {
			con = MySQLUtility.connect();
			ResultSet rs = null;
			if (role.equals(Constants.ROLE_SELLER)) {
				rs = con.createStatement().executeQuery("select * from product where seller_id='" + email + "' ");
			} else if (role.equals(Constants.ROLE_BUYER)) {
				rs = con.createStatement().executeQuery(
						"select * from product p, bidding b where p.pid = b.pid and b.buyer_id='" + email + "' ");
			}

			while (rs.next()) {
				Product p = new Product();
				p.setSeller_id(rs.getString("seller_id"));
				p.setPid(rs.getString("pid"));
				p.setDeadline(rs.getTimestamp("deadline"));
				p.setPbaseprice(rs.getDouble("pbaseprice"));
				p.setPdesc(rs.getString("pdesc"));
				p.setPimageurl(rs.getString("pimageurl"));
				p.setPname(rs.getString("pname"));
				p.setStatus(rs.getString("status"));
				result.add(p);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}
	}

	@Override
	public Product getProduct(String pid) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			ResultSet rs = con.createStatement().executeQuery("select * from product where pid='" + pid + "' ");

			rs.next();
			Product p = new Product();
			p.setSeller_id(rs.getString("seller_id"));
			p.setPid(rs.getString("pid"));
			p.setDeadline(rs.getTimestamp("deadline"));
			p.setPbaseprice(rs.getDouble("pbaseprice"));
			p.setPdesc(rs.getString("pdesc"));
			p.setPimageurl(rs.getString("pimageurl"));
			p.setPname(rs.getString("pname"));
			p.setStatus(rs.getString("status"));

			return p;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			con.close();
		}
	}

	@Override
	public void updateStatus(String pid, String status) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con.prepareStatement("update Product set status=? where pid=?");
			ps.setString(1, status);
			ps.setString(2, pid);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

	@Override
	public void readd(String pid, Double baseprice, Timestamp deadline) throws Exception {
		Connection con = null;
		try {
			con = MySQLUtility.connect();
			PreparedStatement ps = con
					.prepareStatement("update Product set status=?, pbaseprice=?, deadline=? where pid=?");
			ps.setString(1, Constants.STATUS_APPROVED);
			ps.setDouble(2, baseprice);
			ps.setTimestamp(3, deadline);
			ps.setString(4, pid);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			con.close();
		}

	}

}
