package com.hk.rest.models.user;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/15/12
 * Time: 6:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class APIUserDetail {

    private Long                  id;

    private Long                 phone;

    private Integer                 priority;

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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
