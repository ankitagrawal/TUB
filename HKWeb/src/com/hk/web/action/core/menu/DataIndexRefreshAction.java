package com.hk.web.action.core.menu;

import com.hk.exception.SearchException;
import com.hk.pact.service.search.ProductSearchService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.web.action.HomeAction;

@Component
public class DataIndexRefreshAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(DataIndexRefreshAction.class);

    @Autowired
    ProductSearchService productSearchService;

    public Resolution pre() {
        try{
            productSearchService.refreshDataIndexes();
        }catch (SearchException ex){
            logger.error("Unable to refresh solr indexes");
        }
        return new ForwardResolution(HomeAction.class);
    }
}
