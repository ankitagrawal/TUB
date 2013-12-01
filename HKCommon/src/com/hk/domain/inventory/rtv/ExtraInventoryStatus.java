package com.hk.domain.inventory.rtv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Feb 1, 2013
 * Time: 5:43:44 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "extra_inventory_status")

public class ExtraInventoryStatus implements Serializable{


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
  @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ExtraInventoryStatus))
            return false;

        ExtraInventoryStatus extraInventoryStatus = (ExtraInventoryStatus) o;

        if (id != null ? !id.equals(extraInventoryStatus.getId()) : extraInventoryStatus.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
