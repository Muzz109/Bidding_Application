package com.biddingapp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biddingapp.daoimpl.DiscussionsDAOImpl;

public class ManageDFServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			DiscussionsDAOImpl dao = new DiscussionsDAOImpl();

			String type = req.getParameter("type");
			if (type == null) {

				req.setAttribute("topics", dao.getAllTopics());
				req.getRequestDispatcher("admin_mdf.jsp").forward(req, resp);
			} else if (type.equals("delete")) {
				String id = req.getParameter("id");
				dao.deleteTopic(id);
				req.setAttribute("topics", dao.getAllTopics());
				req.getRequestDispatcher("admin_mdf.jsp").forward(req, resp);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
