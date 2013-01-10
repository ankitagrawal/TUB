package com.hk.domain.catalog.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.akube.framework.gson.JsonSkip;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.category.Category;

/**
 * Product generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "product")
/* @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) */
@Inheritance(strategy = InheritanceType.JOINED)
public class Product  implements java.io.Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 20)
    private String               id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer         manufacturer;

    @Column(name = "name", nullable = false, length = 150)
    private String               name;

    @Column(name = "thumb_url", nullable = false, length = 150)
    private String               thumbUrl;

    @Column(name = "order_ranking", precision = 22, scale = 0)
    private Double               orderRanking;

    @Column(name = "brand", length = 100)
    private String               brand;

    @Column(name = "pack_type", length = 45)
    private String               packType;

    @Column(name = "overview")
    private String               overview;

    @Column(name = "description")
    private String               description;

    @Column(name = "keywords", length = 150)
    private String               keywords;

    @Column(name = "min_days")
    private Long                 minDays;

    @Column(name = "max_days")
    private Long                 maxDays;

    @Column(name = "deleted", scale = 0)
    private Boolean              deleted;

    @Column(name = "main_image_id")
    private Long                 mainImageId;

    @Column(name = "product_has_color_options", scale = 0)
    private Boolean              isProductHaveColorOptions;

    @Column(name = "is_service", scale = 0)
    private Boolean              isService;

    @Column(name = "is_google_ad_disallowed", scale = 0)
    private Boolean              isGoogleAdDisallowed;

    @Column(name = "hidden", scale = 0)
    private Boolean              hidden;

    @Column(name = "is_jit", scale = 0)
    private Boolean              isJit;

    @Column(name = "is_amazon_product")
    private Boolean              isAmazonProduct;

    @Column(name = "out_of_stock")
    private Boolean              outOfStock;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier             supplier;

	@JsonSkip
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_dt", nullable = false, length = 19)
    private Date                 createDate = new Date();

    @Column(name = "video_embed_code")
    private String               videoEmbedCode;

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "product_has_related_product", joinColumns = { @JoinColumn(name = "product_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "related_product_id", nullable = false, updatable = false) })
    private List<Product>        relatedProducts  = new ArrayList<Product>(0);

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductVariant> productVariants  = new ArrayList<ProductVariant>(0);

    @JsonSkip
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "category_has_product", joinColumns = { @JoinColumn(name = "product_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "category_name", nullable = false, updatable = false) })
    private List<Category>       categories       = new ArrayList<Category>(0);

    @JsonSkip
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<SimilarProduct> similarProducts  = new ArrayList<SimilarProduct>(0);

    @JsonSkip
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductFeature> productFeatures  = new ArrayList<ProductFeature>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage>   productImages    = new ArrayList<ProductImage>();

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_category", nullable = false)
    private Category             primaryCategory;

    @JsonSkip
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondary_category", nullable = true)
    private Category             secondaryCategory;

    @Column(name = "drop_shipping")
    private boolean              dropShipping;

    @Column(name = "ground_shipping")
    private boolean              groundShipping;

	@Column(name = "cod_allowed", nullable = false, scale = 1)
    private Boolean              codAllowed;

    @Column(name = "is_subscribable", nullable = true)
    private Boolean              isSubscribable;

    @Column(name = "is_installable")
    private Boolean   installable ;

    @Transient
    private String               categoriesPipeSeparated;

    @Transient
    private String               productURL;

    /*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private Set<SuperSaverImage> superSaverImages = new HashSet<SuperSaverImage>(0);*/

    public String getSlug() {
        return Category.getNameFromDisplayName(this.getName());
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Manufacturer getManufacturer() {
        return this.manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbUrl() {
        return this.thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Double getOrderRanking() {
        return this.orderRanking;
    }

    public void setOrderRanking(Double orderRanking) {
        this.orderRanking = orderRanking;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPackType() {
        return this.packType;
    }

    public void setPackType(String packType) {
        this.packType = packType;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getMinDays() {
        return this.minDays;
    }

    public void setMinDays(Long minDays) {
        this.minDays = minDays;
    }

    public Long getMaxDays() {
        return this.maxDays;
    }

    public void setMaxDays(Long maxDays) {
        this.maxDays = maxDays;
    }

    public List<Product> getRelatedProducts() {
        return relatedProducts;
    }

    public void setRelatedProducts(List<Product> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

	public List<ProductVariant> getProductVariants() {
		if (productVariants != null && !productVariants.isEmpty()) {
			Collections.sort(productVariants, new ProductVariantComparator());
		} else {
			productVariants = new ArrayList<ProductVariant>();
		}
		return productVariants;
	}

    public List<ProductVariant> getInStockVariants() {
        List<ProductVariant> inStockVariants = new ArrayList<ProductVariant>();
        for (ProductVariant productVariant : productVariants) {
            if (!productVariant.isDeleted() && !productVariant.isOutOfStock()) {
                inStockVariants.add(productVariant);
            }
        }
        return inStockVariants;
    }

    public List<ProductVariant> getProductVariantsWithOutOfStockListedBelow() {
        Collections.sort(productVariants, new ProductVariantComparator2());
        return productVariants;
    }

    public void setProductVariants(List<ProductVariant> productVariants) {
        this.productVariants = productVariants;
    }

    public List<Category> getCategories() {
        Collections.sort(categories, new CategoryComparator());
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getLowestLevelCategory() {
        return this.categories != null && this.categories.size() > 0 ? this.categories.get(0) : null;
    }

    public List<ProductFeature> getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(List<ProductFeature> productFeatures) {
        this.productFeatures = productFeatures;

    }

    public ProductVariant getMaximumDiscountProducVariant() {
        ProductVariant maxDiscountPV = new ProductVariant();
        maxDiscountPV.setDiscountPercent(0.0);
        for (ProductVariant productVariant : this.getProductVariants()) {
            if (productVariant.getDiscountPercent() >= maxDiscountPV.getDiscountPercent()) {
                maxDiscountPV = productVariant;
                maxDiscountPV.setDiscountPercent(productVariant.getDiscountPercent());
            }
        }
        return maxDiscountPV;
    }

    public ProductVariant getInStockMaximumDiscountProductVariant() {
        ProductVariant inStockMaxDiscountPV = null;
        Double maxDiscountPercent = 0D;
        for (ProductVariant productVariant : this.getProductVariants()) {
            if (!productVariant.isOutOfStock()) {
                if (productVariant.getDiscountPercent() >= maxDiscountPercent) {
                    inStockMaxDiscountPV = productVariant;
                    maxDiscountPercent = productVariant.getDiscountPercent();
                }
            }
        }
        return inStockMaxDiscountPV;
    }

    public ProductVariant getMaximumMRPProducVariant() {
        ProductVariant maxMRPPV = new ProductVariant();
        maxMRPPV.setMarkedPrice(0.0);
        for (ProductVariant productVariant : this.getProductVariants()) {
            if (!productVariant.isDeleted()) {
                if (productVariant.getMarkedPrice() > maxMRPPV.getMarkedPrice())
                    maxMRPPV = productVariant;
            }
        }
        return maxMRPPV;
    }

    public ProductVariant getMinimumMRPProducVariant() {
        ProductVariant minMRPPV = new ProductVariant();
        minMRPPV.setMarkedPrice(1000000.0);
        for (ProductVariant productVariant : this.getProductVariants()) {
            if (!productVariant.isDeleted()) {
                if (productVariant.getMarkedPrice() < minMRPPV.getMarkedPrice())
                    minMRPPV = productVariant;
            }
        }
        return minMRPPV;
    }

    public ProductVariant getMinimumHKPriceProductVariant() {
        ProductVariant minMRPPV = new ProductVariant();
        minMRPPV.setHkPrice(1000000.0);
        for (ProductVariant productVariant : this.getProductVariants()) {
            if (!productVariant.isDeleted()) {
                if (productVariant.getHkPrice() < minMRPPV.getHkPrice())
                    minMRPPV = productVariant;
            }
        }
        return minMRPPV;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(Long mainImageId) {
        this.mainImageId = mainImageId;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public Boolean isProductHaveColorOptions() {
        return isProductHaveColorOptions == null ? false : isProductHaveColorOptions;
    }

    public Boolean getProductHaveColorOptions() {
        return this.isProductHaveColorOptions();
    }

    public void setProductHaveColorOptions(Boolean productHaveColorOptions) {
        isProductHaveColorOptions = productHaveColorOptions;
    }

    public Boolean isOutOfStock() {
        return outOfStock;
    }

    public Boolean getOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(Boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Category getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(Category primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public Boolean isService() {
        return isService;
    }

    public Boolean getService() {
        return isService;
    }

    public void setService(Boolean service) {
        isService = service;
    }

    public Boolean isGoogleAdDisallowed() {
        return isGoogleAdDisallowed;
    }

    public Boolean getGoogleAdDisallowed() {
        return isGoogleAdDisallowed;
    }

    public void setGoogleAdDisallowed(Boolean googleAdDisallowed) {
        isGoogleAdDisallowed = googleAdDisallowed;
    }

    public String getVideoEmbedCode() {
        return videoEmbedCode;
    }

    public void setVideoEmbedCode(String videoEmbedCode) {
        this.videoEmbedCode = videoEmbedCode;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getJit() {
        return isJit;
    }

    public Boolean isJit() {
        return isJit;
    }

    public void setJit(Boolean jit) {
        isJit = jit;
    }

    public Boolean getAmazonProduct() {
        return isAmazonProduct;
    }

    public void setAmazonProduct(Boolean amazonProduct) {
        isAmazonProduct = amazonProduct;
    }

    public void setCategoriesPipeSeparated(String categoriesPipeSeparated) {
        this.categoriesPipeSeparated = categoriesPipeSeparated;
    }

    public String getCategoriesPipeSeparated() {
        return categoriesPipeSeparated;
    }

    public Category getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(Category secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public String getProductURL() {
	    /**
	     * productURL is a Transient Variable should be set in accordance with ProductReferrer from where the product is loaded.
	     * In some cases products are loaded by productIds where tracking is not being done.
	     * In case productURL is null returning the relative URL
	     */
        if (productURL == null){
			productURL = "/product/" + getSlug() + "/" + getId();
		}
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public List<SimilarProduct> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<SimilarProduct> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getPipeSeparatedCategories() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Iterator<Category> categoriesIterator = categories.iterator(); categoriesIterator.hasNext();) {
            Category category = categoriesIterator.next();
            stringBuffer.append(category.getDisplayName());
            if (categoriesIterator.hasNext()) {
                stringBuffer.append("|");
            }
        }
        return stringBuffer.toString();
    }

    public boolean getDropShipping() {
        return dropShipping;
    }

    public boolean isDropShipping() {
        return dropShipping;
    }

    public void setDropShipping(boolean dropShipping) {
        this.dropShipping = dropShipping;
    }

     public boolean isGroundShipping() {
        return groundShipping;
    }
    public Boolean getGroundShipping(){
         return groundShipping;
    }

    public void setGroundShipping(boolean groundShipping) {
        this.groundShipping = groundShipping;
    }

	public Boolean isCodAllowed() {
		return codAllowed;
	}
	
	public Boolean getCodAllowed() {
		return codAllowed;
	}  	

	public void setCodAllowed(Boolean codAllowed) {
		this.codAllowed = codAllowed;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Product))
            return false;

        Product product = (Product) o;

        if (id != null ? !id.equals(product.getId()) : product.getId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public boolean contains(ProductVariant productVariant) {
        for (ProductVariant variant : this.getProductVariants()) {
            if (variant.equals(productVariant))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return id == null ? "" : id;
    }

    public String toStringDetailed() {
        return "Product{" + "id='" + id + "'\n" + ", name='" + name + "'\n" + ", brand='" + brand + "'\n" + ", packType='" + packType + "'\n" + ", overview='" + overview + "'\n"
                + '}';
    }

    public class ProductVariantComparator implements Comparator<ProductVariant> {
        public int compare(ProductVariant o1, ProductVariant o2) {
            if (o1.getOrderRanking() != null && o2.getOrderRanking() != null) {
                return o1.getOrderRanking().compareTo(o2.getOrderRanking());
            }
            return -1;
        }
    }

    public class ProductVariantComparator2 implements Comparator<ProductVariant> {
        public int compare(ProductVariant o1, ProductVariant o2) {
            if (o1.getOrderRanking() != null && o2.getOrderRanking() != null) {
                if (o1.isOutOfStock() && o2.isOutOfStock()) {
                    return o1.getOrderRanking().compareTo(o2.getOrderRanking());
                } else if (o1.isOutOfStock()) {
                    return 1;
                } else if (o2.isOutOfStock()) {
                    return -1;
                } else {
                    return o1.getOrderRanking().compareTo(o2.getOrderRanking());
                }
            }
            return -1;
        }
    }

    public class CategoryComparator implements Comparator<Category> {
        public int compare(Category o1, Category o2) {
            if (o1.getName() != null && o2.getName() != null) {
                return o1.getName().compareTo(o2.getName());
            }
            return -1;
        }
    }

    public boolean isSubscribable() {
        if (isSubscribable != null) {
            return isSubscribable;
        } else {
            return false;
        }
    }

    public void setSubscribable(boolean subscribable) {
        isSubscribable = subscribable;
    }

    public Boolean isInstallable() {
        return installable;
    }

    public void setInstallable(Boolean installable) {
        this.installable = installable;
    }

     public Boolean getInstallable() {
		return installable;
	}

}
