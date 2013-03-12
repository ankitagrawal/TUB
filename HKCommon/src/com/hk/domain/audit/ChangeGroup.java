package com.hk.domain.audit;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author adlakha.vaibhav
 */
@Entity
@Table(name = "change_group")
public class ChangeGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long            id;

    @Column(name = "entity_id", unique = false, nullable = false)
    private String          entityId;

    @Column(name = "entity_name", nullable = false, length = 50)
    private String          entityName;

    @Column(name = "action", nullable = false, length = 50)
    private String          action;

    @Column(name = "modifying_user", nullable = false, length = 100)
    private String          userEmail;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "changeGroup")
    private Set<ChangeItem> changeItems = new HashSet<ChangeItem>();

    @Column(name = "modified_date")
    private Date            modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Set<ChangeItem> getChangeItems() {
        return changeItems;
    }

    public void setChangeItems(Set<ChangeItem> changeItems) {
        this.changeItems = changeItems;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
