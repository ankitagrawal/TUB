package com.hk.domain.clm;

import com.hk.domain.user.User;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: May 29, 2012
 * Time: 3:43:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="user_karma_profile")
public class KarmaProfile{

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true, nullable = false)
  private User user;

  @Column(name = "karma_points", nullable = false)
  private int karmaPoints;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getKarmaPoints() {
    return karmaPoints;
  }

  public void setKarmaPoints(int karmaPoints) {
    this.karmaPoints = karmaPoints;
  }


}
