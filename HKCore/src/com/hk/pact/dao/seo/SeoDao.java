package com.hk.pact.dao.seo;

import com.hk.domain.content.SeoData;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 8/24/12
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SeoDao extends BaseDao {
    public SeoData getSeoById(String id);
}
