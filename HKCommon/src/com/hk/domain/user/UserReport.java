package com.hk.domain.user;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/13/13
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "user_report")

public class UserReport {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "number_of_orders_by_user", nullable = false)
    private Integer numberOfOrdersByUser;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getNumberOfOrdersByUser() {
        return numberOfOrdersByUser;
    }

    public void setNumberOfOrdersByUser(Integer numberOfOrdersByUser) {
        this.numberOfOrdersByUser = numberOfOrdersByUser;
    }
}
