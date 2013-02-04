package com.hk.web.action.pages;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;

@UrlBinding ("/valentines-day-2013")
public class ValentinesDayAction extends BaseAction {

	public Resolution pre() {
		return new ForwardResolution("/pages/campaign/valentines-day/14feb.jsp");
	}
}