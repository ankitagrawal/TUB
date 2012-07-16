package com.hk.domain.catalog.category;
// Generated 10 Mar, 2011 5:37:39 PM by Hibernate Tools 3.2.4.CR1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "category_image")
public class CategoryImage implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category", nullable = false)
  private Category category;

  public void setCategory(Category category) {
    this.category = category;
  }

  public Category getCategory() {
    return category;
  }

  @Column(name = "url", nullable = false, length = 45)
  private String url;

  @Column(name = "alt_text", nullable = false, length = 45)
  private String altText;

  @Column(name = "height")
  private Long height;

  @Column(name = "width")
  private Long width;

  @Column(name = "checksum", nullable = false, length = 45)
  private String checksum;

  @Column(name = "hidden", nullable = false)
  private boolean hidden;

  @Column(name = "uploaded", nullable = false)
  private boolean uploaded;

  @Column(name = "ranking", nullable = true)
  private Long ranking;

  @Column(name = "link", nullable = true, length = 45)
  private String link;

  @Column(name = "position", nullable = true)
  private String position;

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public Long getRanking() {
    return ranking;
  }

  public void setRanking(Long ranking) {
    this.ranking = ranking;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAltText() {
    return this.altText;
  }

  public void setAltText(String altText) {
    this.altText = altText;
  }

  public Long getHeight() {
    return this.height;
  }

  public void setHeight(Long height) {
    this.height = height;
  }

  public Long getWidth() {
    return this.width;
  }

  public void setWidth(Long width) {
    this.width = width;
  }

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public boolean isUploaded() {
    return uploaded;
  }

  public void setUploaded(boolean uploaded) {
    this.uploaded = uploaded;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return id == null ? "" : id.toString();
  }
}


