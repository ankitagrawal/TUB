package com.hk.web.action.core.affiliate;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.affiliate.EnumAffiliateStatus;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.affiliate.AffiliateStatus;
import com.hk.domain.offer.Offer;
import com.hk.domain.user.Address;
import com.hk.domain.user.Role;
import com.hk.impl.dao.CheckDetailsDaoImpl;
import com.hk.impl.dao.affiliate.AffiliateCategoryDaoImpl;
import com.hk.manager.AffiliateManager;
import com.hk.pact.dao.CheckDetailsDao;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.RoleService;
import com.hk.report.dto.payment.AffiliatePaymentDto;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Secure(hasAnyPermissions = {PermissionConstants.MANAGE_AFFILIATES}, authActionBean = AdminPermissionAction.class)
@Component
public class AffiliatePaymentAction extends BasePaginatedAction {
	@Autowired
	AffiliateDao affiliateDao;
	@Autowired
	AffiliateManager affiliateManager;
	@Autowired
	CheckDetailsDao checkDetailsDao;
	@Autowired
	AddressDao addressDao;
	@Autowired
	AffiliateCategoryDaoImpl affiliateCategoryCommissionDao;

	List<AffiliatePaymentDto> affiliatePaymentDtoList = new ArrayList<AffiliatePaymentDto>();
	Affiliate affiliate;
	List<CheckDetails> checkDetailsList;
	List<AffiliateCategoryCommission> affiliateCategoryCommissionList;
	Date startDate;
	Date endDate;
	Offer offer;

	Double amount;
	@ValidateNestedProperties({
			@Validate(field = "checkNo", required = true, on = "payToAffiliate"),
			@Validate(field = "issueDate", required = true, on = "payToAffiliate"),
			@Validate(field = "bankName", required = true, on = "payToAffiliate"),
      @Validate(field = "tds", required = true, on="payToAffiliate")})
	CheckDetails checkDetails;
	@Validate(required = true, on = "payToAffiliate")
	Double amountToPay;
	Address checkDeliveryAddress;
	String name;
	String email;
	String websiteName;
	String code;
	Long affiliateMode;
	Long affiliateType;
	Role role;
  Long affiliateStatus;

	Page affiliatePage;
	List<Affiliate> affiliates = new ArrayList<Affiliate>();

	@Autowired
	private RoleService roleService;

	@DefaultHandler
	public Resolution pre() {
		List<Long> affiliateStatusIds = Arrays.asList(EnumAffiliateStatus.Verified.getId());
		affiliatePage = affiliateDao.searchAffiliates(affiliateStatusIds, name, email, websiteName, code, affiliateMode, affiliateType, EnumRole.HK_AFFILIATE.toRole(), getPerPage(), getPageNo());
		if (affiliatePage != null) {
			affiliates = affiliatePage.getList();
		}

		for (Affiliate affiliate : affiliates) {
			AffiliatePaymentDto affiliatePaymentDto = new AffiliatePaymentDto();
			affiliatePaymentDto.setAffiliate(affiliate);
			affiliatePaymentDto.setAmount(getAffiliateManager().getAmountInAccount(affiliate, null, null));
			affiliatePaymentDto.setPayableAmount(getAffiliateManager().getPayableAmount(affiliate));
			affiliatePaymentDtoList.add(affiliatePaymentDto);
		}
		return new ForwardResolution("/pages/affiliate/payToAffiliates.jsp");
	}

	public Resolution search() {
		List<Long> affiliateStatusIds = new ArrayList<Long>();
		if(affiliateStatus != null){
			affiliateStatusIds.add(affiliateStatus);
		}
		affiliatePage = affiliateDao.searchAffiliates(affiliateStatusIds, name, email, websiteName, code, affiliateMode, affiliateType, role, getPerPage(),getPageNo());
		if (affiliatePage != null) {
			affiliates = affiliatePage.getList();
		}
		for (Affiliate affiliate : affiliates) {
			AffiliatePaymentDto affiliatePaymentDto = new AffiliatePaymentDto();
			affiliatePaymentDto.setAffiliate(affiliate);
			affiliatePaymentDto.setAmount(getAffiliateManager().getAmountInAccount(affiliate, null, null));
			affiliatePaymentDto.setPayableAmount(getAffiliateManager().getPayableAmount(affiliate));
			affiliatePaymentDtoList.add(affiliatePaymentDto);
		}
		return new ForwardResolution("/pages/affiliate/payToAffiliates.jsp");
	}

	public Resolution showAffiliatePlan() {
		affiliateCategoryCommissionList = getAffiliateDao().getAffiliatePlan(affiliate);
		return new ForwardResolution("/pages/affiliate/affiliatePlan.jsp");
	}

	public Resolution savePlan() {
		if(offer == null){
			return new RedirectResolution(AffiliatePaymentAction.class,"showAffiliatePlan").addParameter("affiliate", affiliate);
		}
		affiliate.setOffer(offer);
		affiliate = (Affiliate) affiliateDao.save(affiliate);
    if(affiliateCategoryCommissionList!=null){
		for (AffiliateCategoryCommission categoryCommission : affiliateCategoryCommissionList) {
			getAffiliateCategoryCommissionDao().save(categoryCommission);
		  }
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
		amount = getAffiliateManager().getAmountInAccount(affiliate, startDate, endDate);
		checkDetailsList = getCheckDetailsDao().getCheckListByAffiliate(affiliate);
		return new ForwardResolution("/pages/affiliate/affiliateAccount.jsp");
	}

	public Resolution paymentDetails() {
		if (affiliate.getMainAddressId() != null) {
			checkDeliveryAddress = getAddressDao().get(Address.class, affiliate.getMainAddressId());
		} else {
			checkDeliveryAddress = null;
		}
		amountToPay = affiliateManager.getPayableAmount(affiliate);
			return new ForwardResolution("/pages/affiliate/paymentToAffiliateDetails.jsp");
	}

	public Resolution payToAffiliate() {
		if (getCheckDetailsDao().geCheckDetailsByCheckNo(checkDetails.getCheckNo()) != null) {
			addValidationError("e1", new SimpleError("The check with this check no. has already been sent to someone."));
			return new ForwardResolution("/pages/affiliate/paymentToAffiliateDetails.jsp");
		}
		affiliateManager.paidToAffiiliate(affiliate, amountToPay, checkDetails);
		addRedirectAlertMessage(new SimpleMessage("Entry made."));
		return new RedirectResolution(AffiliatePaymentAction.class, "pre");
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
		HashSet<String> params = new HashSet<String>();
		params.add("affiliateType");
		params.add("affiliateMode");
		params.add("affiliateStatus");
		params.add("websiteName");
		params.add("email");
		params.add("name");
		return params;
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

	public CheckDetailsDao getCheckDetailsDao() {
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Page getAffiliatePage() {
		return affiliatePage;
	}

	public void setAffiliatePage(Page affiliatePage) {
		this.affiliatePage = affiliatePage;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getAffiliateMode() {
		return affiliateMode;
	}

	public void setAffiliateMode(Long affiliateMode) {
		this.affiliateMode = affiliateMode;
	}

	public Long getAffiliateType() {
		return affiliateType;
	}

	public void setAffiliateType(Long affiliateType) {
		this.affiliateType = affiliateType;
	}

	public Role getRole() {
		return role;
  }

	public void setRole(Role role) {
		this.role = role;
	}

  public Long getAffiliateStatus() {
    return affiliateStatus;
  }

  public void setAffiliateStatus(Long affiliateStatus) {
    this.affiliateStatus = affiliateStatus;
  }

  public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
}
