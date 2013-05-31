package com.hk.domain.queue;

import javax.persistence.*;

/*
 * User: Pratham
 * Date: 27/05/13  Time: 15:31
*/
@Entity
@Table(name = "action_path_workflow")
public class ActionPathWorkflow implements java.io.Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_task_id")
    private ActionTask actionTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_action_task_id")
    private ActionTask nextActionTask;

    @Column(name = "priority")
    private Long priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public ActionTask getActionTask() {
        return actionTask;
    }

    public void setActionTask(ActionTask actionTask) {
        this.actionTask = actionTask;
    }

    public ActionTask getNextActionTask() {
        return nextActionTask;
    }

    public void setNextActionTask(ActionTask nextActionTask) {
        this.nextActionTask = nextActionTask;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
