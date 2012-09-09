package org.gonevertical.client.data;

import org.gonevertical.client.utils.data.BaseJso;

public class GoogleUser extends BaseJso {

  protected GoogleUser() {
  }
  
  public final native String getEmail() /*-{
    return this.email;
  }-*/;
 
  public final native String getAuthDomain() /*-{
    return this.authDomain;
  }-*/;
  
  public final native long getUserId() /*-{
    return this.userId;
  }-*/;
  
  public final native String getFederatedIdentity() /*-{
    return this.federatedIdentity;
  }-*/;
  
}
