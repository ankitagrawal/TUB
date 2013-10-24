package com.hk.web.action.nwb;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.api.edge.integration.pact.service.menu.HybridMenuService;
import com.hk.edge.response.menu.CatalogMenuNode;

/**
 * @author vaibhav.adlakha
 */
@Component
public class NwbMenuAction extends BaseAction {

    private List<CatalogMenuNode> menuNodes = new ArrayList<CatalogMenuNode>(0);

    @Autowired
    private HybridMenuService     hybridMenuService;

    @DefaultHandler
    public Resolution pre() {
        menuNodes = getHybridMenuService().getMenuFromEdge();
        return new ForwardResolution("/includes/_menuBeta.jsp");
    }

    public List<CatalogMenuNode> getMenuNodes() {
        return menuNodes;
    }

    public void setMenuNodes(List<CatalogMenuNode> menuNodes) {
        this.menuNodes = menuNodes;
    }

    public HybridMenuService getHybridMenuService() {
        return hybridMenuService;
    }

}
