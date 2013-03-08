package com.hk.listener;

import java.util.Set;

import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hk.pact.service.AnnotationsAPI;
import com.hk.pact.service.audit.AuditAction;
import com.hk.pact.service.audit.AuditService;
import com.hk.pact.service.audit.Auditable;
import com.hk.service.ServiceLocatorFactory;

/**
 * @author vaibhav.adlakha
 */
@SuppressWarnings("serial")
public class EntityAuditEventListener implements PostUpdateEventListener, PostDeleteEventListener, PostInsertEventListener {

    private static Logger  logger = LoggerFactory.getLogger(EntityAuditEventListener.class);

    private AuditService   auditService;

    private AnnotationsAPI annotationsAPI;

    @Override
    public void onPostUpdate(PostUpdateEvent postUpdateEvent) {
        String[] propertyTypeNames = getPropertyTypeNamesFromProprtyTypes(postUpdateEvent.getPersister().getPropertyTypes());

        String entityId = postUpdateEvent.getId().toString();
        

        if (entityId != null && postUpdateEvent.getEntity() != null && isAuditable(postUpdateEvent.getEntity().getClass())) {

            getAuditService().audit(AuditAction.UPDATE, postUpdateEvent.getEntity(), entityId, postUpdateEvent.getState(), postUpdateEvent.getOldState(),
                    postUpdateEvent.getPersister().getPropertyNames(), propertyTypeNames);

            /*
             * logger.info(entityId + " : " + postUpdateEvent.getState() + " : " + postUpdateEvent.getOldState() + " : " +
             * postUpdateEvent.getPersister().getPropertyNames() + " : " + propertyTypeNames);
             */
        }
    }



    @Override
    public void onPostInsert(PostInsertEvent postInsertEvent) {
        String[] propertyTypeNames = getPropertyTypeNamesFromProprtyTypes(postInsertEvent.getPersister().getPropertyTypes());
        
        String entityId = postInsertEvent.getId().toString();

        if (entityId != null && postInsertEvent.getEntity() != null && isAuditable(postInsertEvent.getEntity().getClass())) {
            getAuditService().audit(AuditAction.INSERT, postInsertEvent.getEntity(), entityId, postInsertEvent.getState(), new Object[postInsertEvent.getState().length],
                    postInsertEvent.getPersister().getPropertyNames(), propertyTypeNames);

            /*
             * logger.info(entityId + " : " + postInsertEvent.getState() + " : " + new
             * Object[postInsertEvent.getState().length] + " : " + postInsertEvent.getPersister().getPropertyNames() + " : " +
             * propertyTypeNames);
             */
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent postDeleteEvent) {
        String[] propertyTypeNames = getPropertyTypeNamesFromProprtyTypes(postDeleteEvent.getPersister().getPropertyTypes());
        
        
        if (postDeleteEvent.getEntity() != null && isAuditable(postDeleteEvent.getEntity().getClass())) {
            getAuditService().audit(AuditAction.DELETE, postDeleteEvent.getEntity(), postDeleteEvent.getId().toString(),
                    new Object[postDeleteEvent.getDeletedState().length], postDeleteEvent.getDeletedState(), postDeleteEvent.getPersister().getPropertyNames(), propertyTypeNames);
        }

        /*
         * logger.info(postDeleteEvent.getEntity() + " : " + Integer.parseInt(postDeleteEvent.getId().toString()) + " : " +
         * new Object[postDeleteEvent.getDeletedState().length] + " : " + postDeleteEvent.getDeletedState() + " : " +
         * postDeleteEvent.getPersister().getPropertyNames() + " : " + propertyTypeNames);
         */
    }

    private String[] getPropertyTypeNamesFromProprtyTypes(Type[] propertyTypes) {
        String[] propertyTypeNames = new String[propertyTypes.length];
        for (int i = 0; i < propertyTypes.length; i++) {
            Type type = propertyTypes[i];
            propertyTypeNames[i] = type.getName();
        }

        return propertyTypeNames;
    }

    private boolean isAuditable(Class<?> clazz) {
        Set<AnnotationDesc> auditableList = getAnnotationsAPI().findAnnotationsOnClass(clazz, Auditable.class);
        if (auditableList != null && auditableList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public AuditService getAuditService() {
        if (auditService == null) {
            auditService = (AuditService) ServiceLocatorFactory.getService(AuditService.class);
        }
        return auditService;
    }

    public AnnotationsAPI getAnnotationsAPI() {
        if (annotationsAPI == null) {
            annotationsAPI = (AnnotationsAPI) ServiceLocatorFactory.getService(AnnotationsAPI.class);
        }
        return annotationsAPI;
    }
}
