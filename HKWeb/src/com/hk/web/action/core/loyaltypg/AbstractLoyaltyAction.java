package com.hk.web.action.core.loyaltypg;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.RoleConstants;
import com.hk.store.StoreProcessor;

@Secure(hasAnyRoles = {RoleConstants.HK_USER}, authActionBean=SignInAction.class)
public abstract class AbstractLoyaltyAction extends BasePaginatedAction {
	
	@Qualifier("loyaltyStoreProcessor")
	@Autowired
	private StoreProcessor processor;
	
	public StoreProcessor getProcessor() {
		return processor;
	}

	@Override
	public int getPerPageDefault() {
		return 0;
	}

	@Override
	public int getPageCount() {
		return 0;
	}

	@Override
	public int getResultCount() {
		return 0;
	}

	@Override
	public Set<String> getParamSet() {
		return null;
	}
}
