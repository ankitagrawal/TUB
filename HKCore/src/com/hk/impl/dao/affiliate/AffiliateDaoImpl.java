package com.hk.impl.dao.affiliate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.constants.affiliate.EnumAffiliateTxnType;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.service.RoleService;

@Repository
public class AffiliateDaoImpl extends BaseDaoImpl implements AffiliateDao {

	@Autowired
	private RoleService roleService;

	public Affiliate getAffilateByUser(User user) {
		String queryString = "from Affiliate a where a.user=:user ";
		return (Affiliate) findUniqueByNamedParams(queryString, new String[]{"user"}, new Object[]{user});

	}

	public Affiliate getAffiliateByCode(String code) {
		String queryString = "from Affiliate a where a.code=:code ";
		return (Affiliate) findUniqueByNamedParams(queryString, new String[]{"code"}, new Object[]{code});
	}

	public Affiliate getAffiliateByUserId(Long userId) {
		String queryString = "from Affiliate a where a.user.id = :userId";
		return (Affiliate) findUniqueByNamedParams(queryString, new String[]{"userId"}, new Object[]{userId});
	}

	public Affiliate getAffiliateById(Long affiliateId) {
		return get(Affiliate.class, affiliateId);
	}

	@SuppressWarnings("unchecked")
	public List<AffiliateCategoryCommission> getAffiliatePlan(Affiliate affiliate) {
		String queryString = "from AffiliateCategoryCommission acc where acc.affiliate = :affiliate";
		return findByNamedParams(queryString, new String[]{"affiliate"}, new Object[]{affiliate});
	}

	@SuppressWarnings("unchecked")
	public Page getAffiliateVerifiedUsers(int page, int perPage) {
		List<Role> roleList = new ArrayList<Role>();
		Role verifiedAffiliate = get(Role.class, EnumRole.HK_AFFILIATE.getRoleName());
		roleList.add(verifiedAffiliate);

		List<Long> affiliateIDList = (List<Long>) findByQuery("select a.id from Affiliate a");

		DetachedCriteria criteria = DetachedCriteria.forClass(Affiliate.class);
		criteria.add(Restrictions.in("id", affiliateIDList));
		DetachedCriteria userCriteria = criteria.createCriteria("user");
		DetachedCriteria roleCriteria = userCriteria.createCriteria("roles");
		roleCriteria.add(Restrictions.eq("name", RoleConstants.HK_AFFILIATE));

		return list(criteria, page, perPage);
	}

	@SuppressWarnings("unchecked")
    public Long getMaxCouponsLeft(Affiliate affiliate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Coupon.class);
		detachedCriteria.add(Restrictions.eq("referrerUser", affiliate.getUser()));
		detachedCriteria.add(Restrictions.eq("couponType", EnumCouponType.AFFILIATE.asCouponType()));
		detachedCriteria.add(Restrictions.ge("createDate", DateUtils.getDateMinusDays(7)));
		List<Coupon> couponsList = (List<Coupon>) findByCriteria(detachedCriteria);
		return couponsList != null && !couponsList.isEmpty() ? couponsList.size() : 0L;
	}

	@Override
	public Page searchAffiliates(List<Long> affiliateStatusIds, String name, String email, String websiteName, String code, Long affiliateMode, Long affiliateType, Role role, int perPage, int pageNo) {
		List<Long> affiliateTxnTypeIds = Arrays.asList(EnumAffiliateTxnType.ADD.getId(), EnumAffiliateTxnType.PENDING.getId(), EnumAffiliateTxnType.PAYMENT_DUE.getId(), EnumAffiliateTxnType.PAID.getId());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("affiliateTxnTypeIds", affiliateTxnTypeIds);

		StringBuilder hql = new StringBuilder("select a from Affiliate a left outer join fetch a.affiliateTxns  atx left join fetch a.user u where (atx.affiliateTxnType.id in (:affiliateTxnTypeIds) or atx.affiliateTxnType.id is null)");

		if (affiliateStatusIds != null && !affiliateStatusIds.isEmpty()) {
			hql.append(" and a.affiliateStatus.id in (:affiliateStatusIds) ");
			params.put("affiliateStatusIds", affiliateStatusIds);
		}
		if (websiteName != null && StringUtils.isNotBlank(websiteName)) {
			hql.append(" and a.websiteName like :websiteName ");
			params.put("websiteName", "%" + websiteName + "%");
		}
		if (code != null && StringUtils.isNotBlank(code)) {
			hql.append(" and a.code = :code ");
			params.put("code", code);
		}
		if (affiliateMode != null) {
			hql.append(" and a.affiliateMode = :affiliateMode ");
			params.put("affiliateMode", affiliateMode);
		}

		if (affiliateType != null) {
			hql.append(" and a.affiliateType = :affiliateType ");
			params.put("affiliateType", affiliateType);
		}

		if (name != null && StringUtils.isNotBlank(name)) {
			hql.append(" and u.name like :name ");
			params.put("name", "%" + name + "%");
		}

		if (email != null && StringUtils.isNotBlank(email)) {
			hql.append(" and u.email like :email ");
			params.put("email", "%" + email + "%");
		}

		hql.append(" group by a.id").append(" order by sum(amount) desc");
		return list(hql.toString(), params, pageNo, perPage);
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

}
