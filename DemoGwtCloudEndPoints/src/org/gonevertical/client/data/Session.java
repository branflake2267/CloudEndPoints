package org.gonevertical.client.data;

import org.gonevertical.client.utils.data.BaseJso;

public class Session extends BaseJso {
  
  public static Session newInstance(String json) {
    return fromJson(json).cast();
  }
  
  protected Session() {
  }
  
  public final native GoogleUser getGoogleUser() /*-{
    return this.user;
  }-*/;
  
}
