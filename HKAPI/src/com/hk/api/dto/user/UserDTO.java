package com.hk.api.dto.user;

import java.util.Date;

import com.hk.domain.core.JSONObject;
import com.hk.domain.user.User;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class UserDTO extends JSONObject {

    private String name;
    private String email;
    private Date   birthDate;
    private String gender;
    

    public UserDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.birthDate = user.getBirthDate();
        this.gender = user.getGender();
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    

    @Override
    protected String[] getKeys() {
        return new String[] { "nm", "em", "sex", "bDt"};
    }

    @Override
    protected Object[] getValues() {
        return new Object[] { this.name, this.email, this.gender, this.birthDate };
    }

}
