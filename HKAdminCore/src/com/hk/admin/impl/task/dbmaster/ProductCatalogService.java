package com.hk.admin.impl.task.dbmaster;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.admin.util.XslParser;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.ManufacturerDao;
import com.hk.pact.dao.core.SupplierDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.TaxService;

@Component
public class ProductCatalogService {

	private static Logger logger = LoggerFactory.getLogger(ProductCatalogService.class);
	private static Gson gson = JsonUtils.getGsonDefault();

	@Autowired
	private XslParser xslParser;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ManufacturerDao manufacturerDao;
	
	@Autowired
	private SupplierDao supplierDao;
	
	@Autowired
	CategoryService categoryService;
	
	@Autowired
	ProductVariantService productVariantService;
	
	@Autowired
	TaxService taxService;
	
	@Autowired
	BaseDao baseDao;
	
	//protected com.google.inject.Session> sessionProvider;

	public void insertCatalogue(File inFile, User loggedOnUser) throws Exception {
		Set<Product> productSet = xslParser.readProductList(inFile, loggedOnUser);
		for (Product product : productSet) {
//      logger.trace(gson.toJson(product));

			// todo - product image
			product.setThumbUrl("no-image.jpg");

			// first insert just the product.. remove and keep the productvariant and category list seaprately
			List<ProductVariant> productVariants = product.getProductVariants();
			product.setProductVariants(null);

			List<Category> categoryList = product.getCategories();
			product.setCategories(null);

			Category primaryCategory = product.getPrimaryCategory();
			product.setPrimaryCategory(null);

			Manufacturer manufacturer = product.getManufacturer();
			product.setManufacturer(null);

			// insert manufacturer if not present already.. else pick up the existing one, set the new values.
			logger.debug(product.toString());
			if (manufacturer != null) {
				Manufacturer manufacturerDb = getManufacturerDao().findByName(manufacturer.getName());
				if (manufacturerDb == null) {
					manufacturer = (Manufacturer) getManufacturerDao().save(manufacturer);
					product.setManufacturer(manufacturer);
					logger.debug("Manufacturer: " + manufacturer);
				} else {
					manufacturerDb.setName(manufacturer.getName());
					manufacturerDb.setEmail(manufacturer.getEmail());
					manufacturerDb.setDescription(manufacturer.getDescription());
					manufacturerDb.setWebsite(manufacturer.getWebsite());
					manufacturerDb.setAvailableAllOverIndia(manufacturer.isAvailableAllOverIndia());
					manufacturerDb = (Manufacturer) getManufacturerDao().save(manufacturerDb);
					product.setManufacturer(manufacturerDb);
					logger.debug("Manufacturer: " + manufacturerDb);
				}
			}
      //commented supplier code on 3rd Dec 2011
			/*Supplier supplier = product.getSupplier();

			product.setSupplier(null);

						// insert manufacturer if not present already.. else pick up the existing one. find by name.
						logger.debug(product.toString());
						if (supplier != null) {
							Supplier supplierDb = supplierDaoProvider.get().findByName(supplier.getName());
							if (supplierDb == null) {
								supplier = supplierDaoProvider.get().save(supplier);
							} else {
								if (StringUtils.isNotBlank(supplier.getState()) && !StringUtils.equals(supplier.getState(), supplierDb.getState())) {
									supplierDb.setState(supplier.getState());
								}
								supplier = supplierDb;
							}
							logger.debug("Supplier: " + supplier);
						}

			product.setSupplier(supplier);*/
			/*
						Fix to not let main image id reset to NULL - Ajeet
						 */
			try {
				Product productInDB = getProductService().getProductById(product.getId());
				if (productInDB != null) {
					Long mainImageId = productInDB.getMainImageId();
					logger.debug("mainImageId[" + product.getName() + "]:" + mainImageId);
					product.setMainImageId(mainImageId);
          product.setCreateDate(productInDB.getCreateDate());
				} else {      //Newly created product
					product.setCreateDate(new Date());
				}
			} catch (Exception e) {

			}

			// inserting categories
			for (Category category : categoryList) {
				logger.debug("inserting category " + category);
				category = getCategoryService().save(category);
			}

			// to avoid duplicate primary key in the many to many mapping.. converting to set and then back to list.. eliminating duplicate category names
			product.setCategories(new ArrayList<Category>(new HashSet<Category>(categoryList)));
			//Check for Primary category and set it
			if (primaryCategory != null) {
				product.setPrimaryCategory(getCategoryService().getCategoryByName(primaryCategory.getName()));
			}

			product = getProductService().save(product);

			// product variants and product options
			for (ProductVariant productVariant : productVariants) {
				// first creating product options..

				List<ProductOption> productOptions = productVariant.getProductOptions();
				List<ProductOption> productOptionsDb = new ArrayList<ProductOption>();
				productVariant.setProductOptions(null);

				for (ProductOption productOption : productOptions) {
					// find by name and value
					ProductOption productOptionDb = getProductService().findProductOptionByNameAndValue(productOption.getName(), productOption.getValue());
					if (productOptionDb == null) {
						productOptionDb = new ProductOption(productOption.getName(), productOption.getValue());
						productOptionDb = (ProductOption) getBaseDao().save(productOption);
					}
					productOptionsDb.add(productOptionDb);
				}

				ProductVariant productVariantInDB = getProductVariantService().getVariantById(productVariant.getId());
				productVariant.setMainProductImageId(productVariantInDB != null ? productVariantInDB.getMainProductImageId() : null);
				productVariant.setMainImageId(productVariantInDB != null ? productVariantInDB.getMainImageId() : null);
				productVariant.setProductOptions(productOptionsDb);

				productVariant.setProduct(product);
				if ( productVariantInDB !=null ) {
					productVariant.setCreatedDate(productVariantInDB.getCreatedDate());
				}else{
          productVariant.setCreatedDate(new Date());
        }
				logger.debug("saving product variant : " + productVariant.getId() + "," + productVariant.getHkPrice(null));
				logger.debug("inserting product variant " + productVariant.getId() + " - " + productVariant.getProduct().getName());

				getProductVariantService().save(productVariant);
			}
		}
	}

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public ManufacturerDao getManufacturerDao() {
        return manufacturerDao;
    }

    public void setManufacturerDao(ManufacturerDao manufacturerDao) {
        this.manufacturerDao = manufacturerDao;
    }

    public SupplierDao getSupplierDao() {
        return supplierDao;
    }

    public void setSupplierDao(SupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public TaxService getTaxService() {
        return taxService;
    }

    public void setTaxService(TaxService taxService) {
        this.taxService = taxService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
    
    

	
}
