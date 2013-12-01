package com.hk.domain.catalog.product.combo;

// Generated Oct 10, 2011 12:28:04 PM by Hibernate Tools 3.2.4.CR1

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.product.ProductVariant;

@SuppressWarnings("serial")
@Entity
@Table(name = "combo_instance")
public class ComboInstance implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long                                 id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "combo_id", nullable = false)
    private Combo                                combo;

    @Transient
    private Long                                 qty;

    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "comboInstance")
    private List<ComboInstanceHasProductVariant> comboInstanceProductVariants = new ArrayList<ComboInstanceHasProductVariant>(0);

    public ComboInstanceHasProductVariant getComboInstanceProductVariant(ProductVariant productVariant) {
        for (ComboInstanceHasProductVariant comboInstanceProductVariant : this.getComboInstanceProductVariants()) {
            if (comboInstanceProductVariant.getProductVariant().equals(productVariant)) {
                return comboInstanceProductVariant;
            }
        }
        return null;
    }

    public List<ProductVariant> getVariants() {
        List<ProductVariant> allVariants = new ArrayList<ProductVariant>();
        for (ComboInstanceHasProductVariant comboInstanceProductVariant : this.getComboInstanceProductVariants()) {
            allVariants.add(comboInstanceProductVariant.getProductVariant());
        }
        return allVariants;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Combo getCombo() {
        return combo;
    }

    public void setCombo(Combo combo) {
        this.combo = combo;
    }

    public List<ComboInstanceHasProductVariant> getComboInstanceProductVariants() {
        return comboInstanceProductVariants;
    }

    public void setComboInstanceProductVariants(List<ComboInstanceHasProductVariant> comboInstanceProductVariants) {
        this.comboInstanceProductVariants = comboInstanceProductVariants;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
}
