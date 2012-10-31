package com.hk.domain.user;

import com.akube.framework.gson.JsonSkip;
import com.hk.constants.core.EnumCallPriority;
import com.hk.domain.catalog.product.combo.Combo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "user_detail")
public class UserDetail {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long                  id;

    @Column(name = "phone", nullable = false)
    private Long                 phone;

    @Column(name = "crm_priority", nullable = false)
    private Integer                 priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
