package com.hk.web.action.core.catalog.category;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.controller.StripesFilter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.content.HeadingProduct;
import com.hk.domain.content.PrimaryCategoryHeading;
import com.hk.pact.dao.content.PrimaryCategoryHeadingDao;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.pact.service.homeheading.HeadingProductService;
import com.hk.web.action.HomeAction;
import com.hk.web.filter.WebContext;

@Component
public class PrimaryCategoryHeadingAction extends BaseAction {

  private static Logger                          logger = Logger.getLogger(PrimaryCategoryHeadingAction.class);
  String                                         headingName;
  Integer                                        headingRanking;
  PrimaryCategoryHeading                         heading;
  List<Product>                                  products;
  List<Integer>                                  ranks;
  Category                                       category;
  List<PrimaryCategoryHeading>                   headingList;
  //    HashMap<PrimaryCategoryHeading, List<Product>> headingHasProducts;
  Long                                           headingId;
  List<HeadingProduct>                           headingProducts = new ArrayList<HeadingProduct>(0);
  String                                           productId;
  @Autowired
  PrimaryCategoryHeadingDao                      primaryCategoryHeadingDao;
  @Autowired
  private CategoryService                        categoryService;
  @Autowired
  private HeadingProductService                  headingProductService;
  @Autowired
  private ProductService productService;


  public Resolution create() throws Exception {
    category = heading.getCategory();
    headingList = primaryCategoryHeadingDao.getHeadingsByCategory(category);
    for (PrimaryCategoryHeading CategoryHeading : headingList) {
      if (CategoryHeading.getName().equals(heading.getName())) {
        logger.debug("heading already existed on the category page");
        addRedirectAlertMessage(new SimpleMessage("Heading already exists!"));
        return new ForwardResolution("/pages/createPrimaryCategoryHeading.jsp").addParameter("category", category.getName());
      }
    }
    if (heading.getName().length() > 200) {
      logger.debug("heading name exceeds the limit.so going back to create page");
      addRedirectAlertMessage(new SimpleMessage("Heading name exceeds the limit!"));
      return new ForwardResolution("/pages/createPrimaryCategoryHeading.jsp").addParameter("category", category.getName());
    } else if (heading.getLink() != null) {
      String host = "http://".concat(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());
      String contextPath = WebContext.getRequest().getContextPath();
      String urlString = host.concat(contextPath);
      String linkValue = urlString.concat(heading.getLink());
      // logger.debug(StripesFilter.getConfiguration().getSslConfiguration().getSecureHost());
      // logger.debug(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());

      if (!BaseUtils.remoteFileExists(linkValue)) {
        logger.debug("heading link " + linkValue + " invalid!");
        addRedirectAlertMessage(new SimpleMessage("PLEASE ENTER LINK CORRECTLY .. ENTERED LINK DOES NOT EXIST"));
        return new ForwardResolution("/pages/createPrimaryCategoryHeading.jsp").addParameter("category", category.getName());
      }
    }
    heading = (PrimaryCategoryHeading) primaryCategoryHeadingDao.save(heading);
    addRedirectAlertMessage(new SimpleMessage("new heading created.heading id: " + heading.getId() + " heading name: " + heading.getName()));
    return new ForwardResolution(PrimaryCategoryHeadingAction.class, "addPrimaryCategoryHeadingProducts").addParameter("category", category.getName());
  }

  public Resolution deletePrimaryCategoryHeading() {
    heading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class, heading.getId());
    logger.debug("heading id for the heading to be deleted: " + heading.getId() + " and heading name: " + heading.getName());

    // products related to heading to be deleted have to be set null else a not-null exception for products' primary
    // category arises
    primaryCategoryHeadingDao.delete(heading);
    addRedirectAlertMessage(new SimpleMessage("heading deleted."));
    return new ForwardResolution(CategoryAction.class, "editPrimaryCategoryHeadings").addParameter("category", heading.getCategory().getName());
  }

  public Resolution editPrimaryCategoryHeading() {
    heading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class, heading.getId());
    logger.debug("heading id for the heading to be edited: " + heading.getId() + " and heading name: " + heading.getName());
    return new ForwardResolution("/pages/editPrimaryCategoryHeadingAttributes.jsp");
  }

  public Resolution savePrimaryCategoryHeadingAttributes() throws Exception {
    category = heading.getCategory();
    headingList = primaryCategoryHeadingDao.getHeadingsByCategory(category);
    if (headingList.contains(heading)) {
      logger.debug("heading already existed on the category page");
      addRedirectAlertMessage(new SimpleMessage("Heading already exists!"));
      return new ForwardResolution("/pages/editPrimaryCategoryHeadingAttributes.jsp");
    } else if (heading.getName().length() > 200) {
      logger.debug("heading name exceeds the limit.so going back to edit page");
      addRedirectAlertMessage(new SimpleMessage("Heading name exceeds the limit!"));
      return new ForwardResolution("/pages/editPrimaryCategoryHeadingAttributes.jsp");
    } else if (heading.getRanking() < 1) {
      logger.debug("Minimum value for ranking is 1");
      addRedirectAlertMessage(new SimpleMessage("Minimum value for ranking is 1.Kindly enter a valid ranking!"));
      return new ForwardResolution("/pages/editPrimaryCategoryHeadingAttributes.jsp");
    } else if (heading.getLink() != null) {
      String host = "http://".concat(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());
      String contextPath = WebContext.getRequest().getContextPath();
      String urlString = host.concat(contextPath);
      String linkValue = urlString.concat(heading.getLink());
      logger.info("heading link " + linkValue);
      // logger.debug(StripesFilter.getConfiguration().getSslConfiguration().getSecureHost());
      // logger.debug(StripesFilter.getConfiguration().getSslConfiguration().getUnsecureHost());

      if (!BaseUtils.remoteFileExists(linkValue)) {
        logger.debug("heading link " + linkValue + " invalid!");
        addRedirectAlertMessage(new SimpleMessage("PLEASE ENTER LINK CORRECTLY .. ENTERED LINK DOES NOT EXIST"));
        return new ForwardResolution("/pages/editPrimaryCategoryHeadingAttributes.jsp");
      }
    }
    logger.debug("saving heading attributes for heading id: " + heading.getId() + " heading name: " + heading.getName());
    heading = (PrimaryCategoryHeading) primaryCategoryHeadingDao.save(heading);
    return new ForwardResolution(CategoryAction.class, "editPrimaryCategoryHeadings").addParameter("category", heading.getCategory().getName());
  }

  public Resolution addPrimaryCategoryHeading() {
    logger.debug("adding new heading for category: " + category.getName());
    return new ForwardResolution("/pages/createPrimaryCategoryHeading.jsp").addParameter("category", category.getName());
  }

  public Resolution addPrimaryCategoryHeadingProducts() {
//        heading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class, heading.getId());
    headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
    heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
    logger.debug("adding products for heading id: " + heading.getId() + " name: " + heading.getName());
    return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp").addParameter("heading",heading.getId());
  }

  public Resolution savePrimaryCategoryHeadingProducts() {
//        heading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class, heading.getId());
//        logger.debug("saving products for heading id: " + heading.getId() + " name: " + heading.getName());
//        List<Product> productsList = heading.getProducts();
//        category = heading.getCategory();
//        category = getCategoryService().getCategoryByName(category.getName());
    if (products != null) {
      int i = 0;
      String str = "";
      for (Product product : products) {
        if(product!=null)
        {
          product = getProductService().getProductById(product.getId());
          HeadingProduct headingProduct = getHeadingProductService().getHeadingProductByHeadingAndProductId(heading,product.getId());
          if(headingProduct!=null){
            logger.debug("product can not be displayed more than once");
//            headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
//            heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
            str += product.getId() + " is added more than once in the list!<br>";
//            addRedirectAlertMessage(new SimpleMessage(product.getId() + " is added more than once in the list!"));
//            return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
          }else if (product.isHidden()) {
//            headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
//            heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
            str += product.getId() + " has isHidden property set to true!<br>";
//            addRedirectAlertMessage(new SimpleMessage(product.getId() + " has isHidden property set to true!"));
//            return new RedirectResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
          } else if (product.isOutOfStock()) {
//            headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
//            heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
            str += product.getId() + " has isOutOfStock property set to true!<br>";
//            addRedirectAlertMessage(new SimpleMessage(product.getId() + " has isOutOfStock property set to true!"));
//            return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
          } else if (product.isDeleted()) {
            logger.debug("product to be added has isDeleted property set to true");
//            headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
//            heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
            str += product.getId() + " has isDeleted property set to true!<br>";
//            addRedirectAlertMessage(new SimpleMessage(product.getId() + " has isDeleted property set to true!"));
//            return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
          } else if (Boolean.TRUE.equals(product.isGoogleAdDisallowed())) {
            logger.debug("product to be added has isGoogleAdDisallowed property set to true");
//            headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
//            heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
            str += product.getId() + " has isGoogleAdDisallowed set to true!<br>";
//            addRedirectAlertMessage(new SimpleMessage(product.getId() + " has isGoogleAdDisallowed set to true!"));
//            return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
          } else {
            HeadingProduct headingProduct1 = new HeadingProduct();
            headingProduct1.setProduct(product);
            if(heading!=null){
              headingProduct1.setHeading(heading);
            }
            else {
              addRedirectAlertMessage(new SimpleMessage("Heading Id can't be null, please try again later"));
              return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
            }
            headingProduct1.setRank(ranks.get(i));
            getHeadingProductService().save(headingProduct1);
          }
          i++;
        }
      }
      if(!str.equals("")) {
        headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
        heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
        addRedirectAlertMessage(new SimpleMessage("Following Error came during adding Products :-<br>"+str));
        return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
      }
//            heading.setProducts(productsList);
//            primaryCategoryHeadingDao.save(heading);
    }
    else{
      headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
      addRedirectAlertMessage(new SimpleMessage("Couldn't save, please try again later !!!!!"));
      return new ForwardResolution("/pages/addPrimaryCategoryHeadingProducts.jsp");
    }
    heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
    category = heading.getCategory();
    if (!(category.getName().equals("home"))) {
      return new RedirectResolution(CategoryAction.class, "pre").addParameter("category", category.getName());
    } else {
      return new RedirectResolution(HomeAction.class, "pre").addParameter("category", category.getName());
    }
  }

  public Resolution editPrimaryCategoryHeadingProducts() {
    headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
    heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
    logger.debug("Editing products for heading id: " + heading.getId() + " name: " + heading.getName());
    return new ForwardResolution("/pages/editPrimaryCategoryHeadingProducts.jsp");
  }

  public Resolution deleteSelectedPrimaryCategoryHeadingProducts() {

//        heading = primaryCategoryHeadingDao.get(PrimaryCategoryHeading.class, heading.getId());
//           getHeadingProductService().getHeadingProductsByHeadingId(heading);
//        List<Product> productsList = heading.getProducts();
//        category = heading.getCategory();
//        if (products != null) {
//            // for (Product product : products) {
//            // productsList.remove(product);
//            // }
//            productsList.removeAll(products);
//        }
//        heading.setProducts(productsList);
//        primaryCategoryHeadingDao.save(heading);
    heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
    category = heading.getCategory();
    HeadingProduct headingProduct = null;
    if(heading!=null && products!=null){
      for(Product product : products){
        headingProduct = getHeadingProductService().getHeadingProductByHeadingAndProductId(heading,product.getId());
        if(headingProduct!=null){
          getHeadingProductService().delete(headingProduct);
          addRedirectAlertMessage(new SimpleMessage("Changes saved Successfully"));
        }
      }
    }
    else{
      headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
      addRedirectAlertMessage(new SimpleMessage("There came an Error, please Try again later!!!!"));
      return new ForwardResolution("/pages/editPrimaryCategoryHeadingProducts.jsp");
    }
    if (!(category.getName().equals("home"))) {
      noCache();
      return new RedirectResolution(CategoryAction.class, "pre").addParameter("category.name", category.getName());
    } else {
      noCache();
      return new RedirectResolution(HomeAction.class, "pre").addParameter("category", category.getName());
    }
  }

  public Resolution editSelectedPrimaryCategoryHeadingProducts(){
    heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
    HeadingProduct headingProduct = null;
    if(products!=null){
      for(Product product : products){
        headingProduct = getHeadingProductService().getHeadingProductByHeadingAndProductId(heading,product.getId());
        headingProducts.add(headingProduct);
      }
    }
    return new ForwardResolution("/pages/editSelectedPrimaryCategoryHeadingProducts.jsp");
  }

  public Resolution saveSelectedPrimaryCategoryHeadingProducts(){
    heading = primaryCategoryHeadingDao.getHeadingById(heading.getId());
    category = heading.getCategory();
    HeadingProduct headingProduct = null;
    if(products!=null && ranks!=null){
      int i = 0;
      for(Product product : products){
        Product product1 = getProductService().getProductById(product.getId());
        headingProduct = getHeadingProductService().getHeadingProductByHeadingAndProductId(heading,product.getId());
        if(headingProduct!=null){
          headingProduct.setRank(ranks.get(i));
          getHeadingProductService().save(headingProduct);
        }
        i++;
      }
    }
    else{
      headingProducts = getHeadingProductService().getHeadingProductsByHeadingId(heading);
      addRedirectAlertMessage(new SimpleMessage("There came an Error, please Try again later!!!!"));
      return new ForwardResolution("/pages/editPrimaryCategoryHeadingProducts.jsp");
    }
    if (!(category.getName().equals("home"))) {
      noCache();
      return new RedirectResolution(CategoryAction.class, "pre").addParameter("category.name", category.getName());
    } else {
      noCache();
      return new RedirectResolution(HomeAction.class, "pre").addParameter("category.name", category.getName());
    }
  }
  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public PrimaryCategoryHeading getHeading() {
    return heading;
  }

  public void setHeading(PrimaryCategoryHeading heading) {
    this.heading = heading;
  }

  public Long getHeadingId() {
    return headingId;
  }

  public void setHeadingId(Long headingId) {
    this.headingId = headingId;
  }

  public String getHeadingName() {
    return this.headingName;
  }

  public void setHeadingName(String headingName) {
    this.headingName = headingName;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public Integer getHeadingRanking() {
    return this.headingRanking;
  }

  public void setHeadingRanking(Integer headingRanking) {
    this.headingRanking = headingRanking;
  }

  public CategoryService getCategoryService() {
    return categoryService;
  }

  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  public HeadingProductService getHeadingProductService() {
    return headingProductService;
  }

  public List<HeadingProduct> getHeadingProducts() {
    return headingProducts;
  }

  public void setHeadingProducts(List<HeadingProduct> headingProducts) {
    this.headingProducts = headingProducts;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public List<Integer> getRanks() {
    return ranks;
  }

  public void setRanks(List<Integer> ranks) {
    this.ranks = ranks;
  }

  public ProductService getProductService() {
    return productService;
  }
}