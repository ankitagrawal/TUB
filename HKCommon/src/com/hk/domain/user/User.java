package com.hk.domain.user;

import com.akube.framework.gson.JsonSkip;
import com.hk.constants.clm.CLMConstants;
import com.hk.constants.core.EnumPermission;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.user.EnumEmailSubscriptions;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.Order;
import com.hk.domain.queue.Bucket;
import com.hk.domain.store.Store;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * Author: Kani Date: Aug 29, 2008
 */
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = {"login", "store_id"}))
@NamedQueries({
        @NamedQuery(name = "user.findByEmail", query = "from User u where u.email = :email"),
        @NamedQuery(name = "user.findByLogin", query = "from User u where u.login = :login"),
        @NamedQuery(name = "user.findByEmailAndPassword", query = "from User u where u.email = :email and u.passwordChecksum = :passwordEncrypted")})
@Inheritance(strategy = InheritanceType.JOINED)
/* @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) */
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "login", nullable = false, length = 80)
    private String login;

    @Column(name = "email", nullable = true, length = 80)
    private String email;

    @Column(name = "name", nullable = true, length = 80)
    private String name;

    @Column(name = "password_checksum", nullable = false)
    private String passwordChecksum;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birth_date", nullable = true, length = 19)
    private Date birthDate;

    @Column(name = "gender", nullable = true, length = 6)
    private String gender;

    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date createDate = new Date();


    @Column(name = "subscribed_mask", nullable = false)
    private Integer subscribedMask;


    @Column(name = "unsubscribe_token", length = 512)
    private String unsubscribeToken;


    /*
     * @JsonSkip @Temporal(TemporalType.TIMESTAMP) @Column(name = "update_date", nullable = false, length = 19) private
     * Date updateDate;
     */
    @JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login_date", nullable = false, length = 19)
    private Date lastLoginDate;

    @Transient
    private String password;

    @Transient
    private String confirmPassword;

    @JsonSkip
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "user_has_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_name", referencedColumnName = "name"))
    /* @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) */
    private Set<Role> roles = new HashSet<Role>();


    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserDetail> userDetails = new HashSet<UserDetail>();

    @Column(name = "user_hash", nullable = false, length = 32, unique = true)
    private String userHash;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referred_by", nullable = true)
    private User referredBy;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliate_to", nullable = true)
    private User affiliateTo;

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "referrerUser")
    private List<Coupon> referrerCoupons = new ArrayList<Coupon>(1);

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @OrderBy("createDate desc")
    private List<RewardPoint> rewardPointList = new ArrayList<RewardPoint>();

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<RewardPointTxn> rewardPointTxnList = new ArrayList<RewardPointTxn>();

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserAccountInfo> userAccountInfos = new ArrayList<UserAccountInfo>(1);

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Where(clause = "deleted = 0")
    private List<Address> addresses = new ArrayList<Address>();

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<OfferInstance> offerInstances = new ArrayList<OfferInstance>();

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Order> orders = new ArrayList<Order>();

    @JsonSkip
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserReport report;

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Subscription> subscriptions;

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "warehouse_has_user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "warehouse_id"}), joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "warehouse_id", nullable = false, updatable = false)})
    private Set<Warehouse> warehouses = new HashSet<Warehouse>(0);

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "bucket_owner", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "bucket_id"}), joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "bucket_id", nullable = false, updatable = false)})
    private List<Bucket> buckets = new ArrayList<Bucket>(0);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @JsonSkip
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private KarmaProfile karmaProfile;

    @JsonSkip
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "hub_has_user", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "hub_id"}), joinColumns = {@JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "hub_id", updatable = false)})
    private Hub hub;

    public KarmaProfile getKarmaProfile() {
        return karmaProfile;
    }

    public void setKarmaProfile(KarmaProfile karmaProfile) {
        this.karmaProfile = karmaProfile;
    }

    public boolean isPriorityUser() {
        KarmaProfile karmaProfile = getKarmaProfile();
        if (karmaProfile != null) {
            return (karmaProfile.getKarmaPoints() >= CLMConstants.thresholdScore);
        } else {
            return false;
        }
    }

    public boolean isHKEmployee() {
        boolean isUserHKEmployee = false;
        for (Role role : getRoles()) {
            if (RoleConstants.HK_EMPLOYEE.equalsIgnoreCase(role.getName())) {
                isUserHKEmployee = true;
                break;
            }
        }

        return isUserHKEmployee;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<OfferInstance> getOfferInstances() {
        return offerInstances;
    }

    public void setOfferInstances(List<OfferInstance> offerInstances) {
        this.offerInstances = offerInstances;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Deprecated
    public Set<Warehouse> getWarehouses() {
        return this.warehouses;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public Warehouse getSelectedWarehouse() {
        if (warehouses != null && !warehouses.isEmpty()) {
            return warehouses.iterator().next();
        }

        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public String getPassword() {
        return password;
    }

    /*
     * public Date getUpdateDate() { return this.updateDate; }
     */

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<String> getRoleStrings() {
        Set<String> roleStrings = new HashSet<String>();
        for (Role role : roles) {
            roleStrings.add(role.getName());
        }
        return roleStrings;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPasswordChecksum() {
        return passwordChecksum;
    }

    public void setPasswordChecksum(String passwordChecksum) {
        this.passwordChecksum = passwordChecksum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        String firstName = "";
        try {
            if (name != null) {
                String[] nameArr = name.split(" ");
                if (nameArr.length > 0)
                    firstName = nameArr[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstName;
    }

    public boolean isSubscribedForNotify() {
        return EnumEmailSubscriptions.isSubscribed(EnumEmailSubscriptions.NOTIFY_ME, getSubscribedMask());
    }

    public boolean getSubscribedForNotify() {
        return isSubscribedForNotify();
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /*
     * public void setUpdateDate(Timestamp updateDate) { this.updateDate = updateDate; }
     */

    public String toString() {
        return id == null ? "" : id.toString();
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public User getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(User referredBy) {
        this.referredBy = referredBy;
    }

    public List<Coupon> getReferrerCoupons() {
        return referrerCoupons;
    }

    public void setReferrerCoupons(List<Coupon> referrerCoupons) {
        this.referrerCoupons = referrerCoupons;
    }

    public List<RewardPoint> getRewardPointList() {
        return rewardPointList;
    }

    public void setRewardPointList(List<RewardPoint> rewardPointList) {
        this.rewardPointList = rewardPointList;
    }

    public List<RewardPointTxn> getRewardPointTxnList() {
        return rewardPointTxnList;
    }

    public void setRewardPointTxnList(List<RewardPointTxn> rewardPointTxnList) {
        this.rewardPointTxnList = rewardPointTxnList;
    }

    public List<UserAccountInfo> getUserAccountInfos() {
        return userAccountInfos;
    }

    public void setUserAccountInfos(List<UserAccountInfo> userAccountInfos) {
        this.userAccountInfos = userAccountInfos;
    }

    public UserAccountInfo getUserAccountInfo() {
        return userAccountInfos != null && userAccountInfos.size() > 0 ? userAccountInfos.get(0) : null;
    }

    public Coupon getReferrerCoupon() {
        return referrerCoupons != null && referrerCoupons.size() > 0 ? referrerCoupons.get(0) : null;
    }

    public User getAffiliateTo() {
        return affiliateTo;
    }

    public void setAffiliateTo(User affiliateTo) {
        this.affiliateTo = affiliateTo;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getSubscribedMask() {
        return subscribedMask;
    }

    public void setSubscribedMask(int subscribed) {
        this.subscribedMask = subscribed;
    }


    public String getUnsubscribeToken() {
        return unsubscribeToken;
    }

    public void setUnsubscribeToken(String unsubscribeToken) {
        this.unsubscribeToken = unsubscribeToken;
    }

    /*
     * public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
     */

    public Hub getHub() {
        return hub;
    }

    public void setHub(Hub hub) {
        this.hub = hub;
    }

    public boolean hasPermission(EnumPermission enumPermission) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        Permission permission = new Permission();
        permission.setName(enumPermission.getPermissionName());
        for (Role role : roles) {
            if (role.getPermissions().contains(permission)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (!(o instanceof User))
            return false;

        User user = (User) o;

        if (!id.equals(user.getId()))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public UserReport getReport() {
        return report;
    }

    public void setReport(UserReport report) {
        this.report = report;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Bucket> buckets) {
        this.buckets = buckets;
    }

    public Integer getOrderCount(){
      int orderCount = 0;
      //TODO: implement this correctly Lazy init exception
      /*for(Order order : this.orders){
         if(order.getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())){
           orderCount++;
         }
      }*/
      return orderCount;
    }
}
