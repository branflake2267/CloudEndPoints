package org.gonevertical.client.data;

import org.gonevertical.client.utils.data.BaseJso;
import org.gonevertical.client.utils.data.RequestHandler;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class Session extends BaseJso {

  public static void getRequest(String access_token, final RequestHandler<Session> handler) {
    String url = "https://democloudpoint.appspot.com/_ah/api/sessionendpoint/v2/session";
    
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
    builder.setHeader("Content-Type", "application/json");
    builder.setHeader("Accept", "application/json");
    builder.setHeader("Authorization", "Bearer " + access_token);

    try {
      builder.sendRequest(null, new RequestCallback() {
        public void onResponseReceived(Request request, Response response) {
          if (response.getStatusCode() == 200) {
            String json = response.getText();
            System.out.println("json=" + json);
            if (json.contains("exception")) {
              handler.onFailure(new Exception("Server exception"));
            } else {
              Session session = Session.newInstance(json);
              handler.onSuccess(session);
            }
          } else {
            handler.onFailure(new Exception("Error fetching data"));
          }
        }
        public void onError(Request request, Throwable e) {
          handler.onFailure(e);
        }
      });
    } catch (RequestException e) {
      handler.onFailure(e);
    }
  }

  public static Session newInstance(String json) {
    return fromJson(json).cast();
  }
  
  protected Session() {
  }
  
  public final native GoogleUser getGoogleUser() /*-{
    return this.user;
  }-*/;
  
}
