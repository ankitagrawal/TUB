package com.hk.impl.dao.core;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.util.BaseUtils;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.util.TokenUtils;

@Repository
public class TempTokenDaoImpl extends BaseDaoImpl implements TempTokenDao {

    @Transactional
    public TempToken save(TempToken tempToken) {

        if (tempToken != null) {
            if (tempToken.getCreateDate() == null)
                tempToken.setCreateDate(BaseUtils.getCurrentTimestamp());
        }
        return (TempToken) super.save(tempToken);
    }

    public void expire(TempToken tempToken) {
        tempToken.setExpired(true);
        save(tempToken);
    }

    public TempToken createNew(User user, int expiryDays) {
        TempToken tempToken = new TempToken();
        String token = TokenUtils.generateTempToken();
        tempToken.setToken(token);
        tempToken.setExpired(false);
        tempToken.setUser(user);
        tempToken.setExpiryDate(new DateTime().plusDays(expiryDays).toDate());
        return save(tempToken);
    }

    public TempToken findByToken(String token) {
        Criteria criteria = getSession().createCriteria(TempToken.class);
        criteria.add(Restrictions.eq("token", token));
        return (TempToken) criteria.uniqueResult();
    }
}
