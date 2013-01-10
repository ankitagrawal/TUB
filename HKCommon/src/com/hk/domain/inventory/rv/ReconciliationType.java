package com.hk.domain.inventory.rv;
// Generated Mar 5, 2012 6:01:24 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "reconciliation_type")
public class ReconciliationType implements java.io.Serializable {


    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof ReconciliationType) {
			ReconciliationType reconciliationType = (ReconciliationType) obj;
			if (reconciliationType.getId() != null && this.getId() != null) {
				return reconciliationType.getId().equals(this.getId());
			}
			return false;
		}
		return false;
	}

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}


