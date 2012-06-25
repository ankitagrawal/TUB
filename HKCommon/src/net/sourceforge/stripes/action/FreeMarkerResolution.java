package net.sourceforge.stripes.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.service.impl.FreeMarkerService;

public class FreeMarkerResolution implements Resolution {

  private final String templatePath;
  private final Object valueObject;
  private final FreeMarkerService freeMarkerService;

  public FreeMarkerResolution(String templatePath, Object valueObject, FreeMarkerService freeMarkerService) {
    this.freeMarkerService = freeMarkerService;
    this.valueObject = valueObject;
    this.templatePath = templatePath;
  }


  public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

    FreeMarkerService.RenderOutput renderOutput = freeMarkerService.getRenderOutputForTemplate(templatePath, valueObject);
    response.setContentType("text/html");
    response.getWriter().write(renderOutput.getMessage());
  }

}
