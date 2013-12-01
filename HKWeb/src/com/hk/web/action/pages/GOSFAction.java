package com.hk.web.action.pages;

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

/**
 * Created by IntelliJ IDEA.
 * User: Rahul
 * Date: Dec 7, 2012
 * Time: 4:01:05 PM
 * To change this template use File | Settings | File Templates.
 */
@UrlBinding("/online-shopping-festival")

public class GOSFAction extends BaseAction {

	public Resolution pre() {
		return new ForwardResolution("/pages/gosf.jsp");
	}
}
