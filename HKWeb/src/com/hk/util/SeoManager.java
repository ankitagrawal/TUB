package com.hk.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.lang.StringUtils;

import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.catalog.ProductService;
import com.hk.constants.core.Keys;

@Component
public class SeoManager {

    @Value("#{hkEnvProps['" + Keys.Env.shippingFreeAfter + "']}")
    private String shippingFreeAfter;

    public static final String KEY_BRAND_IN_CAT = "#brand=";
    @Autowired
    private ProductService     productService;
    @Autowired
    private BaseDao            baseDao;
    @Autowired
    private MenuHelper         menuHelper;

    public SeoData generateSeo(String id) {
	    SeoData seoData = getBaseDao().get(SeoData.class, id);
	    Product product = getProductService().getProductById(id);    	    
	    if (seoData == null) {
		    if (product != null) {
			    return generateSeoForProduct(id);
		    } else {
			    // TODO: brand names with space like Sally Hansen come as Sally+Hansen will change this when we move to ids
			    String brandName = id.replaceAll("\\+", " ");
			    if (StringUtils.isNotBlank(brandName)) {
				    String[] sArr = StringUtils.split(brandName, "||");
				    brandName = sArr[0];
			    }
			    if (id.contains(KEY_BRAND_IN_CAT)) {
				    return generateSeoForBrandInCategory(id);
			    } else if (getProductService().doesBrandExist(brandName)) {
				    return generateSeoForBrand(id);
			    } else {
				    return generateSeoForCategory(id);
			    }
		    }
	    } else {
            if (product != null) {
                return checkAndSetAllSeoTagsNotFilledForProducts(seoData, product);
            } else {
                MenuNode node = menuHelper.getMenuNode(id);
                if (node != null) {
                    return checkAndSetAllSeoTagsNotFilledForCategory(seoData, node.getName());
                } else {
                    return seoData;
                }
            }
        }
    }

    private SeoData checkAndSetAllSeoTagsNotFilledForProducts(SeoData seoData, Product product) {
	      String productName = "";
	      if(product !=  null){
		      productName = product.getName();
	      }
        if (seoData.getTitle() == null) {
            StringBuffer title = new StringBuffer(productName);
            title.append("| Buy Online ").append(productName).append(" in India");
            // seoData.setTitle("${product.name} | Buy Online ${product.name} in India");
            seoData.setTitle(title.toString());
        }
        if (seoData.getMetaKeyword() == null) {
            seoData.setMetaKeyword("buy " + productName + " , " + productName + " india, buy " + productName + " india, buy " + productName + " online, buy " + productName + " online india");
        }
        if (seoData.getMetaDescription() == null) {
            seoData.setMetaDescription("Buy " + productName + " Online in India at HealthKart.com. Free home delivery across India on orders above Rs. "+shippingFreeAfter);
        }
        return seoData;
    }

    private SeoData checkAndSetAllSeoTagsNotFilledForCategory(SeoData seoData, String name) {
        if (seoData.getTitle() == null) {
            seoData.setTitle(name + " | Buy Online " + name + " in India");
        }
        if (seoData.getMetaKeyword() == null) {
            seoData.setMetaKeyword("buy " + name + ", " + name + " india, buy " + name + " india, buy " + name + " online, buy " + name + " online india");
        }
        if (seoData.getMetaDescription() == null) {
            seoData.setMetaDescription("Buy " + name + " Online in India at HealthKart.com. Free home delivery across India on orders above Rs. "+shippingFreeAfter);
        }
        return seoData;
    }

    private SeoData generateSeoForProduct(String id) {

        SeoData seoData = new SeoData();
        Product product = getProductService().getProductById(id);
	      String productName = "";
	      if(product != null){
		      productName = product.getName();
	      }
        seoData.setId(id);
        seoData.setH1(product.getName());
        seoData.setTitle( productName + " | Buy Online " + productName + " in India");
        seoData.setMetaKeyword(productName + ", buy " + productName + ", buy " + productName + " in india, buy " + productName + " online");
        seoData.setMetaDescription("Buy " + productName + " Online in India at HealthKart.com. Free home delivery across India on orders above Rs. "+shippingFreeAfter);
        seoData.setDescriptionTitle("About " + productName);

        // seoData.metaDescription=product.overview;
        return seoData;
    }

    private SeoData generateSeoForCategory(String id) {

        SeoData seoData = new SeoData();
        MenuNode node = menuHelper.getMenuNode(id);
        if (node == null)
            return seoData;
        String name = node.getName();
        seoData.setId(id);
        seoData.setH1(name);
        seoData.setTitle(name + " | Buy Online " + name + " in India");
        seoData.setMetaKeyword("buy " + name + ", " + name + " india, buy " + name + " india, buy " + name + " online, buy " + name + " online india");
        seoData.setMetaDescription("Buy " + name + " Online in India at HealthKart.com. Free home delivery across India on orders above Rs. "+shippingFreeAfter);
        seoData.setDescriptionTitle("About " + name + "");

        return seoData;
    }

    private SeoData generateSeoForBrand(String brandName) {
        SeoData seoData = new SeoData();
        seoData.setId(brandName);
	    if (StringUtils.isNotBlank(brandName)) {
		    String[] sArr = StringUtils.split(brandName, "||");
		    brandName = sArr[0];
	    }
        seoData.setH1(brandName);
        seoData.setTitle(brandName);
        seoData.setMetaKeyword("buy " + brandName + " products, " + brandName + " products shop online ");
        seoData.setMetaDescription("Buy " + brandName + " products online from India's most reliable e-health store. ");
        seoData.setDescriptionTitle("About " + brandName);

        return seoData;
    }

    private SeoData generateSeoForBrandInCategory(String keyForBrandInCat) {
        int keyIdx = keyForBrandInCat.indexOf(KEY_BRAND_IN_CAT);
        String categoryURLFragment = keyForBrandInCat.substring(0, keyIdx);
        String brandName = keyForBrandInCat.substring(keyIdx + KEY_BRAND_IN_CAT.length(), keyForBrandInCat.length());

        SeoData brandInCategorySeoData = new SeoData();

        SeoData categorySeoData = getBaseDao().get(SeoData.class, categoryURLFragment);
        SeoData brandSeoData = getBaseDao().get(SeoData.class, brandName);
        brandInCategorySeoData.setId(keyForBrandInCat);

        if (categorySeoData == null) {
            categorySeoData = generateSeoForCategory(categoryURLFragment);
        }
        if (brandSeoData == null) {
            brandSeoData = generateSeoForBrand(brandName);
        }

        brandInCategorySeoData.setH1(brandName);
        brandInCategorySeoData.setTitle(brandName);
        brandInCategorySeoData.setMetaKeyword(brandSeoData.getMetaKeyword());
        brandInCategorySeoData.setMetaDescription(brandSeoData.getMetaDescription());
        brandInCategorySeoData.setDescriptionTitle("About " + brandName);

        if (brandSeoData.getDescription() != null && categorySeoData.getDescription() != null) {
            brandInCategorySeoData.setDescription(brandSeoData.getDescription().concat(categorySeoData.getDescription()));
        } else if (categorySeoData.getDescription() != null) {
            brandInCategorySeoData.setDescription(categorySeoData.getDescription());
        } else if (brandSeoData.getDescription() != null) {
            brandInCategorySeoData.setDescription(brandSeoData.getDescription());
        }

        return brandInCategorySeoData;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public MenuHelper getMenuHelper() {
        return menuHelper;
    }

    public void setMenuHelper(MenuHelper menuHelper) {
        this.menuHelper = menuHelper;
    }

}
