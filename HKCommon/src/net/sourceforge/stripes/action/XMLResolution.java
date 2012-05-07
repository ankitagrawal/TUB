package net.sourceforge.stripes.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;

/**
 * User: kani
 * Time: 23 Jul, 2009 2:54:29 PM
 */
public class XMLResolution implements Resolution {

  private Document document;

  public XMLResolution(Document document) {
    this.document = document;
  }

  public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    response.setContentType("text/xml");
    response.getWriter().write(document.asXML());
    response.flushBuffer();
  }
}
