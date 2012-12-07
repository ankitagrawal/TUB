package com.hk.cache;

import java.util.HashMap;
import java.util.Map;

import com.hk.domain.api.HkApiUser;

/**
 * @author vaibhav.adlakha
 */
public class HkApiUserCache {

    private static HkApiUserCache  _instance         = new HkApiUserCache();
    private HkApiUserCache         _transient;

    private Map<String, HkApiUser> apiKeyToUserCache = new HashMap<String, HkApiUser>();

    private HkApiUserCache() {
    }

    public static HkApiUserCache getInstance() {
        return _instance;
    }

    public void addHkApiUser(HkApiUser apiUser) {
        apiKeyToUserCache.put(apiUser.getApiKey(), apiUser);
    }

    public HkApiUser getHkApiUser(String apiKey) {
        return apiKeyToUserCache.get(apiKey);
    }

    public void freeze() {
        _instance = this;
    }

    public void reset() {
        _transient = new HkApiUserCache();
    }

    public HkApiUserCache getTransientCache() {
        return _transient;
    }
}
