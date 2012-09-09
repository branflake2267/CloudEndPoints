package org.gonevertical.client.data;

import org.gonevertical.client.utils.data.BaseJso;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class User extends BaseJso {
  
  public static void insert() {
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
