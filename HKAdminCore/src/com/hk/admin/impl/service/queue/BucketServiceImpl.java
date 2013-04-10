package com.hk.admin.impl.service.queue;

import com.hk.admin.pact.service.queue.BucketService;
import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 16:04
*/
@Service
public class BucketServiceImpl implements BucketService {

    @Override
    public List<Param> getParamsForBucket(List<Bucket> bucketList) {
        List<Param> params = new ArrayList();
        for (Bucket bucket : bucketList) {
            params.addAll(bucket.getParams());
        }
        return params;
    }

    @Override
    public Map<String, Object> getParamMap(List<Bucket> bucketList) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        List<Param> params = getParamsForBucket(bucketList);
        for (Param param : params) {
            parameters.put(param.getParamName(), param.getParamValues());
        }
        return parameters;
    }

}
