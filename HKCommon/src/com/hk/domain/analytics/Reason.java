package com.hk.domain.analytics;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.queue.ActionTask;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Classification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_id")
    private Classification classification;

    @Transient
    private boolean selected;

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "reason_has_bucket", uniqueConstraints = @UniqueConstraint(columnNames = {"reason_id", "bucket_id"}), joinColumns = {@JoinColumn(name = "reason_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "bucket_id", nullable = false, updatable = false)})
    private List<Bucket> buckets = new ArrayList<Bucket>(0);

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "reason_has_action_task", uniqueConstraints = @UniqueConstraint(columnNames = {"reason_id", "action_task_id"}), joinColumns = {@JoinColumn(name = "reason_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "action_task_id", nullable = false, updatable = false)})
    private List<ActionTask> actionTasks = new ArrayList<ActionTask>(0);

    @Column(name = "type", nullable = false, length = 45)
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public ActionTask getActionTask(){
        return this.actionTasks != null && !this.actionTasks.isEmpty() ? this.actionTasks.get(0) : null;
    }

    public List<ActionTask> getActionTasks() {
        return actionTasks;
    }

    public void setActionTasks(List<ActionTask> actionTasks) {
        this.actionTasks = actionTasks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.id != null ? this.id.toString() : "";
    }

}
