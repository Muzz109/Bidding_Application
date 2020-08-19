package com.biddingapp.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
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
import com.biddingapp.util.Constants;

public class ProductServlet extends HttpServlet {
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
			String type = req.getParameter("type");
			ProductDAO productDAO = new ProductDAOImpl();
			FeedbackDAO feedbackDAO = new FeedbackDAO();

			if (user == null) {
				resp.sendRedirect("login.jsp?msg=Session Expired. Please login again");
			} else {
				String role = user.getRole();
				if (type.equals("seller_products_get")) {
					if (role.equals(Constants.ROLE_SELLER)) {
						List<Product> products = productDAO.getproducts(user.getEmail(), Constants.ROLE_SELLER);
						req.setAttribute("products", products);
						Map<String, Map<String, List<String>>> feedbacks = new LinkedHashMap<>();
						for (Product p : products) {
							feedbacks.put(p.getPid(), feedbackDAO.getFeedbackByPid(p.getPid()));
						}
						req.setAttribute("feedbacks", feedbacks);
						req.getRequestDispatcher("seller_products_get.jsp").forward(req, resp);
					} else {
						resp.sendRedirect(
								"welcome.jsp?msg=Error! You don't have the required privileges to perform this action");
					}
				} else if (type.equals("seller_products_add")) {
					Product product = new Product();
					String deadline = req.getParameter("deadline");
					System.out.println(deadline);
					String sellerid = user.getEmail();
					String pname = req.getParameter("pname");
					String pdesc = req.getParameter("pdesc");
					String imageURL = req.getParameter("imageURL");
					String baseprice = req.getParameter("baseprice");

					if (pname == null || pname.trim().length() == 0 || pdesc == null || pdesc.trim().length() == 0
							|| deadline == null || deadline.trim().length() == 0 || baseprice == null
							|| baseprice.trim().length() == 0) {
						resp.sendRedirect("seller_products_add.jsp?msg=Error! Please fill all the mandatory fields.");
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

						product.setDeadline(new Timestamp(sdf.parse(deadline).getTime()));
						product.setPbaseprice(Double.parseDouble(baseprice));
						product.setPdesc(pdesc);
						product.setPimageurl(imageURL);
						product.setPname(pname);
						product.setSeller_id(sellerid);
						product.setStatus(Constants.STATUS_PENDING);
						productDAO.addProduct(product);
						resp.sendRedirect("seller_products_add.jsp?msg=Added a new Product");
					}

				} else if (type.equals("admin_products_get")) {
					if (role.equals(Constants.ROLE_ADMIN)) {
						req.setAttribute("products", productDAO.getproducts());
						req.getRequestDispatcher("admin_products_get.jsp").forward(req, resp);
					} else {
						resp.sendRedirect(
								"welcome.jsp?msg=Error! You don't have the required privileges to perform this action");
					}
				} else if (type.equals("admin_product_aprrove")) {
					if (role.equals(Constants.ROLE_ADMIN)) {
						productDAO.approveProduct(req.getParameter("pid"));

						req.setAttribute("products", productDAO.getproducts());
						req.getRequestDispatcher("admin_products_get.jsp?msg=Approved the Product").forward(req, resp);
					} else {
						resp.sendRedirect(
								"welcome.jsp?msg=Error! You don't have the required privileges to perform this action");
					}

				} else if (type.equals("admin_product_reject")) {
					if (role.equals(Constants.ROLE_ADMIN)) {
						productDAO.rejectProduct(req.getParameter("pid"));

						req.setAttribute("products", productDAO.getproducts());
						req.getRequestDispatcher("admin_products_get.jsp?msg=Rejected the Product").forward(req, resp);
					} else {
						resp.sendRedirect(
								"welcome.jsp?msg=Error! You don't have the required privileges to perform this action");
					}

				} else if (type.equals("buyer_products_get")) {
					if (role.equals(Constants.ROLE_BUYER)) {
						List<Product> products = productDAO.getApprovedproducts();
							
						req.setAttribute("products", products);
						req.getRequestDispatcher("buyer_products_get.jsp").forward(req, resp);
					} else {
						resp.sendRedirect(
								"welcome.jsp?msg=Error! You don't have the required privileges to perform this action");
					}
				} else if (type.equals("seller_readd")) {
					String pid = req.getParameter("pid");
					String deadline = req.getParameter("deadline");
					String baseprice = req.getParameter("baseprice");
					if (deadline.trim().length() == 0 || baseprice.trim().length() == 0) {
						resp.sendRedirect(
								"products?msg=Please enter the base price and the deadline&type=seller_products_get");
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						productDAO.readd(pid, Double.parseDouble(baseprice),
								new Timestamp(sdf.parse(deadline).getTime()));
						resp.sendRedirect(
								"products?msg=Re-Added the Product&type=seller_products_get");

					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
