package com.biddingapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biddingapp.dao.ProductDAO;
import com.biddingapp.daoimpl.OrderDAOImpl;
import com.biddingapp.daoimpl.ProductDAOImpl;
import com.biddingapp.pojo.Order;
import com.biddingapp.pojo.Product;
import com.biddingapp.pojo.User;

public class OrderServlet extends HttpServlet {

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
			OrderDAOImpl orderDAO = new OrderDAOImpl();

			if (user == null) {
				resp.sendRedirect("login.jsp?msg=Session Expired. Please login again.");
			} else {
				String type = req.getParameter("type");
				if (type.equals("seller_get")) {
					List<Order> orders = orderDAO.getPlacedOrdersForSeller(user.getEmail());
					Map<String, Product> products = new HashMap<>();
					for (Order o : orders) {
						products.put(o.getPid(), productDAO.getProduct(o.getPid()));
					}
					req.setAttribute("orders", orders);
					req.setAttribute("products", products);
					req.getRequestDispatcher("seller_orders.jsp").forward(req, resp);
				} else if (type.equals("buyer_get")) {
					List<Order> orders = orderDAO.getPlacedOrdersForBuyer(user.getEmail());
					Map<String, Product> products = new HashMap<>();
					for (Order o : orders) {
						products.put(o.getPid(), productDAO.getProduct(o.getPid()));
					}
					req.setAttribute("orders", orders);
					req.setAttribute("products", products);
					req.getRequestDispatcher("buyer_orders.jsp").forward(req, resp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
