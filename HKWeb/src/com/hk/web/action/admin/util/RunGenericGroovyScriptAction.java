package com.hk.web.action.admin.util;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.GenericGroovy.GenericGroovyUtil;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class RunGenericGroovyScriptAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(RunGenericGroovyScriptAction.class);


	@Value("#{hkEnvProps['" + Keys.Env.dbHostName + "']}")
	private String dbHostName;

	@Value("#{hkEnvProps['" + Keys.Env.dbName + "']}")
	private String dbName;

	@Value("#{hkEnvProps['" + Keys.Env.dbUser + "']}")
	private String dbUser;

	@Value("#{hkEnvProps['" + Keys.Env.dbPassword + "']}")
	private String dbPassword;


	public Resolution pre() {

		try {
			GenericGroovyUtil genericGroovyUtil = new GenericGroovyUtil(dbHostName, dbName, dbUser, dbPassword);

			//genericGroovyUtil.performScript();

		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Script ran successfully"));
		return new RedirectResolution("/pages/admin/adminHome.jsp");
	}
}