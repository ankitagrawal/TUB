package com.hk.pact.dao.offer;

import com.akube.framework.dao.Page;
import com.hk.domain.offer.Offer;
import com.hk.pact.dao.BaseDao;

public interface OfferDao extends BaseDao {

    public Page listAllValid(int pageNo, int perPage);

    public Page listAll(int pageNo, int perPage);

    public Offer findByIdentifier(String offerIdentifier);

}
