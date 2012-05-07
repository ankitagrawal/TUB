package com.hk.pact.dao;

import java.util.List;

import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;

public interface CheckDetailsDao extends BaseDao {

    public List<CheckDetails> getCheckListByAffiliate(Affiliate affiliate) ;

    public CheckDetails geCheckDetailsByCheckNo(String checkNo) ;

}
