package com.hk.pact.service.audit;


/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface AuditService {

    /**
     * Identify what changed in the last operation and audit the information
     * @param action
     * @param entity
     * @param state
     * @param oldState
     * @param propertyNames
     * @param propertyTypes
     */
    public void audit(AuditAction action, Object entity, String entityId, Object[] state, Object[] oldState, String[] propertyNames, Object[] propertyTypes);
}
