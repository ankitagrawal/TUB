package com.hk.impl.service.audit;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.audit.ChangeGroup;
import com.hk.exception.HealthkartDefaultWebException;
import com.hk.listener.AnnotationDesc;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.AnnotationsAPI;
import com.hk.pact.service.Trackable;
import com.hk.pact.service.audit.AuditAPI;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
@Service
public class AuditAPIImpl implements AuditAPI {

    private static Logger logger = LoggerFactory.getLogger(AuditAPIImpl.class);
    @Autowired
    private AnnotationsAPI annotationsAPI;

    @Autowired
    private BaseDao baseDao;
    
    @Override
    public Set<String> findTrackableProperties(Object entity) {
      Set<String> trackableProperties = new HashSet<String>();
      Set<AnnotationDesc> trackableList = getAnnotationsAPI().findAnnotationsOnClass(entity.getClass(), Trackable.class);
      for (AnnotationDesc annotationDesc : trackableList) {
        trackableProperties.add(annotationDesc.getField().getName());
      }
      return trackableProperties;
    }


    @Override
    @Transactional
    public void saveChangeGroup(ChangeGroup changeGroup) {
      try {
        getBaseDao().save(changeGroup);
      } catch (Exception ex) {
        logger.error("Could not save change group" , ex);
        throw new HealthkartDefaultWebException("Could not save change group");
      }
    }


    public AnnotationsAPI getAnnotationsAPI() {
      return annotationsAPI;
    }


    public BaseDao getBaseDao() {
        return baseDao;
    }

    
}
