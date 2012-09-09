package org.gonevertical.client.application;

import org.gonevertical.client.ClientFactory;
import org.gonevertical.client.data.Session;
import org.gonevertical.client.data.requests.SessionRequest;
import org.gonevertical.client.utils.data.RequestHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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
  
  private ClientFactory clientFactory;

  public Home() {
    initWidget(uiBinder.createAndBindUi(this));
  }
  
  public void setClientFactory(ClientFactory clientFactory) {
    this.clientFactory = clientFactory;
    
    login.setVisible(!clientFactory.isLoggedIn());
    session.setVisible(clientFactory.isLoggedIn());
  }

  @UiHandler("login")
  void onLoginClick(ClickEvent event) {
    login();
  }
  
  @UiHandler("session")
  void onSessionClick(ClickEvent event) {
    SessionRequest.getRequest(clientFactory.getAccessToken(), new RequestHandler<Session>() {
      @Override
      public void onSuccess(Session session) {
        clientFactory.setSession(session);
        
        Window.alert(session.toJson());
      }
      
      @Override
      public void onFailure(Throwable exception) {
        exception.printStackTrace();
      }
    });
  }

  private void login() {
    Window.Location.assign(clientFactory.getLoginString());
  }
  
}
