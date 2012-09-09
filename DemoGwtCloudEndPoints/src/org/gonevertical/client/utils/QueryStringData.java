package org.gonevertical.client.utils;

import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QueryStringData implements IsSerializable {

  /**
   * like domain.tld#[historyToken]?params1=a&params2=b
   */
  private String historyToken;
  
  /**
   * like domain.tld#historyToken?[?params1=a&params2=b]
   */
  private HashMap<String, String> parameters;
  
  /** 
   * use this just incase of future serialization
   */
  public QueryStringData() {
  }
  
  public QueryStringData(String historyToken) {
    this.historyToken = historyToken;
  }
  
  public QueryStringData(String historyToken, HashMap<String, String> parameters) {
    this.historyToken = historyToken;
    this.parameters = parameters;
  }
  
  public void setQueryStringData(String historyToken, HashMap<String, String> parameters) {
    this.historyToken = historyToken;
    this.parameters = parameters;
  }
  
  /**
   * get historyToken domain.tld#[historyToken]?param1=a&param2=b
   * @return
   */
  public String getHistoryToken() {
    return historyToken;
  }
  
  public String getHistoryTokenWithParameters() {
    
    String s = "";
    if (historyToken != null) {
      s += historyToken;
    }
    
    if (parameters != null && parameters.size() > 0) {
      s += QueryStringUtils.PARAMSSTART;
      s += getParametersConcatenated();
    }
    
    return s;
  }
  
  /**
   * get the first part of the historyToken until the underscore
   * like domain.tld#[historyToken]_home?param1=a
   * @return
   */
  public String getHistoryToken_ToFirstUnderScore() {
  	if (historyToken.matches("^.*" + QueryStringUtils.PARAMSSTART + "_.*") == false) {
  		return null;
  	}
  	String[] split = historyToken.split("_");
  	String s = null;
  	if (split != null) {
  		s = split[0];
  	}
    return s;
  }
  
  public String[] getHistoryToken_byUnderScore() {
  	if (historyToken.contains("_") == false) {
  		String[] s = new String[1];
  		s[0] = historyToken;
  		return s;
  	}
  	String[] s = historyToken.split("_");
    return s;
  }
  
  public HashMap<String, String> getParameters() {
    return this.parameters;
  }
  
  /**
   * get query string parameter and return a integer
   * 
   * @param key
   * @return
   */
  public Integer getParameter_Int(String key) {

    if (parameters == null) {
      return null;
    }

    if (key == null || key.length() == 0) {
      return null;
    }

    String s = parameters.get(key);
    if (s == null) {
      return null;
    }

    Integer i = null;
    if (s.toString().length() > 0) {
      try {
        i = Integer.parseInt(s.toString());
      } catch (NumberFormatException e) {
        i = null;
      }
    }

    return i;
  }
  
  public Long getParameter_Long(String key) {

    if (parameters == null) {
      return null;
    }

    if (key == null || key.length() == 0) {
      return null;
    }

    String s = parameters.get(key);
    if (s == null) {
      return null;
    }

    Long l = null;
    if (s.toString().length() > 0) {
      try {
        l = Long.parseLong(s.toString());
      } catch (NumberFormatException e) {
        l = null;
      }
    }

    return l;
  }

  /**
   * get a query string parameter and return a String
   * 
   * @param parameters
   * @param key
   * @return
   */
  public String getParameter_String(String key) {

    if (parameters == null) {
      return null;
    }

    if (key == null || key.length() == 0) {
      return null;
    }

    String s = null;
    if (parameters.get(key) != null && parameters.get(key).toString().length() > 0) {
      s = parameters.get(key).toString().trim();
    }

    return s;
  }
  
  /**
   * get HashMap parameters in url concatenated form, ready for url
   * 
   * like [param1=a&param2=b&param3=c]
   * 
   * @return 
   */
  public String getParametersConcatenated() {

    if (parameters == null) {
      return null;
    }

    String qs = "";

    Iterator<String> iterator = parameters.keySet().iterator();
    int i = 0;
    while (iterator.hasNext()) {

      String key = iterator.next().toString();

      qs += key + QueryStringUtils.PARAMVALUESPLITTER + parameters.get(key).toString();

      if (i < parameters.size() - 1) {
        qs += QueryStringUtils.PARAMSPLITTER;
      }
      i++;
    }
    
    if (qs.trim().length() == 0) {
      qs = null;
    }

    return qs;
  }
  
}
