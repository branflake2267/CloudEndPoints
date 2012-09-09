package org.gonevertical.client;

import org.gonevertical.client.application.Home;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.ui.RootPanel;

public class DemoGwtCloudEndPoints implements EntryPoint {

  @Override
  public void onModuleLoad() {
    Home home = new Home();
    RootPanel.get().add(home);
  }

}
