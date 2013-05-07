package com.hk.domain.queue;
// Generated 10-Apr-2013 13:58:43 by Hibernate Tools 3.2.4.CR1


import com.akube.framework.gson.JsonSkip;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Bucket generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bucket")
public class Bucket implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", length = 45)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id")
    private Classification classification;

    @Transient
    private boolean selected;

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "bucket_constituents", uniqueConstraints = @UniqueConstraint(columnNames = {"bucket_id", "params_id"}), joinColumns = {@JoinColumn(name = "bucket_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "params_id", nullable = false, updatable = false)})
    private Set<Param> params = new HashSet<Param>(0);

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "action_item_buckets", uniqueConstraints = @UniqueConstraint(columnNames = {"bucket_id", "action_item_id"}), joinColumns = {@JoinColumn(name = "bucket_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "action_item_id", nullable = false, updatable = false)})
    private Set<ActionItem> actionItems = new HashSet<ActionItem>(0);

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

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public Set<Param> getParams() {
        return params;
    }

    public void setParams(Set<Param> params) {
        this.params = params;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Set<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(Set<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bucket bucket = (Bucket) o;

        return id.equals(bucket.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}


