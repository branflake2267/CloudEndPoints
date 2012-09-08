package org.gonevertical.server.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * insert 
 * curl -H 'Content-Type: application/json' -d '{ "nameFirst": "Brandon" }' http://localhost:8888/_ah/api/userendpoint/v1/user/insert
 * 
 * list 
 * curl http://localhost:8888/_ah/api/userendpoint/v1/user/list
 */
@PersistenceCapable
public class User {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Long id;

  @Persistent
  private String nameFirst;

  @Persistent
  private String nameLast;

  public User() {
  }

  public void setLongId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setNameFirst(String nameFirst) {
    this.nameFirst = nameFirst;
  }

  public String getNameFirst() {
    return nameFirst;
  }

  public void setNameLast(String nameLast) {
    this.nameLast = nameLast;
  }

  public String getNameLast() {
    return nameLast;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("id=" + id + ",");
    sb.append("nameFirst=" + nameFirst + ",");
    sb.append("nameLast=" + nameLast);
    return sb.toString();
  }

}
