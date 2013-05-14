package com.hk.impl.dao.user;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.dto.user.B2BUserFilterDto;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.user.B2BUserDao;

/**
 * Author: pankaj pandey
 */
@SuppressWarnings("unchecked")
@Repository
public class B2BUserDaoImpl extends BaseDaoImpl implements B2BUserDao {

    
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(B2BUserDaoImpl.class);

	@Override
	public Page search(B2BUserFilterDto userFilterDto, int pageNo, int perPage) {
		userFilterDto.setCreateDateFrom(DateUtils.getStartOfDay(userFilterDto.getCreateDateFrom()));
        userFilterDto.setCreateDateTo(DateUtils.getEndOfDay(userFilterDto.getCreateDateTo()));
        userFilterDto.setLastLoginDateFrom(DateUtils.getStartOfDay(userFilterDto.getLastLoginDateFrom()));
        userFilterDto.setLastLoginDateTo(DateUtils.getEndOfDay(userFilterDto.getLastLoginDateTo()));

        DetachedCriteria criteria = DetachedCriteria.forClass(User.class,"u")
        		.createAlias("roles", "r").add(Restrictions.eq("r.name","B2B_USER"));
        DetachedCriteria b2b_criteria = DetachedCriteria.forClass(B2bUserDetails.class)
        		.setProjection(Projections.property("user"));
        DetachedCriteria ud_criteria = DetachedCriteria.forClass(UserDetail.class)
        		.setProjection(Projections.property("user"));
        
        Conjunction con_criteria=Restrictions.conjunction();
        Disjunction dis_criteria=Restrictions.disjunction();
        boolean is_empty_conjuction=true;
        boolean is_b2b_details=false;
        
        if (userFilterDto.getId() != null) {
            con_criteria.add(Restrictions.eq("id", userFilterDto.getId()));
            is_empty_conjuction=false;
        }
        if (StringUtils.isNotBlank(userFilterDto.getLogin())) {
            con_criteria.add(Restrictions.eq("login", userFilterDto.getLogin()));
            is_empty_conjuction=false;
        }
        if (StringUtils.isNotBlank(userFilterDto.getEmail())) {
        	con_criteria.add(Restrictions.like("email", "%" + userFilterDto.getEmail() + "%"));
        	is_empty_conjuction=false;
        }
        if (StringUtils.isNotBlank(userFilterDto.getName())) {
        	con_criteria.add(Restrictions.like("name", "%" + userFilterDto.getName() + "%"));
        	is_empty_conjuction=false;
        }
        if (StringUtils.isNotBlank(userFilterDto.getUserHash())) {
        	con_criteria.add(Restrictions.eq("userHash", userFilterDto.getUserHash()));
        	is_empty_conjuction=false;
        }
        if (userFilterDto.getCreateDateFrom() != null) {
        	con_criteria.add(Restrictions.ge("createDate", userFilterDto.getCreateDateFrom()));
        	is_empty_conjuction=false;
        }
        if (userFilterDto.getCreateDateTo() != null) {
        	con_criteria.add(Restrictions.le("createDate", userFilterDto.getCreateDateTo()));
        	is_empty_conjuction=false;
        }
        if (userFilterDto.getLastLoginDateFrom() != null) {
        	con_criteria.add(Restrictions.ge("lastLoginDate", userFilterDto.getLastLoginDateFrom()));
        	is_empty_conjuction=false;
        }
        if (userFilterDto.getLastLoginDateTo() != null) {
        	con_criteria.add(Restrictions.le("lastLoginDate", userFilterDto.getLastLoginDateTo()));
        	is_empty_conjuction=false;
        }
        if (StringUtils.isNotBlank(userFilterDto.getTin())) {
        	b2b_criteria.add(Restrictions.eq("tin", userFilterDto.getTin()));
        	is_b2b_details=true;
        }
        if (StringUtils.isNotBlank(userFilterDto.getDlNumber())) {
        	b2b_criteria.add(Restrictions.eq("dlNumber", userFilterDto.getDlNumber()));
        	is_b2b_details=true;
        }
        if (StringUtils.isNotBlank(userFilterDto.getPhone())) {
        	ud_criteria.add(Restrictions.eq("phone", Long.parseLong(userFilterDto.getPhone())));
        	dis_criteria.add(Subqueries.propertyIn("u.id", ud_criteria));
        }
        
        if(!is_empty_conjuction)
        {
        	 dis_criteria.add(con_criteria);
        }
       if(is_b2b_details)
       {
        criteria.add(Restrictions.or(dis_criteria, Subqueries.propertyIn("u.id", b2b_criteria)));
       }
       else
       {
    	   criteria.add(dis_criteria);
       }
        return list(criteria, pageNo, perPage);
	}

    
}
