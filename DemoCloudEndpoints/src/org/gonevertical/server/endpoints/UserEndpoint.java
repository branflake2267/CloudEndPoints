package org.gonevertical.server.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.gonevertical.server.data.PMF;
import org.gonevertical.server.data.User;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.search.Consistency;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@Api(
     name = "userendpoint",
     description = "This entity represents a user.", 
     version = "v3",
     clientIds = { "AIzaSyCCTPZtSIh59yZl3ZfRS-2U8tp6DWoWF9g" } ,
     scopes = { "https://www.googleapis.com/auth/userinfo.email" })
public class UserEndpoint {

  private static final Index INDEX = getIndex();

  private static Index getIndex() {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName("user_index").setConsistency(Consistency.PER_DOCUMENT)
        .build();
    return SearchServiceFactory.getSearchService().getIndex(indexSpec);
  }

  public static void addToSearchIndex(User p) {
    Document.Builder docBuilder = Document.newBuilder()
        .addField(Field.newBuilder().setName("id").setText(Long.toString(p.getId())))
        .addField(Field.newBuilder().setName("nameFirst").setText(p.getNameFirst() != null ? p.getNameFirst() : ""))
        .addField(Field.newBuilder().setName("nameLast").setText(p.getNameLast() != null ? p.getNameLast() : ""));
    docBuilder.setId(Long.toString(p.getId()));
    Document doc = docBuilder.build();
    INDEX.add(doc);
  }

  private static PersistenceManager getPersistenceManager() {
    return PMF.get().getPersistenceManager();
  }

  @ApiMethod(
      httpMethod = "GET",
      name = "user.list", 
      path = "user/list")
  @SuppressWarnings({ "cast", "unchecked" })
  public CollectionResponse<User> listUser(@Nullable @Named("cursor") String cursorString,
      @Nullable @Named("limit") Integer limit, com.google.appengine.api.users.User guser) throws Exception  {
    if (guser == null) {
      throw new UnauthorizedException(CustomErrors.MUST_LOG_IN.toString());
    }
    
    PersistenceManager pm = null;
    Cursor cursor = null;
    List<User> execute = null;
    try {
      pm = getPersistenceManager();
      Query query = pm.newQuery(User.class);

      if (cursorString != null && cursorString != "") {
        cursor = Cursor.fromWebSafeString(cursorString);
        Map<String, Object> extensionMap = new HashMap<String, Object>();
        extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
        query.setExtensions(extensionMap);
      }

      if (limit != null) {
        query.setRange(0, limit);
      }

      execute = (List<User>) query.execute();
      cursor = JDOCursorHelper.getCursor(execute);

      if (cursor != null) {
        cursorString = cursor.toWebSafeString();
      } else {
        cursorString = "";
      }

      execute.size();
    } finally {
      pm.close();
    }

    return CollectionResponse.<User> builder().setItems(execute).setNextPageToken(cursorString).build();
  }

  @ApiMethod(
      httpMethod = "GET", 
      name = "user.get",
      path = "user/get/{id}")
  public User getUser(@Named("id") Long id, com.google.appengine.api.users.User guser) throws Exception {
    if (guser == null) {
      throw new UnauthorizedException(CustomErrors.MUST_LOG_IN.toString());
    }
    
    PersistenceManager mgr = getPersistenceManager();
    User user = null;
    try {
      user = mgr.getObjectById(User.class, id);
    } finally {
      mgr.close();
    }
    return user;
  }

  @ApiMethod(
      httpMethod = "POST",
      name = "user.insert",
      path = "user/insert")
  public User insertUser(User user, com.google.appengine.api.users.User guser) throws Exception {
    if (guser == null) {
      throw new UnauthorizedException(CustomErrors.MUST_LOG_IN.toString());
    }
    
    PersistenceManager mgr = getPersistenceManager();
    try {
      mgr.makePersistent(user);
      addToSearchIndex(user);
    } finally {
      mgr.close();
    }
    return user;
  }

  @ApiMethod(
      httpMethod = "POST",
      name = "user.update",
      path = "user/update")
  public User updateUser(User user, com.google.appengine.api.users.User guser) throws Exception{
    if (guser == null) {
      throw new UnauthorizedException(CustomErrors.MUST_LOG_IN.toString());
    }
    
    PersistenceManager mgr = getPersistenceManager();
    try {
      mgr.makePersistent(user);
      addToSearchIndex(user);
    } finally {
      mgr.close();
    }
    return user;
  }

  @ApiMethod(
      httpMethod = "GET",
      name = "user.remove",
      path = "user/remove/{id}")
  public User removeUser(@Named("id") Long id, com.google.appengine.api.users.User guser) throws Exception {
    if (guser == null) {
      throw new UnauthorizedException(CustomErrors.MUST_LOG_IN.toString());
    }
    
    PersistenceManager mgr = getPersistenceManager();
    User user = null;
    try {
      user = mgr.getObjectById(User.class, id);
      mgr.deletePersistent(user);
      INDEX.remove(user.getId().toString());
    } finally {
      mgr.close();
    }
    return user;
  }

  @ApiMethod(
      httpMethod = "GET", 
      name = "user.search",
      path = "user/search/{queryString}")
  public List<User> search(@Named("queryString") String queryString, com.google.appengine.api.users.User guser) 
      throws Exception {
    if (guser == null) {
      throw new UnauthorizedException(CustomErrors.MUST_LOG_IN.toString());
    }
    
    List<User> returnList = new ArrayList<User>();
    Results<ScoredDocument> searchResults = INDEX.search(queryString);

    for (ScoredDocument scoredDoc : searchResults) {
      Field fieldId = scoredDoc.getOnlyField("id");
      if (fieldId == null || fieldId.getText() == null)
        continue;

      long userId = Long.parseLong(fieldId.getText());
      if (userId != -1) {
        User p = getUser(userId, guser);
        returnList.add(p);
      }
    }
    return returnList;
  }
  
}
