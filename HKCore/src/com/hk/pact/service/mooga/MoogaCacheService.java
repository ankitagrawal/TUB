package com.hk.pact.service.mooga;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 7/5/12
 * Time: 1:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MoogaCacheService {
    void pushReconmmededItems(String productId, List<String> recommendedItems);

    List<String> getRecommendedItems(String productId);

    boolean hasProduct(String productId);
}
