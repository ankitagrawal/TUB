package com.hk.api.edge.integration.pact.service.menu;

import java.util.List;

import com.hk.edge.response.menu.CatalogMenuNode;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface HybridMenuService {

    public List<CatalogMenuNode> getMenuFromEdge();
}
