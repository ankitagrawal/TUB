package com.hk.domain.queue;
// Generated 15-Apr-2013 01:46:24 by Hibernate Tools 3.2.4.CR1


import javax.persistence.*;

/**
 * Classification generated by hbm2java
 */
@Entity
@Table(name = "classification")
public class Classification implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "primary", nullable = false, length = 45)
    private String primary;

    @Column(name = "secondary", length = 45)
    private String secondary;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimary() {
        return this.primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return this.secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Classification that = (Classification) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}


