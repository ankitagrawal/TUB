package com.hk.pact.dao.offer;

import com.akube.framework.dao.Page;
import com.hk.domain.offer.Offer;
import com.hk.pact.dao.BaseDao;

import java.util.List;

public interface OfferDao extends BaseDao {

    public Page listAllValid(int pageNo, int perPage);

    public Page listAllValidShowPromptly(int pageNo, int perPage);

    public List<Offer> listAllValidShowPromptly();

    public Page listAll(int pageNo, int perPage);

    public Offer findByIdentifier(String offerIdentifier);

}
