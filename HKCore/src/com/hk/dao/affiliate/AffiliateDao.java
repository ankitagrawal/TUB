package com.hk.dao.affiliate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;

@Repository
public class AffiliateDao extends BaseDaoImpl {

    public Affiliate getAffilateByUser(User user) {
        String queryString = "from Affiliate a where a.user=:user ";
        return (Affiliate) findUniqueByNamedParams(queryString, new String[] { "user" }, new Object[] { user });

    }

    

    public Affiliate getAffiliateByCode(String code) {
        String queryString = "from Affiliate a where a.code=:code ";
        return (Affiliate) findUniqueByNamedParams(queryString, new String[] { "code" }, new Object[] { code });
    }

    public Affiliate getAffiliateByUserId(Long userId) {
        String queryString = "from Affiliate a where a.user.id = :userId";
        return (Affiliate) findUniqueByNamedParams(queryString, new String[] { "userId" }, new Object[] { userId });
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

        List<Long> affiliateIDList = (List<Long>) find("select a.id from Affiliate a");
        
        DetachedCriteria criteria = DetachedCriteria.forClass(Affiliate.class);
        criteria.add(Restrictions.in("id", affiliateIDList));
        DetachedCriteria userCriteria = criteria.createCriteria("user");
        DetachedCriteria roleCriteria = userCriteria.createCriteria("roles");
        roleCriteria.add(Restrictions.eq("name", RoleConstants.HK_AFFILIATE));
        
        return list(criteria, page, perPage);
    }

}
