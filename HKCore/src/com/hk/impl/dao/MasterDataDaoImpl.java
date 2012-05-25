package com.hk.impl.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hk.constants.catalog.product.EnumProductVariantPaymentType;
import com.hk.constants.core.EnumRole;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.TicketStatus;
import com.hk.domain.TicketType;
import com.hk.domain.review.ReviewStatus;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.CartLineItemType;
import com.hk.domain.core.EmailType;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.PaymentStatus;
import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.domain.core.ProductVariantServiceType;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.core.Surcharge;
import com.hk.domain.core.Tax;
import com.hk.domain.courier.BoxSize;
import com.hk.domain.courier.Courier;
import com.hk.domain.inventory.GrnStatus;
import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.MasterDataDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.marketing.MarketingService;

@Repository
public class MasterDataDaoImpl implements MasterDataDao {

  @Autowired
  private BaseDao baseDao;
  @Autowired
  private UserService userService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private MarketingService marketingService;

  public List<PaymentStatus> getPaymentStatusList() {
    return getBaseDao().getAll(PaymentStatus.class);
    // return paymentStatusDaoProvider.get().listAll();
  }

  public List<PaymentMode> getPaymentModeList() {
    return getBaseDao().getAll(PaymentMode.class);
    // return paymentModeDaoProvider.get().listAll();
  }

  public List<OrderStatus> getOrderStatusList() {
    return getBaseDao().getAll(OrderStatus.class);
    // return orderStatusDaoProvider.get().listAll();
  }

  public List<Courier> getCourierList() {
    return getBaseDao().getAll(Courier.class);
    // return courierDaoProvider.get().listAll();
  }

  public List<PaymentMode> getpaymentModesForReconciliationReport() {
    return Arrays.asList(EnumPaymentMode.TECHPROCESS.asPaymenMode(), EnumPaymentMode.COD.asPaymenMode(), EnumPaymentMode.CITRUS.asPaymenMode());
  }

  public List<ReconciliationStatus> getReconciliationStatus() {
    return Arrays.asList(EnumReconciliationStatus.DONE.asReconciliationStatus(), EnumReconciliationStatus.PENDING.asReconciliationStatus());
  }

  public List<CartLineItemType> getLineItemTypeList() {
    return getBaseDao().getAll(CartLineItemType.class);
    // return cartLineItemTypeDaoProvider.get().listAll();
  }

  public List<CancellationType> getCancellationTypeList() {
    return getBaseDao().getAll(CancellationType.class);
    // return cancellationTypeDaoProvider.get().listAll();
  }

  public List<RewardPointStatus> getRewardPointStatusList() {
    return getBaseDao().getAll(RewardPointStatus.class);
    // return rewardPointStatusDaoProvider.get().listAll();
  }

  public List<Tax> getTaxList() {
    return getBaseDao().getAll(Tax.class);
    // return taxDaoProvider.get().listAll();
  }

  public List<Manufacturer> getManufacturerList() {
    return getBaseDao().getAll(Manufacturer.class);
    // return manufacturerDaoProvider.get().listAll();
  }

  public List<TicketType> getTicketTypeList() {
    return getBaseDao().getAll(TicketType.class);
    // return ticketTypeDaoProvider.get().listAll();
  }

  public List<TicketStatus> getTicketStatusList() {
    return getBaseDao().getAll(TicketStatus.class);
    // return ticketStatusDaoProvider.get().listAll();
  }

  public List<User> getTicketAdminList() {
    return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.TICKETADMIN));
  }

  public List<Category> getTopLevelCategoryList() {
    return getCategoryService().getPrimaryCategories();
  }

  public List<Category> getMarketExpenseCategoriesList() {
    return getMarketingService().marketExpenseCategoriesList();
  }

  public List<AffiliateCategory> getallAffiliateCategories() {
    return getBaseDao().getAll(AffiliateCategory.class);
  }

  public List<PaymentMode> getPaymentModes() {
    return getBaseDao().getAll(PaymentMode.class);
    // return paymentModeDaoProvider.get().listAll();
  }

  public List<BoxSize> getBoxSizes() {
    return getBaseDao().getAll(BoxSize.class);
    // return boxSizeDaoProvider.get().listAll();
  }

  public List<RewardPointMode> getRewardPointModes() {
    return getBaseDao().getAll(RewardPointMode.class);
    // return rewardPointModeDaoProvider.get().listAll();
  }

  public List<ProductVariantPaymentType> getPaymentTypes() {
    return Arrays.asList(EnumProductVariantPaymentType.Default.asProductVariantPaymentType(), EnumProductVariantPaymentType.Prepaid.asProductVariantPaymentType(),
        EnumProductVariantPaymentType.Postpaid.asProductVariantPaymentType());
  }

  public List<ProductVariantServiceType> getServiceTypes() {
    return getBaseDao().getAll(ProductVariantServiceType.class);
    // return serviceTypeDaoProvider.get().listAll();
  }

  public List<PurchaseOrderStatus> getPurchaseOrderStatusList() {
    return getBaseDao().getAll(PurchaseOrderStatus.class);
    // return purchaseOrderStatusDaoProvider.get().listAll();
  }

  public List<GrnStatus> getGrnStatusList() {
    return getBaseDao().getAll(GrnStatus.class);
    // return grnStatusDaoProvider.get().listAll();
  }

  public List<User> getApproverList() {
    // return getBaseDao().getAll(User.class);

    return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.PO_APPROVER));
    // return userDaoProvider.get().findByRole(roleDaoProvider.get().find(EnumRole.PO_APPROVER.getRoleName()));
  }

  public List<User> getCreatorList() {
    return getUserService().findByRole(getRoleService().getRoleByName(EnumRole.CATEGORY_MANAGER));
  }

  public List<DebitNoteStatus> getDebitNoteStatusList() {
    return getBaseDao().getAll(DebitNoteStatus.class);
    // return debitNoteStatusDaoProvider.get().listAll();
  }

  public List<PurchaseInvoiceStatus> getPurchaseInvoiceStatusList() {
    return getBaseDao().getAll(PurchaseInvoiceStatus.class);
    // return purchaseInvoiceStatusDaoProvider.get().listAll();
  }

  public List<Surcharge> getSurchargeList() {
    return getBaseDao().getAll(Surcharge.class);
    // return surchargeDaoProvider.get().listAll();
  }

  public List<ReconciliationType> getReconciliationTypeList() {
    return getBaseDao().getAll(ReconciliationType.class);
    // return reconciliationTypeDaoProvider.get().listAll();
  }

  public List<EmailType> getEmailTypeList() {
    return getBaseDao().getAll(EmailType.class);
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public CategoryService getCategoryService() {
    return categoryService;
  }

  public void setCategoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  public RoleService getRoleService() {
    return roleService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public MarketingService getMarketingService() {
    return marketingService;
  }

  public void setMarketingService(MarketingService marketingService) {
    this.marketingService = marketingService;
  }

  public List<ShippingOrderStatus> getSOStatusForShipmentDetailsList() {
    return EnumShippingOrderStatus.getStatusForChangingShipmentDetails();
  }

  public List<ReviewStatus> getReviewStatusList() {
    return getBaseDao().getAll(ReviewStatus.class);
  }
}
