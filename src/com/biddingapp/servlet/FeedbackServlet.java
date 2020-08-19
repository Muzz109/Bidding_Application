package com.biddingapp.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biddingapp.dao.ProductDAO;
import com.biddingapp.daoimpl.FeedbackDAO;
import com.biddingapp.daoimpl.ProductDAOImpl;
import com.biddingapp.pojo.Product;
import com.biddingapp.pojo.User;

public class FeedbackServlet extends HttpServlet {

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
			if (user == null) {
				resp.sendRedirect("login.jsp?msg=Session Expired. Please login again");
			} else {
				FeedbackDAO feedbackDAO = new FeedbackDAO();
				ProductDAO productDAO = new ProductDAOImpl();
				String type = req.getParameter("type");
				if (type.equals("buyer_feedback_get")) {
					String pid = req.getParameter("pid");
					Map<String, List<String>> feedbacks = feedbackDAO.getFeedbackByPid(pid);
					Product product = productDAO.getProduct(pid);
					req.setAttribute("feedbacks", feedbacks);
					req.setAttribute("product", product);
					req.getRequestDispatcher("buyer_feedback_get.jsp").forward(req, resp);
				} else if (type.equals("buyer_feedback_write")) {
					String pid = req.getParameter("pid");
					String feedback = req.getParameter("feedback");
					if (feedback == null || feedback.trim().length() < 100) {
						resp.sendRedirect("feedback?type=buyer_feedback_get&pid=" + pid
								+ "&msg=Feedback must have minimum of 100 characters");
					} else {
						feedbackDAO.writeFeedback(user.getEmail(), pid, feedback);
						resp.sendRedirect("feedback?type=buyer_get_my_feedback&msg=Done writing the feedback. Thank you");
					}
				} else if (type.equals("buyer_get_my_feedback")) {
					Map<String, String> feedbacks = feedbackDAO.getFeedbackByBuyerID(user.getEmail());
					Iterator<String> it = feedbacks.keySet().iterator();
					Map<String, Product> products = new HashMap<>();
					Map<String, List<String>> feedback = new HashMap<>();
					while (it.hasNext()) {
						String pid = it.next();
						List<String> fbs = new ArrayList<>();
						if (feedback.containsKey(pid.trim())) {
							fbs = feedback.get(pid.trim());
							fbs.add(feedbacks.get(pid));
						} else {
							fbs.add(feedbacks.get(pid));
						}
						feedback.put(pid.trim(), fbs);
						products.put(pid, productDAO.getProduct(pid.trim()));
					}
					
					
					req.setAttribute("feedbacks", feedback);
					req.setAttribute("products", products);
					req.getRequestDispatcher("buyer_get_my_feedback.jsp").forward(req, resp);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
