package com.hk.domain.queue;
// Generated 15-Apr-2013 01:46:24 by Hibernate Tools 3.2.4.CR1


import com.hk.domain.user.User;

import javax.persistence.*;

/**
 * ActionTask generated by hbm2java
 */
@Entity
@Table(name = "action_task")
public class ActionTask implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_action_task_id")
    private ActionTask nextActionTask;

    @Column(name = "name", nullable = false, length = 90)
    private String name;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "acknowledged", scale = 0)
    private boolean acknowledged;

    @Column(name = "range")
    private Long range;

    @Column(name = "hierarchy")
    private Long hierarchy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acknowledged_by", nullable = false)
    private User acknowledgedBy;

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

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    public Long getRange() {
        return this.range;
    }

    public void setRange(Long range) {
        this.range = range;
    }

    public Long getHierarchy() {
        return this.hierarchy;
    }

    public void setHierarchy(Long hierarchy) {
        this.hierarchy = hierarchy;
    }

    public ActionTask getNextActionTask() {
        return nextActionTask;
    }

    public void setNextActionTask(ActionTask nextActionTask) {
        this.nextActionTask = nextActionTask;
    }

    public User getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public void setAcknowledgedBy(User acknowledgedBy) {
        this.acknowledgedBy = acknowledgedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionTask that = (ActionTask) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}


