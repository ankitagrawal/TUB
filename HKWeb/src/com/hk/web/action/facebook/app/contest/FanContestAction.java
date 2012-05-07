package com.hk.web.action.facebook.app.contest;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.restfb.types.Page;
import com.restfb.types.User;

public class FanContestAction extends BaseAction {

  User fbUser;
  Page fbPage;

  String access_token;
  String uid;

  public Resolution pre() {

      //TODO: rewrite
   // access_token = FbSessionHelper.getCookieParam(getContext().getRequest(), FbConstants.contestAppId, FbConstants.Param.access_token);
   // uid = FbSessionHelper.getCookieParam(getContext().getRequest(), FbConstants.contestAppId, FbConstants.Param.uid);

//    fbUser = facebookClient.fetchObject("me", User.class);
//    fbPage = facebookClient.fetchObject("healthkart", Page.class);

    return new ForwardResolution("/pages/facebook/app/contest/fanContest.jsp");
  }

  public User getFbUser() {
    return fbUser;
  }

  public Page getFbPage() {
    return fbPage;
  }

  public String getAccess_token() {
    return access_token;
  }

  public String getUid() {
    return uid;
  }
}
