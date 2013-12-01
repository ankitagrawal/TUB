package com.hk.api.models.user;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/15/12
 * Time: 6:20 PM
 */
public class APIUserDetail {

    //User Id
    private Long                  id;
    //Phone number of the user
    private Long                 phone;
    //Priority of the user
    private Integer                 priority = 0;

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
