package com.hk.domain.reversePickupOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/*
 * User: Pratham
 * Date: 19/09/13  Time: 14:13
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "reverse_pickup_type")
public class ReversePickupType  implements Serializable {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "status", nullable = false, unique = true)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ReversePickupType))
            return false;
        ReversePickupType reversePickupType = (ReversePickupType) o;
        if (this.getId() != null && reversePickupType.getId() != null) {
            return this.getId().equals(reversePickupType.getId());
        }
        return false;
    }
}
