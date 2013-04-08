package com.hk.web.action.core.b2b;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.XslParser;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.dto.B2BProduct;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.B2BOrderService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.stripesstuff.plugin.security.Secure;

import java.io.File;
import java.util.*;

/**
 * @author Nihal
 */
@Secure(hasAnyPermissions = {PermissionConstants.PLACE_B2B_ORDER}, authActionBean = AdminPermissionAction.class)
public class B2BCartAction extends BaseAction {

  @Autowired
  private ProductVariantService productVariantService;

  private Order order;
  @Autowired
  UserDao userDao;
  UserManager userManager;
  @Autowired
  OrderManager orderManager;
  @Autowired
  InventoryService inventoryService;
  Set<CartLineItem> cartLineItems;
  @Autowired
  private B2BOrderService b2bOrderService;
  private boolean cFormAvailable;
  private List<B2BProduct> b2bProductListFromExcel;

  private FileBean fileBean;

  private String productVariantId;

  private List<B2BProduct> b2bInvalidProductList;

  @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
  String adminUploadsPath;

  @Autowired
  XslParser xslParser;


  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    User user = null;
    if (getPrincipal() != null) {
      user = userDao.getUserById(getPrincipal().getId());
      if (user != null) {
        order = orderManager.getOrCreateOrder(user);
        cartLineItems = order.getCartLineItems();
        cFormAvailable = getB2bOrderService().checkCForm(order);
      }

    }

    return new ForwardResolution("/pages/b2b/b2bBulkOrder.jsp");
  }

  public Resolution getPVDetails() {
    Map<Object, Object> dataMap = new HashMap<Object, Object>();
    HealthkartResponse healthkartResponse = null;
    if (StringUtils.isNotBlank(productVariantId)) {
      ProductVariant pv = productVariantService.getVariantById(productVariantId);
      if (pv != null) {
        try {
          dataMap.put("variant", pv);
          dataMap.put("product", pv.getProduct().getName());
          dataMap.put("options", pv.getOptionsCommaSeparated());
          dataMap.put("imageUrl", null);
          healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant",
              dataMap);
          Long value = inventoryService.getAvailableUnbookedInventory(pv);
          dataMap.put("inventory", value);

        } catch (Exception e) {
          healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, e.getMessage(),
              dataMap);
        }
      } else {
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
            "Invalid Product VariantID", dataMap);
      }
    } else {
      healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID",
          dataMap);
    }

    noCache();
    return new JsonResolution(healthkartResponse);
  }

  public List<B2BProduct> verifyProductList(List<B2BProduct> b2bProductList) {
    b2bInvalidProductList = new ArrayList<B2BProduct>();
    for (B2BProduct b2bProduct : b2bProductList) {
      if (productVariantService.getVariantById(b2bProduct.getProductId()) == null || b2bProduct.getQuantity() < 0) {
        b2bInvalidProductList.add(b2bProduct);
      }
    }

    return b2bInvalidProductList;
  }

  @SuppressWarnings("unchecked")
  public Resolution parseOnly() {

    String excelFilePath = adminUploadsPath + "/b2bOrder/" + "b2b_" + System.currentTimeMillis() + ".xls";
    File excelFile = new File(excelFilePath);
    excelFile.getParentFile().mkdirs();

    HealthkartResponse healthkartResponse;
    Map dataMap = new HashMap();
    b2bProductListFromExcel = new ArrayList<B2BProduct>();
    try {
      fileBean.save(excelFile);
      b2bProductListFromExcel = getXslParser().parseExcelAndGetProductList(excelFile);
      dataMap.put("b2bProductListFromExcel", b2bProductListFromExcel);

      if (verifyProductList(b2bProductListFromExcel).size() == 0) {
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK,
            "These are items to be added in the cart", dataMap);
        noCache();
        return new ForwardResolution("/pages/b2b/b2bExcelUpload.jsp");
      } else {
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR,
            "There are some invalid variant Ids in the list", dataMap);
        noCache();
        return new ForwardResolution("/pages/b2b/b2bExcelUpload.jsp");
      }
    } catch (Exception e) {
      return new ForwardResolution("/pages/b2b/b2bExcelUpload.jsp");
    }


  }

  @ValidationMethod(on = "parseOnly")
  public void validateOnParse() {
    if (fileBean == null) {
      getContext().getValidationErrors().add("1", new SimpleError("Please select a file to upload."));
    }
  }


  public ProductVariantService getProductVariantService() {
    return productVariantService;
  }

  public void setProductVariantService(ProductVariantService productVariantService) {
    this.productVariantService = productVariantService;
  }


  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Set<CartLineItem> getCartLineItems() {
    return cartLineItems;
  }

  public void setCartLineItems(Set<CartLineItem> cartLineItems) {
    this.cartLineItems = cartLineItems;
  }

  public List<B2BProduct> getB2bProductListFromExcel() {
    return b2bProductListFromExcel;
  }

  public void setB2bProductListFromExcel(List<B2BProduct> b2bProductListFromExcel) {
    this.b2bProductListFromExcel = b2bProductListFromExcel;
  }

  public FileBean getFileBean() {
    return fileBean;
  }

  public void setFileBean(FileBean fileBean) {
    this.fileBean = fileBean;
  }

  public List<B2BProduct> getB2bInvalidProductList() {
    return b2bInvalidProductList;
  }

  public void setB2bInvalidProductList(List<B2BProduct> b2bInvalidProductList) {
    this.b2bInvalidProductList = b2bInvalidProductList;
  }

  public String getProductVariantId() {
    return productVariantId;
  }

  public void setProductVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
  }

  public B2BOrderService getB2bOrderService() {
    return b2bOrderService;
  }

  public void setB2bOrderService(B2BOrderService b2bOrderService) {
    this.b2bOrderService = b2bOrderService;
  }

  public boolean isCFormAvailable() {
    return cFormAvailable;
  }

  public boolean getCFormAvailable() {
    return cFormAvailable;
  }

  public void setCFormAvailable(boolean cFormAvailable) {
    this.cFormAvailable = cFormAvailable;
  }

  public XslParser getXslParser() {
    return xslParser;
  }
}
