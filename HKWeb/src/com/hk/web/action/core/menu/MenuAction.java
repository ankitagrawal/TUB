package com.hk.web.action.core.menu;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;


public class MenuAction extends BaseAction {

  List<MenuNode> menuNodes;

  @Autowired
  MenuHelper menuHelper;

  @DefaultHandler
  public Resolution pre() {
    //TODO #introducing gc as a hit n try solution for server performance
    System.gc();
    menuNodes = menuHelper.getMenuNodes();
    return new ForwardResolution("/includes/_menu.jsp");
  }

  public List<MenuNode> getMenuNodes() {
    return menuNodes;
  }

}