package com.sam.h2proj.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.sam.h2proj.model.User;
import com.sam.h2proj.model.service.UserService;
import com.sam.h2proj.util.Const;
import com.sam.h2proj.util.Page;

/**
 * Restful API DEMO
 * 
 * GET    /User      取得User清單
 * GET    /User/{id} 取得指定User
 * POST   /User      新增User
 * PUT    /User/{id} 更新指定User
 * DELETE /USER/{id} 刪除指定User
 * 
 */
@WebServlet({"/User","/User/*"})
public class UserServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private UserService userService;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
    }
    
    private void initSpringContext() {
    	if(this.userService == null) {
    		ApplicationContext context = (ApplicationContext) getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    		this.userService = (UserService) context.getBean("userService");
    		
    		System.out.println("Init user service");
    	}
    }
    
    private Map<String,Object> getParamMap(HttpServletRequest request){
    	Map<String,Object> paramMap = new HashMap<>();
		
		Enumeration<String> e = request.getParameterNames();
		while(e.hasMoreElements()) {
		    String param = e.nextElement();
		    String value = request.getParameter(param);
		    
		    paramMap.put(param, value);
		}
		
		return paramMap;
    }
    
    private String getJsonString(Object bean) {
    	String result = "";
    	
    	if(bean == null)
    		return new JSONObject().toString();
    	
    	// 陣列須轉換成JSONArray輸出Json字串才會正常
    	if(bean instanceof Collection) {
    		result = new JSONArray((Collection) bean).toString();
    	} else if(bean instanceof Map) {
    		result = new JSONObject((Map) bean).toString();
    	} else {
    		result = new JSONObject(bean).toString();
    	}
    	
    	return result;
    }
    
    /**
     * 檢查參數是否含有分頁所需參數 pageSize & pageNum
     * @param paramMap
     * @return 是否含有分頁所需參數
     */
    private boolean hasPageInfo(Map<String,Object> paramMap) {
    	return paramMap.containsKey(Const.PAGE_NUM) && paramMap.containsKey(Const.PAGE_SIZE);
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initSpringContext();
		
		// 設定回傳為Json型別
		response.setContentType("application/json");
		
		String pathInfo = request.getPathInfo();
		
		// For URL mapping GET /User : 查詢User清單(可給複數參數)
		if(pathInfo == null) {
			
			Map<String,Object> paramMap = getParamMap(request);
			String resultJsonStr = "";
			
			if(hasPageInfo(paramMap)) {
				// 含有分頁參數, 回傳帶有分頁訊息的清單資料 (部分輸出, 依據指定的每業資料筆數以及頁碼而定)
				Page<User> resultList = userService.getAllUserWithPages(paramMap);
				resultJsonStr = getJsonString(resultList);
			} else {
				// 不含分頁參數, 回傳單純的清單資料 (全輸出, 查到多少輸出多少)
				List<User> resultList = userService.getAllUser(paramMap);
				resultJsonStr = getJsonString(resultList);
			}
			
			response.getWriter().append(resultJsonStr);
			
			return;
		} else if(pathInfo.lastIndexOf("/") == 0 && pathInfo.length() > 1) {
			
			// For URL mapping GET /User/{id} : 查詢特定Id的User
			Integer id = Integer.valueOf(pathInfo.substring(1));
			User result = userService.getUser(id);
			
			response.getWriter().append(getJsonString(result));
			
			return;
		}
		
		response.getWriter().append(getJsonString(Collections.singletonMap("msg", "invalid query")));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initSpringContext();
		
		// 設定回傳為Json型別
		response.setContentType("application/json");
		
		String pathInfo = request.getPathInfo();
		
		// For URL mapping POST /User : 新增User
		if(pathInfo == null) {
			User user = new User();
			user.setUsername(request.getParameter("username"));
			user.setPassword(request.getParameter("password"));
			
			userService.addUser(user);
			
			response.getWriter().write(getJsonString(Collections.singletonMap("msg", "insert success")));
			
			return;
		}
		
		response.getWriter().append(getJsonString(Collections.singletonMap("msg", "invalid insert")));
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initSpringContext();
		
		// 設定回傳為Json型別
		response.setContentType("application/json");
		
		String pathInfo = request.getPathInfo();
		
		// For URL mapping POST /User/{id} : 更新User
		if(pathInfo.lastIndexOf("/") == 0 && pathInfo.length() > 1) {
			Map<String,String> paramMap = getParameterMap(request);
			
			Integer id = Integer.valueOf(pathInfo.substring(1));
			
			User user = new User();
			user.setId(id);
			user.setUsername(paramMap.get("username"));
			user.setPassword(paramMap.get("password"));
			
			userService.updateUser(user);
			
			response.getWriter().append(getJsonString(Collections.singletonMap("msg", "update success")));
			
			return;
		}
		
		response.getWriter().append(getJsonString(Collections.singletonMap("msg", "invalid update")));
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initSpringContext();
		
		// 設定回傳為Json型別
		response.setContentType("application/json");
		
		String pathInfo = request.getPathInfo();
		
		// For URL mapping DELETE /User/{id} : 刪除User
		if(pathInfo.lastIndexOf("/") == 0 && pathInfo.length() > 1) {
			Integer id = Integer.valueOf(pathInfo.substring(1));
			
			userService.deleteUser(id);
			
			response.getWriter().append(getJsonString(Collections.singletonMap("msg", "delete success")));
			
			return;
		}
		
		response.getWriter().append(getJsonString(Collections.singletonMap("msg", "invalid delete")));
	
	}
	
	// 可解決PUT/DELTE Mapping在Servlet中抓不到參數值的問題
	private Map<String, String> getParameterMap(HttpServletRequest request) {

	    BufferedReader br = null;
	    Map<String, String> dataMap = new HashMap<>();

	    try {

	        InputStreamReader reader = new InputStreamReader(
	                request.getInputStream());
	        br = new BufferedReader(reader);

	        String data = br.readLine();

	        String[] dataSets = data.split("&");

	        for(String dataSet : dataSets) {
	        	String param = dataSet.substring(0, dataSet.indexOf("="));
	        	String value = dataSet.substring(dataSet.indexOf("=") + 1);
	        	
	        	dataMap.put(param, value);
	        }
	        
	        return dataMap;
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException ex) {
	            	ex.printStackTrace();
	            }
	        }
	    }

	    return dataMap;
	}

}
