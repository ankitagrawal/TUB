package com.hk.domain.store;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
@Table(name = "store")
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long   id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "prefix", nullable = false, length = 45)
    private String prefix;

    @Column(name ="callback_rest_url", length = 100)
    private String callbackRestUrl;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCallbackRestUrl() {
        return callbackRestUrl;
    }

    public void setCallbackRestUrl(String callbackRestUrl) {
        this.callbackRestUrl = callbackRestUrl;
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Store) {
            Store store = (Store) o;

            return new EqualsBuilder().append(this.name, store.getName()).isEquals();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.name).toHashCode();
    }

}
