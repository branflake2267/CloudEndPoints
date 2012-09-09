package org.gonevertical.server.data;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class Session {

  private com.google.appengine.api.users.User user;

  public com.google.appengine.api.users.User getUser() {
    return user;
  }

  public void setUser(com.google.appengine.api.users.User user) {
    this.user = user;
  }
  
}
