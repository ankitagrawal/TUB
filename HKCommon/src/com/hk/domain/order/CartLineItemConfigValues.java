package com.hk.domain.order;
// Generated Feb 7, 2012 4:39:32 PM by Hibernate Tools 3.2.4.CR1


import com.hk.domain.catalog.product.VariantConfigOption;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name = "cart_line_item_config_values")
public class CartLineItemConfigValues implements java.io.Serializable, Comparable<CartLineItemConfigValues> {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "variant_config_option_id", nullable = false)
	private VariantConfigOption variantConfigOption;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_line_item_config_id", nullable = false)
	private CartLineItemConfig cartLineItemConfig;

	@Column(name = "value", nullable = false, length = 45)
	private String value;

	@Column(name = "additional_price", nullable = false, precision = 10)
	private Double additionalPrice;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public VariantConfigOption getVariantConfigOption() {
		return this.variantConfigOption;
	}

	public void setVariantConfigOption(VariantConfigOption variantConfigOption) {
		this.variantConfigOption = variantConfigOption;
	}

	public CartLineItemConfig getLineItemConfig() {
		return this.cartLineItemConfig;
	}

	public void setLineItemConfig(CartLineItemConfig cartLineItemConfig) {
		this.cartLineItemConfig = cartLineItemConfig;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getAdditionalPrice() {
		return this.additionalPrice;
	}

	public void setAdditionalPrice(Double additionalPrice) {
		this.additionalPrice = additionalPrice;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;

		if (o instanceof CartLineItemConfigValues) {
			CartLineItemConfigValues configValue = (CartLineItemConfigValues) o;

			return new EqualsBuilder().append(this.value, configValue.getValue()).
					append(this.additionalPrice, configValue.getAdditionalPrice()).append(this.variantConfigOption.getId(), configValue.variantConfigOption.getId()).isEquals();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.id).append(this.value).append(this.additionalPrice).append(this.variantConfigOption.getId()).toHashCode();
	}

	public int compareTo(CartLineItemConfigValues o) {
//    if (this.getValue().equalsIgnoreCase(o.getValue())) {
//      return this.getAdditionalPrice().compareTo(o.getAdditionalPrice());
//    }
		//return this.getValue().compareTo(o.getValue());

		return this.variantConfigOption.compareTo(o.getVariantConfigOption());
	}

}


