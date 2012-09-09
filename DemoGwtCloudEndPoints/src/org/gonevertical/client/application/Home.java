package org.gonevertical.client.application;

import org.gonevertical.client.utils.QueryStringData;
import org.gonevertical.client.utils.QueryStringUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class Home extends Composite {

  interface HomeUiBinder extends UiBinder<Widget, Home> {
  }
  
  private static HomeUiBinder uiBinder = GWT.create(HomeUiBinder.class);
  @UiField Button login;
  @UiField Button session;

  public Home() {
    initWidget(uiBinder.createAndBindUi(this));
    
    if (getAccessToken() != null) {
      session.setVisible(true);
      login.setVisible(false);
    } else {
      session.setVisible(false);
      login.setVisible(true);
    }
  }

  @UiHandler("login")
  void onLoginClick(ClickEvent event) {
    login();
  }
  
  @UiHandler("session")
  void onSessionClick(ClickEvent event) {
    getSession();
  }

  private void login() {
    Window.Location.assign(getLoginString());
  }
  
  private String getLoginString() {
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
  
  private void getSession() {
    String token = getAccessToken();
    
    String url = "https://democloudpoint.appspot.com/_ah/api/sessionendpoint/v2/session";
    
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    builder.setHeader("Content-Type", "application/json");
    builder.setHeader("Accept", "application/json");
    builder.setHeader("Authorization", "Bearer " + token);
    
    try {
      builder.sendRequest(null, new RequestCallback() {
        public void onResponseReceived(Request request, Response response) {
          if (response.getStatusCode() == 200) {
            System.out.println("response=" + response.getText());
          }
        }
        public void onError(Request request, Throwable exception) {
          exception.printStackTrace();
        }
      });
    } catch (RequestException e) {
      e.printStackTrace();
    }
  }
  
  private String getAccessToken() {
    QueryStringData qsd = QueryStringUtils.getQueryStringData();
    String token = qsd.getParameter_String("access_token");
    return token;
  }

  private void insertUser() {
    String url = "https://democloudpoint.appspot.com/_ah/api/userendpoint/v1/user/insert";
    String requestData = "{ \"nameFirst\": \"Brandon\" }";
    
    RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
    builder.setHeader("Content-Type", "application/json");
    builder.setHeader("Accept", "application/json");
    try {
      builder.sendRequest(requestData, new RequestCallback() {
        public void onResponseReceived(Request request, Response response) {
          if (response.getStatusCode() == 200) {
            System.out.println("response=" + response.getText());
          }
        }
        public void onError(Request request, Throwable exception) {
          exception.printStackTrace();
        }
      });
    } catch (RequestException e) {
      e.printStackTrace();
    }
  }
  
}
