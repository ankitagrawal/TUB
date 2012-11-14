package com.hk.domain.catalog;

// Generated 10 Mar, 2011 5:37:39 PM by Hibernate Tools 3.2.4.CR1

import java.util.ArrayList;
import java.util.List;

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
import com.hk.domain.user.Address;

/**
 * Manufacturer generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "manufacturer")
public class Manufacturer implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long          id;

    @Column(name = "name", nullable = false, length = 100)
    private String        name;

    @Column(name = "description")
    private String        description;

    @Column(name = "website", length = 100)
    private String        website;

    @Column(name = "email")
    private String        email;

    @Column(name = "main_address_id")
    private Integer       mainAddressId;

    @Column(name = "is_available_all_over_india", nullable = true)
    private Boolean       isAvailableAllOverIndia;

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "manufacturer_has_address", joinColumns = { @JoinColumn(name = "manufacturer_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "address_id", nullable = false, updatable = false) })
    private List<Address> addresses = new ArrayList<Address>(0);

    public Manufacturer() {
    }

    public Manufacturer(String name, String description, String website) {
        this.name = name;
        this.description = description;
        this.website = website;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Manufacturer that = (Manufacturer) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMainAddressId() {
        return mainAddressId;
    }

    public void setMainAddressId(Integer mainAddressId) {
        this.mainAddressId = mainAddressId;
    }

    public Boolean isAvailableAllOverIndia() {
        return isAvailableAllOverIndia;
    }

    public void setAvailableAllOverIndia(Boolean availableAllOverIndia) {
        isAvailableAllOverIndia = availableAllOverIndia;
    }

    public Boolean getAvailableAllOverIndia() {
        return this.isAvailableAllOverIndia();
    }

    @Override
    public String toString() {
        return "Manufacturer{" + "id=" + id + ", name='" + name + "'\n" + '}';
    }
}
