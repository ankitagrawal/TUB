package com.hk.domain.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Author: Kani
 * Date: Sep 2, 2008
 */
@Entity
@Table(name = "role")
/*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
public class Role {

  @Id
  @Column(name = "name")
  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SELECT)
  @JoinTable(name = "role_has_permission",
      joinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"),
      inverseJoinColumns = @JoinColumn(name = "permission_name", referencedColumnName = "name")
  )
  /*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)*/
  private Set<Permission> permissions = new HashSet<Permission>();

  @Transient
  private boolean selected;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Role)) return false;

    Role role = (Role) o;

    if (!name.equalsIgnoreCase(role.name)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
    @Override
    public String toString(){
        return this.name;
    }
}
