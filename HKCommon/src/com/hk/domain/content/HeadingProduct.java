package com.hk.domain.content;

import javax.persistence.*;
import java.io.Serializable;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.PrimaryCategoryHeading;
/**
 * Created by IntelliJ IDEA.
 * User: Shrey
 * Date: Nov 26, 2012
 * Time: 9:08:36 PM
 * To change this template use File | Settings | File Templates.
 */

@NamedQueries({
    @NamedQuery(name = "getHeadingProductsByHeadingId", query = "select hp from HeadingProduct hp where hp.heading = :heading order By rank desc"),
    @NamedQuery(name = "getHeadingProductByHeadingAndProductId", query = "select hp from HeadingProduct hp where hp.heading = :heading and hp.product.id = :productId order By rank desc"),
    @NamedQuery(name = "getHeadingProductsSortedByRank", query = "select hp from HeadingProduct hp where hp.heading.id = :headingId and hp.product.outOfStock = false and hp.product.hidden = false and hp.product.deleted = false order By rank desc")
})

@Entity
@Table(name = "heading_product")
public class HeadingProduct implements Serializable{
   @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true, nullable = false, length = 5)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heading_id", nullable = false)
  private PrimaryCategoryHeading heading;

  @Column(name = "rank", nullable = true)
  private Integer rank;

  @Column(name = "product_id", insertable = false, updatable = false)
  private String productId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PrimaryCategoryHeading getHeading() {
    return heading;
  }

  public void setHeading(PrimaryCategoryHeading heading) {
    this.heading = heading;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getRank() {
    return rank;
  }

  public void setRank(Integer rank) {
    this.rank = rank;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }
}
