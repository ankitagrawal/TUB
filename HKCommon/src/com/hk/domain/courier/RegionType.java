package com.hk.domain.courier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "region_type")
public class RegionType implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long                   id;

    @Column(name = "name", length = 45)
    private String                 name;

    @Column(name = "priority")
    private Long                   priority;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "regionType")
    private Set<PincodeRegionZone> pincodeRegionZones = new HashSet<PincodeRegionZone>(0);

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

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

    public Set<PincodeRegionZone> getPincodeRegionZones() {
        return this.pincodeRegionZones;
    }

    public void setPincodeRegionZones(Set<PincodeRegionZone> pincodeRegionZones) {
        this.pincodeRegionZones = pincodeRegionZones;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
