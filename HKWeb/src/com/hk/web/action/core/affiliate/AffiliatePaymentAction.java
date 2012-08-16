package com.hk.web.action.core.affiliate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.impl.dao.CheckDetailsDaoImpl;
import com.hk.impl.dao.affiliate.AffiliateCategoryDaoImpl;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.RoleService;
import com.hk.report.dto.payment.AffiliatePaymentDto;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.MANAGE_AFFILIATES}, authActionBean = AdminPermissionAction.class)
@Component
public class AffiliatePaymentAction extends BasePaginatedAction {
    @Autowired
    AffiliateDao affiliateDao;
    @Autowired
    AffiliateManager affiliateManager;
    @Autowired
    CheckDetailsDaoImpl checkDetailsDao;
    @Autowired
    AddressDao addressDao;
    @Autowired
    AffiliateCategoryDaoImpl affiliateCategoryCommissionDao;

    List<AffiliatePaymentDto> affiliatePaymentDtoList = new ArrayList<AffiliatePaymentDto>();
    Affiliate affiliate;
    List<CheckDetails> checkDetailsList;
    List<AffiliateCategoryCommission> affiliateCategoryCommissionList;
    Page affiliatePage;

    Double amount;
    @ValidateNestedProperties({
            @Validate(field = "checkNo", required = true, on = "payToAffiliate"),
            @Validate(field = "issueDate", required = true, on = "payToAffiliate"),
            @Validate(field = "bankName", required = true, on = "payToAffiliate")})
    CheckDetails checkDetails;
    @Validate(required = true, on = "payToAffiliate")
    Double amountToPay;
    Address checkDeliveryAddress;
    String name;
    String email;

    @Autowired
    private RoleService roleService;

    @DefaultHandler
    public Resolution pre() {
        affiliatePage = getUserService().findByRole(name, email, getRoleService().getRoleByName(RoleConstants.HK_AFFILIATE), getPageNo(), getPerPage());
        List<User> affiliatesUsers = affiliatePage.getList();
        List<Affiliate> affiliates = new ArrayList<Affiliate>();
        for (User user : affiliatesUsers) {
            affiliates.add(affiliateDao.getAffilateByUser(user));
        }
        for (Affiliate affiliate : affiliates) {
                AffiliatePaymentDto affiliatePaymentDto = new AffiliatePaymentDto();
                affiliatePaymentDto.setAffiliate(affiliate);
                affiliatePaymentDto.setAmount(getAffiliateManager().getAmountInAccount(affiliate));
                affiliatePaymentDtoList.add(affiliatePaymentDto);
        }
        return new ForwardResolution("/pages/affiliate/payToAffiliates.jsp");
    }

    public Resolution showAffiliatePlan() {
        affiliateCategoryCommissionList = getAffiliateDao().getAffiliatePlan(affiliate);
        return new ForwardResolution("/pages/affiliate/affiliatePlan.jsp");
    }

    public Resolution savePlan() {
        for (AffiliateCategoryCommission categoryCommission : affiliateCategoryCommissionList) {
            getAffiliateCategoryCommissionDao().save(categoryCommission);
        }
        return new ForwardResolution("/pages/affiliate/affiliatePlan.jsp");
    }

    public Resolution save() {
        for (AffiliatePaymentDto affiliatePaymentDto : affiliatePaymentDtoList) {
            getAffiliateDao().save(affiliatePaymentDto.getAffiliate());
        }
        return new ForwardResolution("/pages/affiliate/payToAffiliates.jsp");
    }

    public Resolution showAffiliateDetails() {
        amount = getAffiliateManager().getAmountInAccount(affiliate);
        checkDetailsList = getCheckDetailsDao().getCheckListByAffiliate(affiliate);
        return new ForwardResolution("/pages/affiliate/affiliateAccount.jsp");
    }

    public Resolution paymentDetails() {
        if (affiliate.getMainAddressId() != null) {
            checkDeliveryAddress = getAddressDao().get(Address.class, affiliate.getMainAddressId());
        } else {
            checkDeliveryAddress = null;
        }
        return new ForwardResolution("/pages/affiliate/paymentToAffiliateDetails.jsp");
    }

    public Resolution payToAffiliate() {
        if (getCheckDetailsDao().geCheckDetailsByCheckNo(checkDetails.getCheckNo()) != null) {
            addValidationError("e1", new SimpleError("The check with this check no. has already been sent to someone."));
            return new ForwardResolution("/pages/affiliate/paymentToAffiliateDetails.jsp");
        }
        affiliateManager.paidToAffiiliate(affiliate, amountToPay, checkDetails);
        addRedirectAlertMessage(new SimpleMessage("Entry made."));
        return new RedirectResolution("/pages/admin/adminHome.jsp");
    }

    @SuppressWarnings("unchecked")
    public List<AffiliatePaymentDto> getAffiliatePaymentDtoList() {
        Collections.sort(affiliatePaymentDtoList, new Comparator() {
            public int compare(Object o1, Object o2) {
                AffiliatePaymentDto affiliatePaymentObject1 = (AffiliatePaymentDto) o1;
                AffiliatePaymentDto affiliatePaymentObject2 = (AffiliatePaymentDto) o2;
                return affiliatePaymentObject2.getAmount().compareTo(affiliatePaymentObject1.getAmount());
            }
        });
        return affiliatePaymentDtoList;
    }

    public void setAffiliatePaymentDtoList(List<AffiliatePaymentDto> affiliatePaymentDtoList) {
        this.affiliatePaymentDtoList = affiliatePaymentDtoList;
    }

    public Affiliate getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
    }

    public List<CheckDetails> getCheckDetailsList() {
        return checkDetailsList;
    }

    public void setCheckDetailsList(List<CheckDetails> checkDetailsList) {
        this.checkDetailsList = checkDetailsList;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public CheckDetails getCheckDetails() {
        return checkDetails;
    }

    public void setCheckDetails(CheckDetails checkDetails) {
        this.checkDetails = checkDetails;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public Address getCheckDeliveryAddress() {
        return checkDeliveryAddress;
    }

    public void setCheckDeliveryAddress(Address checkDeliveryAddress) {
        this.checkDeliveryAddress = checkDeliveryAddress;
    }

    public List<AffiliateCategoryCommission> getAffiliateCategoryCommissionList() {
        return affiliateCategoryCommissionList;
    }

    public void setAffiliateCategoryCommissionList(List<AffiliateCategoryCommission> affiliateCategoryCommissionList) {
        this.affiliateCategoryCommissionList = affiliateCategoryCommissionList;
    }

    public Set<String> getParamSet() {
        return null;
    }

    public int getPerPageDefault() {
        return 20;
    }

    public int getPageCount() {
        return affiliatePage == null ? 0 : affiliatePage.getTotalPages();
    }

    public int getResultCount() {
        return affiliatePage == null ? 0 : affiliatePage.getTotalResults();
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public AffiliateDao getAffiliateDao() {
        return affiliateDao;
    }

    public void setAffiliateDao(AffiliateDao affiliateDao) {
        this.affiliateDao = affiliateDao;
    }

    public AffiliateManager getAffiliateManager() {
        return affiliateManager;
    }

    public void setAffiliateManager(AffiliateManager affiliateManager) {
        this.affiliateManager = affiliateManager;
    }

    public CheckDetailsDaoImpl getCheckDetailsDao() {
        return checkDetailsDao;
    }

    public void setCheckDetailsDao(CheckDetailsDaoImpl checkDetailsDao) {
        this.checkDetailsDao = checkDetailsDao;
    }

    public AddressDao getAddressDao() {
        return addressDao;
    }

    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    public AffiliateCategoryDaoImpl getAffiliateCategoryCommissionDao() {
        return affiliateCategoryCommissionDao;
    }

    public void setAffiliateCategoryCommissionDao(AffiliateCategoryDaoImpl affiliateCategoryCommissionDao) {
        this.affiliateCategoryCommissionDao = affiliateCategoryCommissionDao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
