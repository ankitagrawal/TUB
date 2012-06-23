package com.hk.mooga.pact;

/**
 * Created by IntelliJ IDEA.
 * User: Marut
 * Date: 6/23/12
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RecommendationEngine  {
     void pushAddToCart(long userId, long dealId);
}
