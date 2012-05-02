package com.akube.framework.stripes.controller;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;

/**
 * This interceptor simply sets the Session flush mode to MANUAL from AUTO See
 * {@link com.akube.framework.dao.BaseDao#save(Object)} java doc for more details
 */
@Intercepts(value = LifecycleStage.ActionBeanResolution)
public class SessionFlushModeChangeInterceptor implements Interceptor {

    public Resolution intercept(ExecutionContext context) throws Exception {
        /*
         * ServiceLocatorFactory.getService(Session.class).setFlushMode(FlushMode.MANUAL); return
         * context.proceed();
         */

        // TODO: rewrite
        return null;
    }

}
