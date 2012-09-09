package org.gonevertical.client.utils.data;

import com.google.gwt.core.client.JavaScriptObject;

public interface RequestHandler<T extends JavaScriptObject> {
  
  void onSuccess(T object);
  
  void onFailure(Throwable exception);
  
}
