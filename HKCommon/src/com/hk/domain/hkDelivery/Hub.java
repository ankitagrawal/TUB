package com.hk.domain.hkDelivery;
// Generated Aug 14, 2012 1:18:49 PM by Hibernate Tools 3.2.4.CR1


import com.akube.framework.gson.JsonSkip;
import com.hk.domain.core.Pincode;
import com.hk.domain.user.User;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Hub generated by hbm2java
 */
@Entity
@Table(name = "hub")
public class Hub implements java.io.Serializable {


    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pincode_id", nullable = false)
    private Pincode pincode;


    @Column(name = "name", nullable = false, length = 150)
    private String name;


    @Column(name = "address", length = 150)
    private String address;


    @Column(name = "country", length = 50)
    private String country;

	@JsonSkip
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "hub_has_user",
			joinColumns = {@JoinColumn(name = "hub_id", nullable = false, updatable = false)},
			inverseJoinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}
	)
	private Set<User> users = new HashSet<User>(0);


    /*  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hub")
        private Set<Consignment> consignments = new HashSet<Consignment>(0);

        @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hub")
        private Set<Runsheet> runsheets = new HashSet<Runsheet>(0);
    */
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pincode getPincode() {
        return this.pincode;
    }

    public void setPincode(Pincode pincode) {
        this.pincode = pincode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}


