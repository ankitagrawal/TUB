package com.hk.domain.catalog.product.combo;
// Generated Jul 17, 2012 1:59:37 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.catalog.product.Product;

import javax.persistence.*;

@Entity
@Table(name = "super_saver_image")
public class SuperSaverImage implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "url", nullable = false, length = 45)
    private String url;

    @Column(name = "alt_text", length = 200)
    private String altText;

    @Column(name = "height")
    private Long height;

    @Column(name = "width")
    private Long width;

    @Column(name = "hidden", nullable = false)
    private boolean hidden;

    @Column(name = "checksum", nullable = false, length = 45)
    private String checksum;

    @Column(name = "uploaded", nullable = false)
    private boolean uploaded;

    @Column(name = "ranking")
    private Long ranking;

    @Column(name = "link", length = 450)
    private String link;

    @Column(name = "is_main_image", nullable = false)
    private boolean isMainImage;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public boolean getHidden() {
        return this.hidden;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public boolean getUploaded() {
        return this.uploaded;
    }

    public boolean isUploaded() {
        return this.uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public Long getRanking() {
        return this.ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean getIsMainImage() {
        return this.isMainImage;
    }

    public boolean isMainImage() {
        return isMainImage;
    }

    public void setMainImage(boolean mainImage) {
        isMainImage = mainImage;
    }

    @Override
    public String toString() {
        return id == null ? "" : id.toString();
    }
}