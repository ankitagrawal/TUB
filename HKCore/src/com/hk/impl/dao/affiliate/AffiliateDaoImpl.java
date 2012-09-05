package com.hk.impl.dao.affiliate;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.coupon.EnumCouponType;
import com.hk.core.search.OrderBySqlFormula;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.affiliate.AffiliateStatus;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

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

	public Long getMaxCouponsLeft(Affiliate affiliate) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Coupon.class);
		detachedCriteria.add(Restrictions.eq("referrerUser", affiliate.getUser()));
		detachedCriteria.add(Restrictions.eq("couponType", EnumCouponType.AFFILIATE.asCouponType()));
		detachedCriteria.add(Restrictions.ge("createDate", DateUtils.getDateMinusDays(7)));
		List<Coupon> couponsList = (List<Coupon>) findByCriteria(detachedCriteria);
		return couponsList != null && !couponsList.isEmpty() ? couponsList.size() : 0L;
	}

	/*@Override
	public Page searchAffiliates(AffiliateStatus affiliateStatus, String name, String email, String websiteName, String code, Long affiliateMode, Long affiliateType, Role role, int perPage, int pageNo) {
//		DetachedCriteria affiliateTxnCriteria = DetachedCriteria.forClass(AffiliateTxn.class);
//
//		DetachedCriteria affiliateCriteria = affiliateTxnCriteria.createCriteria("affiliate", JoinFragment.LEFT_OUTER_JOIN);

		DetachedCriteria affiliateCriteria = DetachedCriteria.forClass(Affiliate.class);   //todo ps duplicate results
		if (affiliateStatus != null) {
			affiliateCriteria.add(Restrictions.eq("affiliateStatus", affiliateStatus));
		}
		if (websiteName != null && StringUtils.isNotBlank(websiteName)) {
			affiliateCriteria.add(Restrictions.eq("websiteName", websiteName));
		}
		if (code != null && StringUtils.isNotBlank(code)) {
			affiliateCriteria.add(Restrictions.eq("code", code));
		}
		if (affiliateMode != null) {
			affiliateCriteria.add(Restrictions.eq("affiliateMode", affiliateMode));
		}
		if (affiliateType != null) {
			affiliateCriteria.add(Restrictions.eq("affiliateType", affiliateType));
		}
		DetachedCriteria userCriteria = affiliateCriteria.createCriteria("user");
		if (name != null && StringUtils.isNotBlank(name)) {
			userCriteria.add(Restrictions.eq("name", name));
		}
		if (email != null && StringUtils.isNotBlank(email)) {
			userCriteria.add(Restrictions.eq("email", email));
		}

		DetachedCriteria affiliateTxnCriteria = affiliateCriteria.createCriteria("affiliateTxns");
		affiliateTxnCriteria.add(Restrictions.in("affiliateTxnType.id", Arrays.asList(new Long[]{10L, 20L})));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("panNo"));
		affiliateCriteria.setProjection(projectionList);

		affiliateCriteria.addOrder(OrderBySqlFormula.sqlFormula(" sum(amount) desc"));

		return list(affiliateCriteria, pageNo, perPage);
	}*/

	@Override
	public Page searchAffiliates(AffiliateStatus affiliateStatus, String name, String email, String websiteName, String code, Long affiliateMode, Long affiliateType, Role role, int perPage, int pageNo) {

		StringBuilder hql = new StringBuilder("select a from Affiliate a left join fetch a.affiliateTxns  atx where atx.affiliateTxnType.id in (10,20)");

		Map<String, Object> params = new HashMap<String, Object>();

		if (affiliateStatus != null) {
			hql.append(" and a.affiliateStatus.id = :affiliateStatusId ");
			params.put("affiliateStatusId",affiliateStatus.getId());
		}
		/*if (websiteName != null && StringUtils.isNotBlank(websiteName)) {
			hql.append(" and a.websiteName = :websiteName ");

		}*/
		/*if (code != null && StringUtils.isNotBlank(code)) {
			affiliateCriteria.add(Restrictions.eq("code", code));
		}
		if (affiliateMode != null) {
			affiliateCriteria.add(Restrictions.eq("affiliateMode", affiliateMode));
		}
		if (affiliateType != null) {
			affiliateCriteria.add(Restrictions.eq("affiliateType", affiliateType));
		}*/
		/*DetachedCriteria userCriteria = affiliateCriteria.createCriteria("user");
		if (name != null && StringUtils.isNotBlank(name)) {
			userCriteria.add(Restrictions.eq("name", name));
		}
		if (email != null && StringUtils.isNotBlank(email)) {
			userCriteria.add(Restrictions.eq("email", email));
		}*/

		/*DetachedCriteria affiliateTxnCriteria = affiliateCriteria.createCriteria("affiliateTxns");
		affiliateTxnCriteria.add(Restrictions.in("affiliateTxnType.id", Arrays.asList(new Long[]{10L, 20L})));
*/
		/*ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("panNo"));
		affiliateCriteria.setProjection(projectionList);

		affiliateCriteria.addOrder(OrderBySqlFormula.sqlFormula(" sum(amount) desc"));
*/
		hql.append(" group by a.id").append(" order by sum(amount) desc");
		//List results =   findByNamedParams(hql.toString(), new String[]{"affiliateStatusId"}, new Object[]{affiliateStatus.getId()});

		return list(hql.toString(), params, pageNo,perPage);
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

}
