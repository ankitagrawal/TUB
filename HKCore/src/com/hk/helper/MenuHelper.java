package com.hk.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.Product;
import com.hk.dto.menu.MenuNode;
import com.hk.util.MenuParser;

@Component
public class MenuHelper {

  final String appBasePath ="/";
  File menuFile;

  List<MenuNode> menuNodes;
  List<MenuNode> menuNodesFlat;

  
  /*public MenuHelper(//@Na.appBasePath) 
          String appbasePath) {
    this.appBasePath = appbasePath;
    menuFile = new File(appbasePath + "menu.txt");
    initMenuNodes();
  }*/
  
  public MenuHelper() {
    menuFile = new File("D:/eclipse_wrk/rewrite/HKWeb/view/menu.txt");
    initMenuNodes();
  }

  private void initMenuNodes() {
    menuNodes = MenuParser.parseMenu(menuFile);
    menuNodesFlat = new ArrayList<MenuNode>();

    for (MenuNode menuNode : this.menuNodes) {
      menuNodesFlat.addAll(getMenuNodesFlat(menuNode));
    }
  }

  public List<MenuNode> getMenuNodes() {
    return menuNodes;
  }

  public List<MenuNode> getMenuNodesFlat() {
    return menuNodesFlat;
  }

  private List<MenuNode> getMenuNodesFlat(MenuNode menuNode) {
    List<MenuNode> menuNodes = new ArrayList<MenuNode>();

    menuNodes.add(menuNode);
    if (menuNode.getChildNodes() != null && !menuNode.getChildNodes().isEmpty()) {
      for (MenuNode node : menuNode.getChildNodes()) {
        menuNodes.addAll(getMenuNodesFlat(node));
      }
    }

    return menuNodes;
  }

  public void refresh() {
    initMenuNodes();
  }

  public List<MenuNode> getSiblings(String urlFragment) {
    return getMatchingSiblings(urlFragment, menuNodes);
  }

  public MenuNode getMenuNode(String urlFragment) {
    return getMatchingMenuNode(urlFragment, menuNodes);
  }

  private MenuNode getMatchingMenuNode(String urlFragment, List<MenuNode> menuNodes) {
    for (MenuNode menuNode : menuNodes) {
      if (menuNode.getUrl().equals(urlFragment)) {
        return menuNode;
      } else {
        MenuNode matchingMenuNode = getMatchingMenuNode(urlFragment, menuNode.getChildNodes());
        if (matchingMenuNode != null) return matchingMenuNode;
      }
    }
    return null;
  }

  private List<MenuNode> getMatchingSiblings(String urlFragment, List<MenuNode> menuNodes) {
    for (MenuNode menuNode : menuNodes) {
      if (menuNode.getUrl().equals(urlFragment)) {
        return menuNodes;
      } else {
        List<MenuNode> matchingSiblings = getMatchingSiblings(urlFragment, menuNode.getChildNodes());
        if (matchingSiblings != null) return matchingSiblings;
      }
    }
    return null;
  }

  public MenuNode getTopParentMenuNode(MenuNode menuNode) {
    while (menuNode.getParentNode() != null) {
      menuNode = menuNode.getParentNode();
    }
    return menuNode;
  }

  public List<MenuNode> getHierarchicalMenuNodes(MenuNode menuNode) {
    List<MenuNode> hierarchicalMenuNodes = new ArrayList<MenuNode>();
    while (menuNode.getParentNode() != null) {
      hierarchicalMenuNodes.add(menuNode);
      menuNode = menuNode.getParentNode();
    }
    return hierarchicalMenuNodes;
  }

  public String getUrlFragementFromProduct(Product breadcrumbProduct) {
    MenuNode finalMenuNode = getMenoNodeFromProduct(breadcrumbProduct);
    return finalMenuNode == null ? null : finalMenuNode.getUrl();
  }

  public MenuNode getMenoNodeFromProduct(Product breadcrumbProduct) {
    MenuNode finalMenuNode = null;
    for (MenuNode menuNode : menuNodesFlat) {
      if (menuNode.hasProduct(breadcrumbProduct)) {
        if (finalMenuNode == null) {
          finalMenuNode = menuNode;
        } else if (menuNode.getLevel() > finalMenuNode.getLevel()) {
          finalMenuNode = menuNode;
        }
      }
    }
    return finalMenuNode;
  }

  public String getTopCategorySlug(MenuNode menuNode) {
    if (menuNode == null) {
      return null;
    }
    while (menuNode.getParentNode() != null) {
      menuNode = menuNode.getParentNode();
    }
    return menuNode.getSlug();
  }

  public String getAllCategoriesString(MenuNode menuNode) {
    StringBuffer allCategories = new StringBuffer();
    if (menuNode != null) {
      allCategories.append(menuNode.getSlug());
    } else {
      return "";
    }
    while (menuNode.getParentNode() != null) {
      allCategories.append(",").append(menuNode.getParentNode().getSlug());
      menuNode = menuNode.getParentNode();
    }
    return allCategories.toString();
  }

  public List<String> getAllCategoriesList(MenuNode menuNode) {
    List<String> categoryNames = new ArrayList<String>();
    if (menuNode != null) {
        categoryNames.add(menuNode.getSlug());
    } else {
      return null;
    }
    while (menuNode.getParentNode() != null) {
      categoryNames.add(menuNode.getParentNode().getSlug());
      menuNode = menuNode.getParentNode();
    }
    return categoryNames;
  }

}
