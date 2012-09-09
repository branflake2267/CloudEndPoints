package org.gonevertical.client.utils.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;

public class BaseJso extends JavaScriptObject {
  
  public static native JavaScriptObject fromJson(String json) /*-{
    return eval("(" + json + ")");
  }-*/;
  
  public final String toJson() {
    return new JSONObject(this).toString();
  }
  
  protected BaseJso() {
  }
  
}
