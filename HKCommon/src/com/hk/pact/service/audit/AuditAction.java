package com.hk.pact.service.audit;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public enum AuditAction {

    INSERT("insert"), UPDATE("update"), DELETE("delete");

    private AuditAction(String actionType) {
      this.actionType = actionType;
    }

    private String actionType;

    public String getActionType() {
      return actionType;
    }
}
