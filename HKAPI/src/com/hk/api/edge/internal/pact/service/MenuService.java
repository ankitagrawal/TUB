package com.hk.api.edge.internal.pact.service;

import java.util.List;

import com.hk.api.edge.internal.response.menu.CatalogMenuNode;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface MenuService {

    public List<CatalogMenuNode> getMenu();
}
