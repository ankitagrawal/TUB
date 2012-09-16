package com.hk.web.action.pages;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import com.akube.framework.stripes.action.BaseAction;

@UrlBinding ("/dmp")
public class DiabetesManagementProgramAction extends BaseAction {

	public Resolution pre() {
		return new ForwardResolution("/pages/lp/hk_dmp/hk-dmp.jsp");
	}
}