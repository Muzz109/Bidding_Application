package com.trigger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.biddingapp.dao.ProductDAO;
import com.biddingapp.daoimpl.BiddingDAOImpl;
import com.biddingapp.daoimpl.FeedbackDAO;
import com.biddingapp.daoimpl.OrderDAOImpl;
import com.biddingapp.daoimpl.ProductDAOImpl;
import com.biddingapp.mail.SendMail;
import com.biddingapp.pojo.Order;
import com.biddingapp.pojo.Product;
import com.biddingapp.util.Constants;

public class BackgroundTask extends TimerTask {

	ProductDAO productsDAO = new ProductDAOImpl();
	FeedbackDAO feedbackDAO = new FeedbackDAO();
	BiddingDAOImpl biddingDAO = new BiddingDAOImpl();
	OrderDAOImpl orderDAO = new OrderDAOImpl();

	@Override
	public void run() {
		System.out.println("Background Job called..");
		try {
			List<Product> products = productsDAO.getproducts();
			Timestamp curtime = new Timestamp(System.currentTimeMillis());
			for (Product p : products) {
				if (p.getStatus().equals(Constants.STATUS_APPROVED)) {
					if (curtime.after(p.getDeadline())) {

						productsDAO.updateStatus(p.getPid(), Constants.STATUS_BIDDING_CLOSED);

						List<String> bids = biddingDAO.getBidsByProductID(p.getPid());
						biddingDAO.deleteAllBids(p.getPid());

						if (bids != null && bids.size() > 0) {
							System.out.println();
							Order order = new Order();
							order.setPid(p.getPid());
							order.setSellerid(p.getSeller_id());
							String[] arr = bids.get(0).split(Constants.DELIMITER);
							order.setBuyerid(arr[0]);
							order.setAmount(Double.parseDouble(arr[1]));
							orderDAO.placeOrder(order);
							System.out.println("Order Placed 1");

							String sub = "Bidding Application : Congratulation : Order Placed for " + p.getPname();
							String body = "Dear <b>" + arr[0] + "</b>";
							body += "<br/><br/>Congratulations<br/><br/>";
							body += "You won the Bid for <b>" + p.getPname() + "</b> at <b>Rs. " + arr[1]
									+ " /-</b><br/>";
							body += "The Seller <b>" + p.getSeller_id()
									+ "</b> will contact you soon to fulfill this order.<br/><br/><br/>";
							body += "Thank you";
							List<String> rec = new ArrayList<>();
							rec.add(arr[0]);
							SendMail mail = new SendMail();
							mail.send(rec, sub, body);

							body = "Dear <b> " + p.getSeller_id() + "</b>";
							body += "<br/><br/>Congratulations<br/><br/>";
							body += "The order has been placed for your product <b>" + p.getPname() + "</b> by <b>"
									+ arr[0] + "</b> at <b>Rs. " + arr[1] + " /-</b><br/>";
							body += "Please contact the Buyer soon and fulfil the order";
							body += "Thank you";
							List<String> to = new ArrayList<>();
							to.add(p.getSeller_id());

							mail.send(to, sub, body);

							System.out.println("Order Placed 2");

						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
