package com.hk.web.action.test;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;

/**
 * Created by IntelliJ IDEA.
 * User: nitin
 * Date: 13 May, 2011
 * Time: 12:45:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SayHelloAction extends BaseAction {

  String name;

  public Resolution pre() {
    name = name + " concat";
    return new ForwardResolution("/pages/test/hello.jsp"); 
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
