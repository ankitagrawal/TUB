package com.hk.domain.core;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Nov 27, 2012
 * Time: 4:45:04 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "country")
public class Country implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "country_code", nullable = false, length = 6)
    private String countryCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

    public int compareTo(Country country) {
        return this.getName().compareTo(country.getName());

    }


}
