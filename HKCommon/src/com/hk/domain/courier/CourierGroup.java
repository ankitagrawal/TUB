package com.hk.domain.courier;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "courier_group")
public class CourierGroup implements java.io.Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long    id;

    @Column(name = "name", length = 45)
    private String  name;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private Courier courier;

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

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }
}
