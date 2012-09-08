package org.gonevertical.server;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.sun.net.ssl.HttpsURLConnection;

public class TestUtils {
  
  private String charset = "UTF-8";

  public String getRequest(String url) {
    String content = "";
    try {
      URL urlo = new URL(url);
      URLConnection conn = urlo.openConnection();
      conn.setDoInput(true); 
      conn.setDoOutput(true);
      conn.setRequestProperty("Content-Type", "application/json");
      
      InputStream in = conn.getInputStream();
      int ch;
      while (((ch = in.read()) != -1)) {
        content += (char) ch;
      }
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    
    System.out.println("url=" + url + " content=" + content);
    
    return content;
  }

  public String postRequest(String url, String json) {
    String content = "";
    try {
      URL urlo = new URL(url);
      URLConnection conn = urlo.openConnection();
      conn.setDoOutput(true);
      conn.setRequestProperty("Accept-Charset", charset);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");
      
      OutputStream output = null;
      try {
        output = conn.getOutputStream();
        output.write(json.getBytes(charset));
      } finally {
        if (output != null)
          try {
            output.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
      
      InputStream in = conn.getInputStream();
      int ch;
      while (((ch = in.read()) != -1)) {
        content += (char) ch;
      }
      in.close();
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
    
    System.out.println("url=" + url + " content=" + content);
    return content;
  }

}
