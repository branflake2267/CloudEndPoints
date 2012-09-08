package org.gonevertical.server;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

public class UserEndpointLocalTest extends TestUtils {

  /**
   * curl -H 'Content-Type: application/json' -d '{ "nameFirst": "Brandon", "nameLast": "Donnelson" }' http://localhost:8888/_ah/api/userendpoint/v1/user/insert
   * curl -H 'Content-Type: application/json' -d '{ "nameFirst": "Brandon", "nameLast": "Donnelson" }' https://democloudpoint.appspot.com/_ah/api/userendpoint/v1/user/insert
   */
  @Test
  public void testInsert() {
    String url = "http://localhost:8888/_ah/api/userendpoint/v1/user/insert";
    String json = "{ \"nameFirst\": \"Brandon\", \"nameLast\": \"Donnelson\" }";
    String content = postRequest(url, json);
    assertTrue(content.contains("\"id\" :"));
  }
  
  /**
   * curl http://localhost:8888/_ah/api/userendpoint/v1/user/list
   * curl https://democloudpoint.appspot.com/_ah/api/userendpoint/v1/user/list
   */
  @Test
  public void testList() {
    String content = getRequest("http://localhost:8888/_ah/api/userendpoint/v1/user/list?limit=2");
    assertTrue(content.contains("\"items\" :"));
  }
  
  @Test
  public void testGet() {
    String url = "http://localhost:8888/_ah/api/userendpoint/v1/user/insert";
    String json = "{ \"nameFirst\": \"Brandon\" }";
    String content = postRequest(url, json);
    
    JSONObject jso = null;
    try {
      jso = new JSONObject(content);
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }
    
    Long id = -1l;
    try {
      id = jso.getLong("id");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    
    url = "http://localhost:8888/_ah/api/userendpoint/v1/user/get/" + id;
    content = getRequest(url);
    assertTrue(content.contains("\"id\" :"));
  }
  
  @Test
  public void testUpdate() {
    String url = "http://localhost:8888/_ah/api/userendpoint/v1/user/insert";
    String json = "{ \"nameFirst\": \"Brandon\" }";
    String content = postRequest(url, json);
    
    JSONObject jso = null;
    try {
      jso = new JSONObject(content);
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }
    
    url = "http://localhost:8888/_ah/api/userendpoint/v1/user/update";
    try {
      jso.put("nameLast", "Donnelson");
    } catch (JSONException e1) {
      e1.printStackTrace();
      fail();
    }
    
    json = jso.toString();
    
    content = postRequest(url, json);
    
    try {
      JSONObject jso2 = new JSONObject(content);
      String nameFirst = jso2.getString("nameFirst");
      String nameLast = jso2.getString("nameLast");
      assertEquals("Brandon", nameFirst);
      assertEquals("Donnelson", nameLast);
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }
  }
  
  @Test
  public void testSearch() {
    String content = getRequest("http://localhost:8888/_ah/api/userendpoint/v1/user/search/brandon");
    assertTrue(content.contains("\"items\" :"));
  }
  
  @Test
  public void testRemove() {
    String url = "http://localhost:8888/_ah/api/userendpoint/v1/user/insert";
    String json = "{ \"nameFirst\": \"Brandon\" }";
    String content = postRequest(url, json);
    
    JSONObject jso = null;
    try {
      jso = new JSONObject(content);
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }
    
    long id = 0;
    try {
      id = jso.getLong("id");
    } catch (JSONException e) {
      e.printStackTrace();
      fail();
    }
    
    url = "http://localhost:8888/_ah/api/userendpoint/v1/user/remove/" + id;
    content = getRequest(url);
    assertTrue(content.contains(Long.toString(id)));
  }
  
}
