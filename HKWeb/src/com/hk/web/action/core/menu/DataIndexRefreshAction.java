package com.hk.web.action.core.menu;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.manager.SolrManager;
import com.hk.web.action.HomeAction;

@Component
public class DataIndexRefreshAction extends BaseAction {
    @Autowired
    SolrManager solrManager;

    public Resolution pre() {
        solrManager.refreshDataIndexes();
        return new ForwardResolution(HomeAction.class);
    }
}
