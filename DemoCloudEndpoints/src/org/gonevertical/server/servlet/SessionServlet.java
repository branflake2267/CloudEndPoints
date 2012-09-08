package org.gonevertical.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class SessionServlet extends HttpServlet {

  private UserService userService = UserServiceFactory.getUserService();
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    
    String thisUrl = request.getRequestURI();
   
    if (userService.isUserLoggedIn()) {
      writeLoggedIn(response, thisUrl);
    } else {
      writeLoggedOut(response, thisUrl);
    }
  }

  private void writeLoggedOut(HttpServletResponse response, String thisUrl) throws IOException {
    String url = userService.createLoginURL(thisUrl);
    String s = "{ \"login\": \"" + url + "\" }";
    response.getWriter().println(s);
  }

  private void writeLoggedIn(HttpServletResponse response, String thisUrl) throws IOException {
    String url = userService.createLoginURL(thisUrl);
    String s = "{ \"logout\": \"" + url + "\", \"googleuser\" : " + getGoogleUser() + " }";
    response.getWriter().println(s);
  }

  private String getGoogleUser() {
    User user = getUser();
    JSONObject jso = new JSONObject(user);
    return jso.toString();
  }

  private User getUser() {
    return userService.getCurrentUser();
  }

}
