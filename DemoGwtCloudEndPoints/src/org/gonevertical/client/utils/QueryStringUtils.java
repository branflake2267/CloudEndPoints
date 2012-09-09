package org.gonevertical.client.utils;

import java.util.HashMap;

import com.google.gwt.user.client.History;

public class QueryStringUtils {
  
  /**
   * notes represent this as ?
   */
  public static final String PARAMSSTART = ""; 
  
  /**
   * instead of &, will make it ::
   */
  public static final String PARAMSPLITTER = "&"; 
  
  /**
   * instead of = will make it :
   */
  public static final String PARAMVALUESPLITTER = "="; 

  /**
   * constructor
   */
  public QueryStringUtils() {
  }

  /**
   * get query string & parameters anywhere in an app
   * 
   * @return
   */
  public static QueryStringData getQueryStringData() {
    
    String historytoken = getHistoryToken_Anchor(History.getToken());
    HashMap<String, String> params = getHistoryToken_Parameters(History.getToken());
    
    QueryStringData qsd = new QueryStringData();
    qsd.setQueryStringData(historytoken, params);

    return qsd;
  }
  
  public static String getToken() {
    String historytoken = getHistoryToken_Anchor(History.getToken());
    if (historytoken == null) {
      historytoken = "";
    }
    return historytoken;
  }
  
  public static void newItem(String historyToken, boolean issueEvent) {
    History.newItem(historyToken, issueEvent);
  }
  
  public static void newItem(String historyToken, HashMap<String,String> params, boolean issueEvent) {
    
    QueryStringData qsd = new QueryStringData(historyToken, params);
    String ht = qsd.getHistoryTokenWithParameters();
    
    History.newItem(ht, issueEvent);
  }
  
  /**
   * set new parameters to url
   * @param params
   */
  public static void newItem(HashMap<String,String> params, boolean issueEvent) {
    QueryStringData oldQsd = getQueryStringData();
    
    QueryStringData newQsd = new QueryStringData(oldQsd.getHistoryToken(), params);
    String ht = newQsd.getHistoryTokenWithParameters();
    
    // set it to url
    History.newItem(ht, issueEvent);
  }
  

  /**
   * get historyToken by itself
   * 
   * like domain.tld#[historyToken]?params1=a&params2=b
   * 
   * @param historyToken
   * @return
   */
  private static String getHistoryToken_Anchor(String historyToken) {

    // skip if there is no question mark
    if (historyToken.contains(PARAMSSTART) == false) {
      return historyToken;
    }

    // get just the historyToken/anchor tag
    String[] s = null; 
    if (historyToken.contains(PARAMSSTART) == true) {
      s = historyToken.split(PARAMSSTART);
    }
    
    historyToken = s[0];

    return historyToken;
  }

  /**
   * get historyToken parameters
   * 
   * like domain.tld#historyToken?[?params1=a&params2=b]
   * 
   * @param historyToken (anchor tag)
   * @return HashMap of the parameters ([varName, var] OR [key, value])
   */
  private static HashMap<String, String> getHistoryToken_Parameters(String historyToken) {

    if (historyToken.contains(PARAMSSTART) == false && historyToken.equals("") == false) {
      return null;
    }
    
    int index = historyToken.indexOf(PARAMSSTART) + 1;
    if (index == 0) {
      return null;
    }

    // get the sub string of parameters var=1&var2=2&var3=3... params1=a&params2=b...
    HashMap<String, String> params = null;
    try {
      String[] s = historyToken.substring(index, historyToken.length()).split(PARAMSPLITTER);
      params = new HashMap<String, String>();
      for (int i = 0; i < s.length; i++) {
        String[] ss = s[i].split(PARAMVALUESPLITTER);
        params.put(ss[0], ss[1]);
      }
    } catch (Exception e) {
      params = null;
    }

    return params;
  }

}
