package com.hk.domain.catalog.product;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "product_extra_option")
public class ProductExtraOption implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", unique = true)
  private Long id;

	@Column(name = "name", nullable = false, length = 45)
	private String name;

	@Column(name = "value", length = 45)
	private String value;

  public ProductExtraOption() {
  }

  public ProductExtraOption(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public Long getId() {
    return id;
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

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProductExtraOption)) return false;

    ProductExtraOption that = (ProductExtraOption) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (value != null ? !value.equals(that.value) : that.value != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }
}