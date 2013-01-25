package com.hk.web.action.pages;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;

@UrlBinding ("/clearance-sale")
public class ClearanceSaleAction extends BaseAction {

	public Resolution pre() {
		return new ForwardResolution("/pages/26jan.jsp");
	}
}