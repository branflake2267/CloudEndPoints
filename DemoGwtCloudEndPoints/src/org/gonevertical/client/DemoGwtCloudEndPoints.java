package org.gonevertical.client;

import org.gonevertical.client.application.Home;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class DemoGwtCloudEndPoints implements EntryPoint {

  private ClientFactory clientFactory = new ClientFactory();
  
  @Override
  public void onModuleLoad() {
    Home home = new Home();
    home.setClientFactory(clientFactory);
    RootPanel.get("content").add(home);
  }

}
