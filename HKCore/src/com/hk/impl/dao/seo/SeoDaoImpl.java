package com.hk.impl.dao.seo;

import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.content.SeoData;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.seo.SeoDao;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 8/24/12
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class SeoDaoImpl extends BaseDaoImpl implements SeoDao {

    public SeoData getSeoById(String id) {
        return get(SeoData.class, id);
    }
}
