package com.biddingapp.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.biddingapp.daoimpl.DiscussionsDAOImpl;
import com.biddingapp.pojo.DiscussionTopic;
import com.biddingapp.pojo.Responses;
import com.biddingapp.pojo.User;

public class DiscussionsServlet extends HttpServlet {

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
				String type = req.getParameter("type");
				DiscussionsDAOImpl discussionsDAO = new DiscussionsDAOImpl();
				if (type.equals("get")) {
					List<DiscussionTopic> topics = discussionsDAO.getAllTopics();
					Map<String, Integer> count = new HashMap<>();
					for (DiscussionTopic t : topics) {
						count.put(t.getTopicid(), discussionsDAO.getAllResponses(t.getTopicid()).size());
					}
					req.setAttribute("topics", topics);
					req.setAttribute("count", count);
					req.getRequestDispatcher("discussions.jsp").forward(req, resp);
				} else if (type.equals("put")) {
					String sub = req.getParameter("sub");
					String desc = req.getParameter("desc");
					if (sub.trim().length() == 0 || desc.trim().length() == 0) {
						resp.sendRedirect("discussions?type=get&msg=All the fields are mandatory");
					} else {
						DiscussionTopic topic = new DiscussionTopic();
						topic.setCreater(user.getEmail());
						topic.setDescription(desc);
						topic.setSub(sub);
						discussionsDAO.createTopic(topic);
						resp.sendRedirect("discussions?type=get&msg=Created a Ttopic Successfully");
					}
				} else if (type.equals("view_responses")) {
					String topic_id = req.getParameter("topicid");
					DiscussionTopic topic = discussionsDAO.getTopicByID(topic_id);
					List<Responses> responses = discussionsDAO.getAllResponses(topic_id);
					req.setAttribute("responses", responses);
					req.setAttribute("topic", topic);
					req.getRequestDispatcher("discussions_topic_details.jsp").forward(req, resp);
				} else if (type.equals("write_respponse")) {
					String topicid = req.getParameter("topicid");
					String responsetext = req.getParameter("response");
					if (responsetext.trim().length() == 0) {
						resp.sendRedirect(
								"discussions?type=view_responses&msg=Please provide your response&topicid=" + topicid);

					} else {
						Responses response = new Responses();
						response.setResponder(user.getEmail());
						response.setResponsetext(responsetext);
						response.setTopicid(topicid);
						discussionsDAO.writeResponse(response);
						resp.sendRedirect(
								"discussions?type=view_responses&msg=Successfully written the Response&topicid="
										+ topicid);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resp.sendRedirect("error.jsp");
		}
	}

}
