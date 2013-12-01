package com.hk.pact.dao.clm;

import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA. User: Pradeep Date: May 29, 2012 Time: 4:00:16 PM To change this template use File |
 * Settings | File Templates.
 */
public interface KarmaProfileDao extends BaseDao {

    public KarmaProfile save(KarmaProfile karmaProfile);

    public KarmaProfile findByUser(User user);

}
