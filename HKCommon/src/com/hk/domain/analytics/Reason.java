package com.hk.domain.analytics;

import javax.persistence.*;

/*
 * User: Pratham
 * Date: 25/03/13  Time: 22:51
*/
@Entity
@Table(name = "reason")
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "primary_classification", nullable = false, length = 100)
    private String primaryClassification;

    @Column(name = "secondary_classification", nullable = false, length = 100)
    private String secondaryClassification;

    @Column(name = "type", nullable = false, length = 45)
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimaryClassification() {
        return primaryClassification;
    }

    public void setPrimaryClassification(String primaryClassification) {
        this.primaryClassification = primaryClassification;
    }

    public String getSecondaryClassification() {
        return secondaryClassification;
    }

    public void setSecondaryClassification(String secondaryClassification) {
        this.secondaryClassification = secondaryClassification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
