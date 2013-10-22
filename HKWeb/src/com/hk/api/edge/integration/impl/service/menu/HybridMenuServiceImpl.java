package com.hk.api.edge.integration.impl.service.menu;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hk.api.edge.constants.ServiceEndPoints;
import com.hk.api.edge.constants.StoreConstants;
import com.hk.api.edge.http.HkHttpClient;
import com.hk.api.edge.http.URIBuilder;
import com.hk.api.edge.integration.pact.service.menu.HybridMenuService;
import com.hk.api.edge.integration.response.menu.MenuNodeResponseWrapperFromEdge;
import com.hk.edge.response.menu.CatalogMenuNode;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HybridMenuServiceImpl implements HybridMenuService {

    @Override
    @SuppressWarnings("unchecked")
    public List<CatalogMenuNode> getMenuFromEdge() {

        Long storeId = StoreConstants.DEFAULT_STORE_ID;

        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.STORE_MENU + storeId.toString());

        MenuNodeResponseWrapperFromEdge menuNodeResponseWrapper = (MenuNodeResponseWrapperFromEdge) HkHttpClient.executeGet(builder.getWebServiceUrl(), MenuNodeResponseWrapperFromEdge.class);
        if (menuNodeResponseWrapper != null && menuNodeResponseWrapper.getMenuResponse() != null) {
            return menuNodeResponseWrapper.getMenuResponse().getMenuNodes();
        }
        return Collections.EMPTY_LIST;
    }
}
