package com.hk.impl.service.audit;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hk.domain.audit.ChangeGroup;
import com.hk.domain.audit.ChangeItem;
import com.hk.domain.user.User;
import com.hk.exception.HealthkartDefaultWebException;
import com.hk.pact.service.UserService;
import com.hk.pact.service.audit.AuditAPI;
import com.hk.pact.service.audit.AuditAction;
import com.hk.pact.service.audit.AuditService;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class AuditServiceImpl  implements AuditService{

    
    private static Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);
    
    @Autowired
    private AuditAPI auditAPI;
    @Autowired
    private UserService userService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void audit(AuditAction action, Object entity, Long entityId, Object[] state, Object[] oldState, String[] propertyNames, Object[] propertyTypes) {
      Set<String> trackableProperties = getAuditAPI().findTrackableProperties(entity);

      if (trackableProperties.size() > 0) {
        if (!(state.length == oldState.length && oldState.length == propertyNames.length && propertyNames.length == propertyTypes.length)) {
          logger.error("Unable to audit as length of params passed for state, type and names is not equal.");
        }

        User loggedUser = getUserService().getLoggedInUser();
        if (loggedUser == null) {
          throw new HealthkartDefaultWebException("NO_USER_LOGGED_IN");
        }
        String userEmail = loggedUser.getLogin();

        ChangeGroup changeGroup = new ChangeGroup();
        changeGroup.setAction(action.getActionType());
        changeGroup.setEntityName(entity.getClass().getSimpleName());
        changeGroup.setEntityId(entityId);
        changeGroup.setUserEmail(userEmail);


        int index = 0;
        boolean anyChanges = false;
        for (String propertyName : propertyNames) {
          if (trackableProperties.contains(propertyName)) {
            boolean isEqual = new EqualsBuilder().append(oldState[index], state[index]).isEquals();
            if (!isEqual) {
              ChangeItem changeItem = new ChangeItem();
              changeItem.setChangeGroup(changeGroup);
              changeItem.setFieldType(propertyTypes[index] == null ? null : propertyTypes[index].toString());
              changeItem.setFieldName(propertyName);
              changeItem.setNewValue(state[index] == null ? null : state[index].toString());
              changeItem.setOldValue(oldState[index] == null ? null : oldState[index].toString());
              changeGroup.getChangeItems().add(changeItem);
              anyChanges = true;
            }
          }
          index++;
        }

        if (anyChanges) {

          getAuditAPI().saveChangeGroup(changeGroup);

        }
      }
    }

    public AuditAPI getAuditAPI() {
      return auditAPI;
    }

    public UserService getUserService() {
      return userService;
    }
}
