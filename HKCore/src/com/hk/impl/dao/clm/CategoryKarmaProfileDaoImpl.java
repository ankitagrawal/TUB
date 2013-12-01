package com.hk.impl.dao.clm;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.catalog.category.Category;
import com.hk.domain.clm.CategoryKarmaProfile;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.clm.CategoryKarmaProfileDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/25/12
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository
@SuppressWarnings("unchecked")
public class CategoryKarmaProfileDaoImpl extends BaseDaoImpl implements CategoryKarmaProfileDao {

    @Transactional
    public CategoryKarmaProfile save(CategoryKarmaProfile categoryKarmaProfile) {
        return (CategoryKarmaProfile) super.save(categoryKarmaProfile);
    }

    public CategoryKarmaProfile findByUserAndCategory(User user, Category category) {

        return (CategoryKarmaProfile) findUniqueByNamedParams(" from CategoryKarmaProfile ckp where ckp.user.id = :userId and ckp.category.name = :categoryName", new String[]{"userId","categoryName"}, new Object[]{user.getId(), category.getName()});
        //return  (KarmaProfile) getSession().createQuery("from KarmaProfile kp where kp.user = :user ").setEntity("user", user).uniqueResult();
    }

}
