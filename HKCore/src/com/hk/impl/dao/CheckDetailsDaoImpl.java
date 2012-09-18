package com.hk.impl.dao;

import java.util.List;

import com.hk.pact.dao.CheckDetailsDao;
import org.springframework.stereotype.Repository;

import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;

@SuppressWarnings("unchecked")
@Repository
public class CheckDetailsDaoImpl extends BaseDaoImpl implements CheckDetailsDao {

    public List<CheckDetails> getCheckListByAffiliate(Affiliate affiliate) {
        return getSession().createQuery("from CheckDetails c where c.affiliate=:affiliate").setParameter("affiliate", affiliate).list();
    }

    public CheckDetails geCheckDetailsByCheckNo(String checkNo) {
        return (CheckDetails) getSession().createQuery("from CheckDetails c where c.checkNo =:checkNo").setParameter("checkNo", checkNo).uniqueResult();
    }

}
