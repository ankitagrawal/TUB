package web.action.core.menu;

import org.springframework.stereotype.Component;

import web.action.HomeAction;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.manager.SolrManager;


@Component
public class DataIndexRefreshAction extends BaseAction{
   SolrManager solrManager;
  public Resolution pre(){
    solrManager.refreshDataIndexes();
    return new ForwardResolution(HomeAction.class);      
  }
}
