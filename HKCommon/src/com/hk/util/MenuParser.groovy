package com.hk.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import com.hk.dto.menu.MenuNode;

public class MenuParser {
    

  public static List<MenuNode> parseMenu(File menuFile) {
    MenuNode prevNode = null
    List<MenuNode> menuNodes = new ArrayList()
    int prevIdentation = 0

    menuFile.eachLine {
      line ->

      
      if (StringUtils.isNotBlank(line)) {
        def m = line =~ /^[-]*/
        int identation = m[0] ? m[0].size() : 0

        // get name and URL
        String name = ""
        String url = ""
        if (line.indexOf(':') != -1) {
          String[] lineTokens = StringUtils.split(line, ':')
          url = lineTokens[0].trim().replaceAll(/^[-]*/,'')
          name = lineTokens[1].trim()
        }
        if (StringUtils.isBlank(url)) {
          url = line.trim().replaceAll(/^[-]*/,'')
        }
        if (StringUtils.isBlank(name)) {
          name = url.trim().substring(url.lastIndexOf('/') + 1, url.size())
          name = name.replaceAll("-", " ")
          name = WordUtils.capitalize(name)
        }


        // Node creaion - depending on three conditions
        // create a parent, sibling or a child
        MenuNode menuNode = null;

        if (identation == prevIdentation) {
          // create a new sibling node
          menuNode = new MenuNode(name: name, url: url)
          if (prevNode == null || prevNode.getParentNode() == null) {
            menuNodes.add(menuNode)
          } else {
            menuNode.setParentNode(prevNode.getParentNode())
            prevNode.getParentNode().getChildNodes().add(menuNode)
          }
          prevNode = menuNode
        } else if (identation < prevIdentation) {
          // go back and create a new parent level node
          // find out how many levels to go back
          for (int i = 0; i < prevIdentation - identation; i++) {
            prevNode = prevNode.getParentNode()
          }
          menuNode = new MenuNode(name: name, url: url)
          if (prevNode == null || prevNode.getParentNode() == null) {
            menuNodes.add(menuNode)
          } else {
            menuNode.setParentNode(prevNode.getParentNode())
            prevNode.getParentNode().getChildNodes().add(menuNode)
          }
          prevNode = menuNode
        } else { // which means (identation > prevIdentation)
          // create a new child node
          if ((identation - prevIdentation) > 1) throw new RuntimeException("Ident cannot increase by more than 1!")
          menuNode = new MenuNode(name: name, url: url, parentNode: prevNode)
          prevNode.getChildNodes().add(menuNode)
          prevNode = menuNode
        }
        menuNode.level = identation

        prevIdentation = identation
      }

    }

    return menuNodes
  }

}