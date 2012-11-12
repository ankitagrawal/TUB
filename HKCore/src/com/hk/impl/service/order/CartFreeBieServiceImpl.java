package com.hk.impl.service.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.order.CartFreeBieDao;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.web.filter.WebContext;

@Service
public class CartFreeBieServiceImpl implements CartFreebieService {


    private String         revitalTshirt   =  "/pages/lp/revital/images/banner_tshirt.jpg";  // 630
    private String         revitalWatch    =  "/pages/lp/revital/images/banner_watch.jpg";   // 1260
    private String         revitalSunglass =  "/pages/lp/revital/images/banner_sunglass.jpg"; // 2520
    private String         revitalYogaDVD  =  "/pages/lp/revital/images/banner_yoga_dvd.jpg";
    private String         revitalMovieDVD =  "/pages/lp/revital/images/banner_free_dvd.jpg";

    @Autowired
    private CartFreeBieDao cartFreeBieDao;

    public String getFreebieBanner(Order order) {
        String imageURL = null;
        List<String> productList = new ArrayList<String>();
        productList.add("NUT410");// Revital Daily Health Supplement
        productList.add("NUT411");// Revital For Women
        productList.add("NUT412");// Revital Form Seniors
        productList.add("NUT598");// Ranbaxy Revitalite Powder
        Double cartValue = getCartValueForProducts(productList, order);

        Double rvWValue = getCartValueForVariants(Arrays.asList("NUT411-01"), order); // Revital For Women

        List<String> productVariantList = new ArrayList<String>();
        productVariantList.add("NUT410-01");// Revital Daily Health Supplement 30cps
        productVariantList.add("NUT411-01");// Revital For Women 30cps
        productVariantList.add("NUT412-01");// Revital Form Seniors 30cps

        Double rv30CpsValue = getCartValueForVariants(productVariantList, order);
        String  context         = WebContext.getRequest().getContextPath();
        if (cartValue > 2520.0) {
            imageURL = context + revitalSunglass;
        } else if (cartValue > 1260) {
            imageURL = context + revitalWatch;
        } else if (cartValue > 630) {
            imageURL = context + revitalTshirt;
        } else if (rvWValue >= 250) {
            imageURL = context + revitalYogaDVD;
        } else if (rv30CpsValue >= 240) {
            imageURL = context + revitalMovieDVD;
        }

        return imageURL;
    }

  public String getFreebieItem(ShippingOrder order) {
    String freebieItem = null;
    List<String> productList = new ArrayList<String>();
    productList.add("NUT410");//Revital Daily Health Supplement
    productList.add("NUT411");//Revital For Women
    productList.add("NUT412");//Revital Form Seniors
    productList.add("NUT598");//Ranbaxy Revitalite Powder
    Double cartValue = getCartValueForSOProducts(productList, order);

    Double rvWValue = getCartValueForSOVariants(Arrays.asList("NUT411-01"), order); //Revital For Women

    List<String> productVariantList = new ArrayList<String>();
    productVariantList.add("NUT410-01");//Revital Daily Health Supplement 30cps
    productVariantList.add("NUT411-01");//Revital For Women 30cps
    productVariantList.add("NUT412-01");//Revital Form Seniors 30cps

    Double rv30CpsValue = getCartValueForSOVariants(productVariantList, order);

    if (cartValue > 2520.0) {
      freebieItem = "Sunglass";
    } else if (cartValue > 1260) {
      freebieItem = "Reebok Watch";
    } else if (cartValue > 630) {
      freebieItem = "T-shirt";
    } else if (rvWValue >= 250) {
      freebieItem = "Yoga DVD";
    } else if (rv30CpsValue >= 240) {
      freebieItem = "Dabangg & Bodygurad 2in1 DVD";
    }

    return freebieItem;
  }

    private Double getCartValueForProducts(List<String> productList, Order order) {
        Double value = getCartFreeBieDao().getCartValueForProducts(productList, order);
        return value != null ? value : 0.0;
    }

    private Double getCartValueForVariants(List<String> productVariantList, Order order) {
        Double value = getCartFreeBieDao().getCartValueForVariants(productVariantList, order);
        return value != null ? value : 0.0;
    }

   private Double getCartValueForSOProducts(List<String> productList, ShippingOrder order) {
        Double value = getCartFreeBieDao().getCartValueForProducts(productList, order);
        return value != null ? value : 0.0;
    }

    private Double getCartValueForSOVariants(List<String> productVariantList, ShippingOrder order) {
        Double value = getCartFreeBieDao().getCartValueForVariants(productVariantList, order);
        return value != null ? value : 0.0;
    }

    public CartFreeBieDao getCartFreeBieDao() {
        return cartFreeBieDao;
    }

    public void setCartFreeBieDao(CartFreeBieDao cartFreeBieDao) {
        this.cartFreeBieDao = cartFreeBieDao;
    }
    
    

}
