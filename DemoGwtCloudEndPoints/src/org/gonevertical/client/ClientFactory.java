package org.gonevertical.client;

import org.gonevertical.client.data.Session;
import org.gonevertical.client.utils.QueryStringData;
import org.gonevertical.client.utils.QueryStringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class ClientFactory {

  private String access_token;
  private Session session;
  
  public ClientFactory() {
    initAccessToken();
  }

  public String getAccessToken() {
    return access_token;
  }
  
  public boolean isLoggedIn() {
    return access_token != null;
  }
  
  private void initAccessToken() {
    QueryStringData qsd = QueryStringUtils.getQueryStringData();
    access_token = qsd.getParameter_String("access_token");
  }
  
  public String getLoginString() {
    String thisurl = GWT.getHostPageBaseURL() + Window.Location.getQueryString();
    
    String url = URL.encode(thisurl);
    
    String oauthurl = "";
    oauthurl += "https://accounts.google.com/o/oauth2/auth";
    oauthurl += "?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";
    oauthurl += "&state=login";
    oauthurl += "&redirect_uri=" + url;
    oauthurl += "&response_type=token";
    oauthurl += "&client_id=734175750239.apps.googleusercontent.com";
    oauthurl += "&approval_prompt=force";
    return oauthurl;
  }

  public void setSession(Session session) {
    this.session = session;
  }
  
}
