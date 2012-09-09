package org.gonevertical.server.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class HomeServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    writeHeader(response);
    writeAd(response);
    writeFooter(response);
  }

  private void writeHeader(HttpServletResponse response) throws IOException {
    String s = "<html><body>";
    response.getWriter().println(s);
  }

  private void writeFooter(HttpServletResponse response) throws IOException {
    String s = "";
    s += "<a href=\"/_ah/api/explorer\">Api Explorer</a><br/>";
    s += "<a href=\"https://democloudpoint.appspot.com/_ah/api/sessionendpoint/v2/session\">Session</a><br/>";
    s += "<a href=\"" + getLoginString() + "\">Login with OAuth</a><br/>";
    s += "</body></html>";
    response.getWriter().println(s);
  }

  private void writeAd(HttpServletResponse response) throws IOException {
    File file = new File("home.txt");
    InputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(file));
      int ch;
      while ((ch = in.read()) != -1) {
        response.getWriter().print((char) ch);
      }
    } finally {
      if (in != null)
        in.close();
    }
  }

  private String getLoginString() {
    String oauthurl = "";
    oauthurl += "https://accounts.google.com/o/oauth2/auth";
    oauthurl += "?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
    oauthurl += "&state=login";
    oauthurl += "&redirect_uri=https%3A%2F%2Fdemocloudpoint.appspot.com%2F_ah%2Fapi%2Fsessionendpoint%2Fv2%2Fsession";
    oauthurl += "&response_type=token";
    oauthurl += "&client_id=734175750239.apps.googleusercontent.com";
    oauthurl += "&approval_prompt=force";
    return oauthurl;
  }
  
}
