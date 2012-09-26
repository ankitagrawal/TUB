package com.hk.domain.hkDelivery;
// Generated Aug 14, 2012 1:18:49 PM by Hibernate Tools 3.2.4.CR1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * RunsheetStatus generated by hbm2java
 */
@Entity
@Table(name = "runsheet_status")
public class RunsheetStatus implements java.io.Serializable {


    @Id

    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @Column(name = "status", nullable = false, length = 100)
    private String status;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }
}


