package com.hk.api.edge.internal.impl.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hk.api.edge.constants.ServiceEndPoints;
import com.hk.api.edge.constants.StoreConstants;
import com.hk.api.edge.http.HkHttpClient;
import com.hk.api.edge.http.URIBuilder;
import com.hk.api.edge.internal.pact.service.MenuService;
import com.hk.api.edge.internal.response.menu.MenuNodeResponseApiWrapper;
import com.hk.edge.response.menu.CatalogMenuNode;

/**
 * @author vaibhav.adlakha
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Override
    @SuppressWarnings("unchecked")
    public List<CatalogMenuNode> getMenu() {

        Long storeId = StoreConstants.DEFAULT_STORE_ID;

        URIBuilder builder = new URIBuilder().fromURI(ServiceEndPoints.STORE_MENU + storeId.toString());

        MenuNodeResponseApiWrapper menuNodeResponseWrapper = (MenuNodeResponseApiWrapper) HkHttpClient.executeGet(builder.getWebServiceUrl(), MenuNodeResponseApiWrapper.class);
        if (menuNodeResponseWrapper != null && menuNodeResponseWrapper.getMenuResponse() != null) {
            return menuNodeResponseWrapper.getMenuResponse().getMenuNodes();
        }
        return Collections.EMPTY_LIST;
    }
}
