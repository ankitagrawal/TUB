package com.hk.domain.inventory.rtv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Dec 18, 2012
 * Time: 5:56:30 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "rtv_note_status")

public class RtvNoteStatus implements Serializable{


  @Id
  @Column(name = "id", unique = true, nullable = false)
  private Long id;


  @Column(name = "name", nullable = false, length = 45)
  private String name;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

	@Override
	 public String toString() {
	   return id == null ? "" : id.toString();
	 }


}

