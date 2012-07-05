package com.hk.impl.service.mooga;

import com.hk.pact.service.mooga.MoogaCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 7/5/12
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MoogaCacheServiceImpl implements MoogaCacheService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private final String ITEM_LIST = "RecoItems";
    /**
     * pushes recommended items to a list specific to productId
     * @param productId
     * @param recommendedItems
     */
    public void pushReconmmededItems(String productId, List<String> recommendedItems){
            for (String item : recommendedItems){
                redisTemplate.opsForList().leftPush(getKey(productId), item);
            }
    }

    public List<String> getRecommendedItems(String productId){
        return redisTemplate.opsForList().range(getKey(productId), 0, -1);
    }

    public boolean hasProduct(String productId){
        return redisTemplate.hasKey(getKey(productId)).booleanValue();
    }

    protected String getKey(String productId){
        return String.format("%s:%s", ITEM_LIST, productId);
    }
}
