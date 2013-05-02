package com.hk.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.hk.domain.catalog.product.Product;
import com.hk.dto.menu.MenuNode;
import com.hk.util.MenuParser;

@Component
public class MenuHelper implements ServletContextAware{

  private String appBasePath;
  File menuFile;

  List<MenuNode> menuNodes;
  List<MenuNode> menuNodesFlat;
  

  
  public void postConstruction() {
     /* this.appBasePath = AppConstants.appBasePath;
      menuFile = new File(appBasePath + "menu.txt");
      initMenuNodes();*/
  }
  
  public MenuHelper(){

  }
  
  @Override
  public void setServletContext(ServletContext servletContext) {
      this.appBasePath = servletContext.getRealPath("/");
      this.menuFile = new File(this.appBasePath + "menu.txt");
      this.initMenuNodes();
  }
  
  /*
     * public MenuHelper() { menuFile = new File("D:/eclipse_wrk/rewrite/HKWeb/view/menu.txt"); initMenuNodes(); }
     */

 /* public MenuHelper() {
      this.appBasePath = AppConstants.appBasePath;
      menuFile = new File(appBasePath + "menu.txt");
      initMenuNodes();
  }*/



private void initMenuNodes() {
    this.menuNodes = MenuParser.parseMenu(this.menuFile);
    this.menuNodesFlat = new ArrayList<MenuNode>();

    for (MenuNode menuNode : this.menuNodes) {
      this.menuNodesFlat.addAll(this.getMenuNodesFlat(menuNode));
    }
  }

  public List<MenuNode> getMenuNodes() {
    return this.menuNodes;
  }

  public List<MenuNode> getMenuNodesFlat() {
    return this.menuNodesFlat;
  }

  private List<MenuNode> getMenuNodesFlat(MenuNode menuNode) {
    List<MenuNode> menuNodes = new ArrayList<MenuNode>();

    menuNodes.add(menuNode);
    if (menuNode.getChildNodes() != null && !menuNode.getChildNodes().isEmpty()) {
      for (MenuNode node : menuNode.getChildNodes()) {
        menuNodes.addAll(this.getMenuNodesFlat(node));
      }
    }

    return menuNodes;
  }

  public void refresh() {
    this.initMenuNodes();
  }

  public List<MenuNode> getSiblings(String urlFragment) {
    return this.getMatchingSiblings(urlFragment, this.menuNodes);
  }

  public MenuNode getMenuNode(String urlFragment) {
    return this.getMatchingMenuNode(urlFragment, this.menuNodes);
  }

  private MenuNode getMatchingMenuNode(String urlFragment, List<MenuNode> menuNodes) {
    for (MenuNode menuNode : menuNodes) {
      if (menuNode.getUrl().equals(urlFragment)) {
        return menuNode;
      } else {
        MenuNode matchingMenuNode = this.getMatchingMenuNode(urlFragment, menuNode.getChildNodes());
        if (matchingMenuNode != null) {
			return matchingMenuNode;
		}
      }
    }
    return null;
  }

  private List<MenuNode> getMatchingSiblings(String urlFragment, List<MenuNode> menuNodes) {
    for (MenuNode menuNode : menuNodes) {
      if (menuNode.getUrl().equals(urlFragment)) {
        return menuNodes;
      } else {
        List<MenuNode> matchingSiblings = this.getMatchingSiblings(urlFragment, menuNode.getChildNodes());
        if (matchingSiblings != null) {
			return matchingSiblings;
		}
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
    MenuNode finalMenuNode = this.getMenoNodeFromProduct(breadcrumbProduct);
    return finalMenuNode == null ? null : finalMenuNode.getUrl();
  }

  public MenuNode getMenoNodeFromProduct(Product breadcrumbProduct) {
    MenuNode finalMenuNode = null;
    for (MenuNode menuNode : this.menuNodesFlat) {
      if (menuNode.hasProduct(breadcrumbProduct) && this.getTopCategorySlug(menuNode).equals(breadcrumbProduct.getPrimaryCategory().getName())) {
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
