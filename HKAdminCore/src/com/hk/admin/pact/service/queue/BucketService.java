package com.hk.admin.pact.service.queue;

import com.hk.domain.queue.Bucket;
import com.hk.domain.queue.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * User: Pratham
 * Date: 10/04/13  Time: 15:54
*/
@Service
public interface BucketService {

    public List<Param> getParamsForBucket(List<Bucket> bucketList);

    public Map<String, Object> getParamMap(List<Bucket> bucketList);

}
