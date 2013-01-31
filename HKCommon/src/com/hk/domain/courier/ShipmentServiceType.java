package com.hk.domain.courier;


import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name = "shipment_service_type")
public class ShipmentServiceType implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 90)
    private String name;

    @Transient
    private Boolean cod;

    @Transient
    private Boolean ground;

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

    public Boolean getCod() {
        return cod;
    }

    public void setCod(Boolean cod) {
        this.cod = cod;
    }

    public Boolean getGround() {
        return ground;
    }

    public void setGround(Boolean ground) {
        this.ground = ground;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ShipmentServiceType))
            return false;

        ShipmentServiceType ShipmentServiceType = (ShipmentServiceType) o;
        return id.equals(ShipmentServiceType.getId());
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

}