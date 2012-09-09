package org.gonevertical.server.endpoints;

import org.gonevertical.server.data.Session;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Api(name = "sessionendpoint", 
     version = "v2")
public class SessionEndpoint {

  @ApiMethod(clientIds = { "AIzaSyCCTPZtSIh59yZl3ZfRS-2U8tp6DWoWF9g" } ,
             scopes = { "https://www.googleapis.com/auth/userinfo.email" })
  public Session getSession(User user) {
    Session session = new Session();
    session.setUser(user);
    return session;
  }

}
