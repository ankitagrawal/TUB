package com.hk.domain.courier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.akube.framework.gson.JsonSkip;

@SuppressWarnings("serial")
@Entity
@Table(name = "courier_group")
public class CourierGroup implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long         id;

    @Column(name = "name", length = 45)
    private String       name;

    @JsonSkip
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)   
    @JoinTable(name = "courier_group_has_courier", joinColumns = @JoinColumn(name = "courier_group_id", updatable = false), inverseJoinColumns = @JoinColumn(name = "courier_id", updatable = false))
    private Set<Courier> couriers = new HashSet<Courier>();

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

    public Set<Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(Set<Courier> couriers) {
        this.couriers = couriers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CourierGroup))
            return false;

        CourierGroup courierGroup = (CourierGroup) o;
        if (this.name != null && courierGroup.getName() != null) {
            return this.name.equals(courierGroup.getName());
        }
        return false;

    }

    @Override
    public int hashCode() {
        return this.name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

}
