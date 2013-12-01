package com.hk.pact.dao.faq;

import com.akube.framework.dao.Page;
import com.hk.pact.dao.BaseDao;

public interface FaqDao extends BaseDao {

    public Page searchFaq(String keywords, String primaryCategory, String secondaryCategory, int pageNo, int perPage);

    public Page getFaqByCategory(String primaryCategory, String secondaryCategory, int pageNo, int perPage);
    
}