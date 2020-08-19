package com.biddingapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biddingapp.dao.UserDAO;
import com.biddingapp.daoimpl.UserDAOImpl;
import com.biddingapp.pojo.User;

public class MembershipServlet extends HttpServlet {

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
			UserDAO dao = new UserDAOImpl();

			if (user == null) {
				resp.sendRedirect("login.jsp?msg=Session Expired. Please login again");
			} else {
				String type = req.getParameter("type");
				if (type.equals("get")) {
					req.setAttribute("users", dao.getPendingUsers());
					req.getRequestDispatcher("admin_membership.jsp").forward(req, resp);
				} else if (type.equals("approve")) {
					String email = req.getParameter("email");
					String role = req.getParameter("role");
					dao.approveProfile(email, role);
					resp.sendRedirect("membership?type=get");
					
				} else if (type.equals("reject")) {
					String email = req.getParameter("email");			
					dao.deleteProfile(email);
					resp.sendRedirect("membership?type=get");

				} else if (type.equals("get_all")) {
					req.setAttribute("users", dao.getAllUsers());
					req.getRequestDispatcher("admin_all_users.jsp").forward(req, resp);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
