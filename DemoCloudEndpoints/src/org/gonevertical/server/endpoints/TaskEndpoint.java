package org.gonevertical.server.endpoints;

import org.gonevertical.server.data.PMF;
import org.gonevertical.server.data.Task;

import com.google.api.server.spi.config.Api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "taskendpoint")
public class TaskEndpoint {

  /**
   * This method lists all the entities inserted in datastore.
   * It uses HTTP GET method.
   *
   * @return List of all entities persisted.
   */
  @SuppressWarnings({"cast", "unchecked"})
  public List<Task> listTask() {
    PersistenceManager mgr = getPersistenceManager();
    List<Task> result = new ArrayList<Task>();
    try{
      Query query = mgr.newQuery(Task.class);
      for (Object obj : (List<Object>) query.execute()) {
        result.add(((Task) obj));
      }
    } finally {
      mgr.close();
    }
    return result;
  }

  /**
   * This method gets the entity having primary key id. It uses HTTP GET method.
   *
   * @param id the primary key of the java bean.
   * @return The entity with primary key id.
   */
  public Task getTask(@Named("id") Long id) {
    PersistenceManager mgr = getPersistenceManager();
    Task task  = null;
    try {
      task = mgr.getObjectById(Task.class, id);
    } finally {
      mgr.close();
    }
    return task;
  }

  /**
   * This inserts the entity into App Engine datastore.
   * It uses HTTP POST method.
   *
   * @param task the entity to be inserted.
   * @return The inserted entity.
   */
  public Task insertTask(Task task) {
    PersistenceManager mgr = getPersistenceManager();
    try {
      mgr.makePersistent(task);
    } finally {
      mgr.close();
    }
    return task;
  }

  /**
   * This method is used for updating a entity.
   * It uses HTTP PUT method.
   *
   * @param task the entity to be updated.
   * @return The updated entity.
   */
  public Task updateTask(Task task) {
    PersistenceManager mgr = getPersistenceManager();
    try {
      mgr.makePersistent(task);
    } finally {
      mgr.close();
    }
    return task;
  }

  /**
   * This method removes the entity with primary key id.
   * It uses HTTP DELETE method.
   *
   * @param id the primary key of the entity to be deleted.
   * @return The deleted entity.
   */
  public Task removeTask(@Named("id") Long id) {
    PersistenceManager mgr = getPersistenceManager();
     Task task  = null;
    try {
      task = mgr.getObjectById(Task.class, id);
      mgr.deletePersistent(task);
    } finally {
      mgr.close();
    }
    return task;
  }

  private static PersistenceManager getPersistenceManager() {
    return PMF.get().getPersistenceManager();
  }

}
