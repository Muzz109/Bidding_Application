package com.biddingapp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biddingapp.dao.ProductDAO;
import com.biddingapp.daoimpl.BiddingDAOImpl;
import com.biddingapp.daoimpl.FeedbackDAO;
import com.biddingapp.daoimpl.OrderDAOImpl;
import com.biddingapp.daoimpl.ProductDAOImpl;
import com.biddingapp.mail.MailThread;
import com.biddingapp.pojo.BidHistory;
import com.biddingapp.pojo.Order;
import com.biddingapp.pojo.Product;
import com.biddingapp.pojo.User;
import com.biddingapp.util.Constants;

public class BidServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			User user = (User) req.getSession().getAttribute("user");
			ProductDAO productDAO = new ProductDAOImpl();
			FeedbackDAO feedbackDAO = new FeedbackDAO();
			BiddingDAOImpl biddingDAO = new BiddingDAOImpl();
			if (user == null) {
				resp.sendRedirect("login.jsp?msg=Session Expired. Please login again");
			} else {
				String type = req.getParameter("type");
				if (type.equals("view")) {
					if (user.getRole().equals(Constants.ROLE_BUYER)) {
						String pid = req.getParameter("pid");
						Product product = productDAO.getProduct(pid);
						if (product == null) {
							resp.sendRedirect("buyer_bid.jsp?msg=Product ID " + pid + " not found in the portal");
						} else {
							List<String> bids = biddingDAO.getBidsByProductID(pid);
							Map<String, List<String>> feedbacks = feedbackDAO.getFeedbackByPid(pid);
							req.setAttribute("feedbacks", feedbacks);
							req.setAttribute("bids", bids);
							req.setAttribute("product", product);
							req.getRequestDispatcher("buyer_bid.jsp").forward(req, resp);
						}
					} else {
						resp.sendRedirect(
								"welcome.jsp?msg=You don't have the required privileges to perform this action.");
					}

				} else if (type.equals("bid")) {
					String pid = req.getParameter("pid");
					String amt = req.getParameter("amt");
					String baseprice = req.getParameter("baseprice");
					if (amt == null || amt.trim().length() == 0) {
						resp.sendRedirect("bid?msg=Please enter the Bid Amount&type=view&pid=" + pid);
					} else {
						Double prevbid = Double.parseDouble(req.getParameter("prevbid"));
						if (Double.parseDouble(amt) <= prevbid) {
							resp.sendRedirect(
									"bid?msg=Bid Amount must be higher than the Present winner's Bid&type=view&pid="
											+ pid);
						} else if (Double.parseDouble(baseprice) > Double.parseDouble(amt)) {
							resp.sendRedirect(
									"bid?msg=Bid Amount must be higher than the Base Price&type=view&pid=" + pid);

						}

						else {
							try {
								biddingDAO.perfomr_bid(pid, user.getEmail(), Double.parseDouble(amt));
								List<String> bids = biddingDAO.getBidsByProductID(pid);
								for (String b : bids) {
									String arr[] = b.split(Constants.DELIMITER);
									String rec = arr[0];
									if (!rec.equals(user.getEmail())) {
										List<String> rcv = new ArrayList<>();
										rcv.add(rec);
										Product product = productDAO.getProduct(pid);
										new MailThread("<b>Dear " + rec
												+ ",</b> <br> Another buyer has bid a higer amount for the product you had bid <br/>Product name: <b>"
												+ product.getPname() + "</b><br/>Buyer name: <b>" + user.getEmail()
												+ "</b><br/>New Bid amount: <b>" + amt+"</b>", "Bid Alert", rcv);
									}
								}
								resp.sendRedirect("bid?msg=Successfully Placed your Bid&type=view&pid=" + pid);
							} catch (Exception e) {
								resp.sendRedirect("bid?msg=Invalid Bid Amount&type=view&pid=" + pid);
							}
						}
					}
				} else if (type.equals("buyer_bid_history")) {
					OrderDAOImpl orderDAO = new OrderDAOImpl();
					List<Order> orders = orderDAO.getPlacedOrdersForBuyer(user.getEmail());
					req.setAttribute("orders", orders);
					Map<String, Product> products = new HashMap<>();
					for (Order o : orders) {
						products.put(o.getPid(), productDAO.getProduct(o.getPid()));
					}
					req.setAttribute("products", products);
					req.getRequestDispatcher("buyer_bid_history.jsp").forward(req, resp);
				} else if (type.equals("seller_bid_view")) {
					String pid = req.getParameter("pid");
					Product product = productDAO.getProduct(pid);
					List<String> bids = biddingDAO.getBidsByProductID(pid);
					req.setAttribute("bids", bids);
					req.setAttribute("product", product);
					Map<String, List<String>> feedbacks = feedbackDAO.getFeedbackByPid(pid);
					req.setAttribute("feedbacks", feedbacks);

					req.getRequestDispatcher("seller_bids.jsp").forward(req, resp);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
