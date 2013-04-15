package com.hk.impl.dao.queue;

import com.hk.domain.queue.ActionItem;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.queue.ActionItemDao;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 20:26
*/
public class ActionItemDaoImpl extends BaseDaoImpl implements ActionItemDao {

    @Override
    public ActionItem save(ActionItem actionItem) {
        return (ActionItem) super.save(actionItem);
    }

}
