package com.hk.util;

import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.SeoData;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.service.ProductService;

@Component
public class SeoManager {

    public static final String KEY_BRAND_IN_CAT = "#brand=";

    private ProductService     productService;
    private BaseDao            baseDao;

    private MenuHelper         menuHelper;

    public SeoData generateSeo(String id) {
        SeoData seoData = getBaseDao().get(SeoData.class, id);
        Product product = getProductService().getProductById(id);

        // TODO: brand names with space like Sally Hansen come as Sally+Hansen will change this when we move to ids
        // instead of strings
        String brandName = id.replaceAll("\\+", " ");
        if (seoData == null) {
            if (product != null) {
                return generateSeoForProduct(id);
            } else if (id.contains(KEY_BRAND_IN_CAT)) {
                return generateSeoForBrandInCategory(id);
            } else if (getProductService().doesBrandExist(brandName)) {
                return generateSeoForBrand(id);
            } else {
                return generateSeoForCategory(id);
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
    if (seoData.getTitle() == null) {
      seoData.setTitle("${product.name} | Buy Online ${product.name} in India");
    }
    if (seoData.getMetaKeyword() == null) {
      seoData.setMetaKeyword("buy ${product.name}, ${product.name} india, buy ${product.name} india, buy ${product.name} online, but ${product.name} online india");
    }
    if (seoData.getMetaDescription() == null) {
      seoData.setMetaDescription("Buy ${product.name} Online in India at HealthKart.com. Free home delivery across India on orders above Rs. 250");
    }
    return seoData;
  }

    private SeoData checkAndSetAllSeoTagsNotFilledForCategory(SeoData seoData, String name) {
    if (seoData.getTitle() == null) {
      seoData.setTitle( "${name} | Buy Online ${name} in India");
    }
    if (seoData.getMetaKeyword() == null) {
      seoData.setMetaKeyword("buy ${name}, ${name} india, buy ${name} india, buy ${name} online, buy ${name} online india");
    }
    if (seoData.getMetaDescription() == null) {
      seoData.setMetaDescription( "Buy ${name} Online in India at HealthKart.com. Free home delivery across India on orders above Rs. 250");
    }
    return seoData;
  }

    private SeoData generateSeoForProduct(String id) {

    SeoData seoData = new SeoData();
    Product product = getProductService().getProductById(id);
    seoData.setId(id);
    seoData.setH1(product.getName());
    seoData.setTitle( "${product.name} | Buy Online ${product.name} in India");
    seoData.setMetaKeyword("buy ${product.name}, ${product.name} india, buy ${product.name} india, buy ${product.name} online, but ${product.name} online india");
    seoData.setMetaDescription( "Buy ${product.name} Online in India at HealthKart.com. Free home delivery across India on orders above Rs. 250");
    seoData.setDescriptionTitle( "About ${product.name}");

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
    seoData.setTitle("${name} | Buy Online ${name} in India");
    seoData.setMetaKeyword( "buy ${name}, ${name} india, buy ${name} india, buy ${name} online, buy ${name} online india");
    seoData.setMetaDescription( "Buy ${name} Online in India at HealthKart.com. Free home delivery across India on orders above Rs. 250");
    seoData.setDescriptionTitle( "About ${name}");

    return seoData;
  }

    private SeoData generateSeoForBrand(String brandName) {
        SeoData seoData = new SeoData();
        seoData.setId (brandName);
        seoData.setH1(brandName);
        seoData.setTitle( brandName);
        seoData.setMetaKeyword("buy ${brandName} products, ${brandName} products shop online ");
        seoData.setMetaDescription("Buy ${brandName} products online from India's most reliable e-health store. ");
        seoData.setDescriptionTitle("About ${brandName}");

        return seoData;
    }

    private SeoData generateSeoForBrandInCategory(String keyForBrandInCat) {
        int keyIdx = keyForBrandInCat.indexOf(KEY_BRAND_IN_CAT);
        String categoryURLFragment = keyForBrandInCat.substring(0, keyIdx);
        String brandName = keyForBrandInCat.substring(keyIdx + KEY_BRAND_IN_CAT.length(), keyForBrandInCat.length());

        SeoData brandInCategorySeoData = new SeoData();

        SeoData categorySeoData = getBaseDao().get(SeoData.class, categoryURLFragment);
        SeoData brandSeoData = getBaseDao().get(SeoData.class, brandName);
        brandInCategorySeoData.setId( keyForBrandInCat);

        if (categorySeoData == null) {
            categorySeoData = generateSeoForCategory(categoryURLFragment);
        }
        if (brandSeoData == null) {
            brandSeoData = generateSeoForBrand(brandName);
        }

        brandInCategorySeoData.setH1( brandName);
        brandInCategorySeoData.setTitle(brandName);
        brandInCategorySeoData.setMetaKeyword(brandSeoData.getMetaKeyword());
        brandInCategorySeoData.setMetaDescription(brandSeoData.getMetaDescription());
        brandInCategorySeoData.setDescriptionTitle("About ${brandName} ");

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
