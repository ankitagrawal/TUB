package com.hk.domain.user;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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

public class UserReport implements java.io.Serializable {
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    @Column(name = "number_of_orders_by_user", nullable = false)
    private Integer numberOfOrdersByUser;


    public Integer getNumberOfOrdersByUser() {
        return numberOfOrdersByUser;
    }

    public void setNumberOfOrdersByUser(Integer numberOfOrdersByUser) {
        this.numberOfOrdersByUser = numberOfOrdersByUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}