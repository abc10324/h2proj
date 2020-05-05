package com.sam.h2proj.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.sam.h2proj.model.User;
import com.sam.h2proj.model.service.UserService;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/HelloWorld")
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HelloServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationContext context = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		UserService userService = (UserService) context.getBean("userService");
		
		User newUser = new User();
		newUser.setUsername("abc10324");
		newUser.setPassword("password");
		
		userService.addUser(newUser);
		
		System.out.println("save user done");
		
		List<User> resultList = userService.getAllUser();
		
		for(User user : resultList) {
			System.out.println("user = " + user);
		}
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("username", "abc10324");

		System.out.println("All round search");
		
		resultList = userService.getAllUser(paramMap);
		
		for(User user : resultList) {
			System.out.println("user = " + user);
		}
		response.getWriter().append("Served at: ").append(request.getContextPath()).append("\r\n Hello world");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
