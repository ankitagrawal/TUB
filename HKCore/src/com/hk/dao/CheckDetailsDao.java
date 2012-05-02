package com.hk.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hk.dao.impl.BaseDaoImpl;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;

@SuppressWarnings("unchecked")
@Repository
public class CheckDetailsDao extends BaseDaoImpl {

    public List<CheckDetails> getCheckListByAffiliate(Affiliate affiliate) {
        return getSession().createQuery("from CheckDetails c where c.affiliate=:affiliate").setParameter("affiliate", affiliate).list();
    }

    public CheckDetails geCheckDetailsByCheckNo(String checkNo) {
        return (CheckDetails) getSession().createQuery("from CheckDetails c where c.checkNo =:checkNo").setParameter("checkNo", checkNo).uniqueResult();
    }

}
