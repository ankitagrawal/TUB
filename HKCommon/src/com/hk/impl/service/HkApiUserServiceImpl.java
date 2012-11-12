package com.hk.impl.service;

import java.security.InvalidParameterException;

import org.springframework.stereotype.Service;

import com.hk.domain.api.HkApiUser;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.HkApiUserService;

/**
 * @author vaibhav.adlakha
 */
@Service
public class HkApiUserServiceImpl implements HkApiUserService {

    private BaseDao baseDao;

    @Override
    public HkApiUser getApiUserByApiKey(String apiKey) {
        if (apiKey == null) {
            throw new InvalidParameterException("API_KEY_CANNOT_BE_BLANK");
        }
        return (HkApiUser) getBaseDao().findUniqueByNamedQueryAndNamedParam("getApiUserByApiKey", new String[] { "apiKey" }, new Object[] { apiKey });
    }

    @Override
    public HkApiUser getApiUserByName(String name) {
        if (name == null) {
            throw new InvalidParameterException("API_UNAME_CANNOT_BE_BLANK");
        }
        return (HkApiUser) getBaseDao().findUniqueByNamedQueryAndNamedParam("getApiUserByName", new String[] { "apiUserName" }, new Object[] { name });
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

}
