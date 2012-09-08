package org.gonevertical.server.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class HomeServlet extends HttpServlet {

  private UserService userService = UserServiceFactory.getUserService();
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    
    String thisUrl = request.getRequestURI();

    writeHeader(response);

    if (userService.isUserLoggedIn()) {
      writeLoggedIn(response, thisUrl);
    } else {
      writeLoggedOut(response, thisUrl);
    }
    
    writeFooter(response);
  }

  private void writeFooter(HttpServletResponse response) throws IOException {
    String s = "<html><body>";
    response.getWriter().println(s);
  }

  private void writeHeader(HttpServletResponse response) throws IOException {
    String s = "";
    s += "<a href=\"/_ah/api/explorer\">Api Explorer</a><br/>";
    s += "<a href=\"/session\">Session</a><br/>";
    s += "</body></html>";
    response.getWriter().println(s);
  }

  private void writeLoggedOut(HttpServletResponse response, String thisUrl) throws IOException {
    String url = userService.createLoginURL(thisUrl);
    String s = "<a href=\"" + url + "\">Log in</a>";
    response.getWriter().println(s);
  }

  private void writeLoggedIn(HttpServletResponse response, String thisUrl) throws IOException {
    String url = userService.createLogoutURL(thisUrl);
    String s = getUserName() + " <a href=\"" + url + "\">Logout</a>";
    response.getWriter().println(s);
  }

  private String getUserName() {
    return userService.getCurrentUser().getNickname();
  }

}
