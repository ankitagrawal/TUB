package com.hk.pact.dao.clm;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.clm.CategoryKarmaProfile;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/25/12
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CategoryKarmaProfileDao extends BaseDao {

    public CategoryKarmaProfile save(CategoryKarmaProfile categoryKarmaProfile);

    public CategoryKarmaProfile findByUserAndCategory(User user, Category category);

}
