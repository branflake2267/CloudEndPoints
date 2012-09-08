package org.gonevertical.server.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.gonevertical.server.data.Animal;
import org.gonevertical.server.data.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.CollectionResponse;
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

@Api(name = "animalendpoint", version = "v3")
public class AnimalEndpoint {

  private static final Index INDEX = getIndex();

  private static Index getIndex() {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName("animal_index").setConsistency(Consistency.PER_DOCUMENT)
        .build();
    return SearchServiceFactory.getSearchService().getIndex(indexSpec);
  }

  public static void addToSearchIndex(Animal animal) {
    Document.Builder docBuilder = Document.newBuilder()
        .addField(Field.newBuilder().setName("id").setText(Long.toString(animal.getId())))
        .addField(Field.newBuilder().setName("name").setText(animal.getName() != null ? animal.getName() : ""));
    docBuilder.setId(Long.toString(animal.getId()));
    Document doc = docBuilder.build();
    INDEX.add(doc);
  }

  private static PersistenceManager getPersistenceManager() {
    return PMF.get().getPersistenceManager();
  }

  @ApiMethod(
      httpMethod = "GET",
      name = "animal.list", 
      path = "animal/list")
  @SuppressWarnings({ "cast", "unchecked" })
  public CollectionResponse<Animal> listAnimal(@Nullable @Named("cursor") String cursorString,
      @Nullable @Named("limit") Integer limit) {
    PersistenceManager pm = null;
    Cursor cursor = null;
    List<Animal> execute = null;
    try {
      pm = getPersistenceManager();
      Query query = pm.newQuery(Animal.class);

      if (cursorString != null && cursorString != "") {
        cursor = Cursor.fromWebSafeString(cursorString);
        Map<String, Object> extensionMap = new HashMap<String, Object>();
        extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
        query.setExtensions(extensionMap);
      }

      if (limit != null) {
        query.setRange(0, limit);
      }

      execute = (List<Animal>) query.execute();
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

    return CollectionResponse.<Animal> builder().setItems(execute).setNextPageToken(cursorString).build();
  }

  @ApiMethod(
      httpMethod = "GET", 
      name = "animal.get",
      path = "animal/get/{id}")
  public Animal getAnimal(@Named("id") Long id) {
    PersistenceManager mgr = getPersistenceManager();
    Animal animal = null;
    try {
      animal = mgr.getObjectById(Animal.class, id);
    } finally {
      mgr.close();
    }
    return animal;
  }

  @ApiMethod(
      httpMethod = "POST",
      name = "animal.insert",
      path = "animal/insert")
  public Animal insertAnimal(Animal animal) {
    PersistenceManager mgr = getPersistenceManager();
    try {
      mgr.makePersistent(animal);
      addToSearchIndex(animal);
    } finally {
      mgr.close();
    }
    return animal;
  }

  @ApiMethod(
      httpMethod = "POST",
      name = "animal.update",
      path = "animal/update")
  public Animal updateAnimal(Animal animal) {
    PersistenceManager mgr = getPersistenceManager();
    try {
      mgr.makePersistent(animal);
      addToSearchIndex(animal);
    } finally {
      mgr.close();
    }
    return animal;
  }

  @ApiMethod(
      httpMethod = "GET",
      name = "animal.remove",
      path = "animal/remove/{id}")
  public Animal removeAnimal(@Named("id") Long id) {
    PersistenceManager mgr = getPersistenceManager();
    Animal animal = null;
    try {
      animal = mgr.getObjectById(Animal.class, id);
      mgr.deletePersistent(animal);
    } finally {
      mgr.close();
    }
    return animal;
  }

  @ApiMethod(
      httpMethod = "GET", 
      name = "animal.search",
      path = "animal/search/{queryString}")
  public List<Animal> search(String queryString) {
    List<Animal> returnList = new ArrayList<Animal>();
    Results<ScoredDocument> searchResults = INDEX.search(queryString);

    for (ScoredDocument scoredDoc : searchResults) {
      Field fieldId = scoredDoc.getOnlyField("id");
      if (fieldId == null || fieldId.getText() == null)
        continue;

      long animalId = Long.parseLong(fieldId.getText());
      if (animalId != -1) {
        Animal a = getAnimal(animalId);
        returnList.add(a);
      }
    }
    return returnList;
  }

}
