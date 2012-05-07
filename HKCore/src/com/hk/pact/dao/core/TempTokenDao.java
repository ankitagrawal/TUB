package com.hk.pact.dao.core;

import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

public interface TempTokenDao extends BaseDao {

    public TempToken save(TempToken tempToken);

    public void expire(TempToken tempToken);

    public TempToken createNew(User user, int expiryDays);

    public TempToken findByToken(String token);
}
