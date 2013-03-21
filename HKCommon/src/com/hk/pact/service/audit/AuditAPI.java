package com.hk.pact.service.audit;

import java.util.Set;

import com.hk.domain.audit.ChangeGroup;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface AuditAPI {

    /**
     * Find all properties on the entity that have been marked as trackable and need to be audited
     * @param entity
     * @return
     */
    public Set<String> findTrackableProperties(Object entity);

    /**
     * save auditing inofrmation
     * @param changeGroup
     *
     */
    public void saveChangeGroup(ChangeGroup changeGroup);
}
