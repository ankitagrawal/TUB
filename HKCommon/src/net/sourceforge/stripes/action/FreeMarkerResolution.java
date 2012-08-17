package net.sourceforge.stripes.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.service.impl.FreeMarkerService;

import freemarker.template.Template;

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
    Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(templatePath);
    FreeMarkerService.RenderOutput renderOutput = freeMarkerService.getRenderOutputForTemplate(freemarkerTemplate, valueObject);
    response.setContentType("text/html");
    response.getWriter().write(renderOutput.getMessage());
  }

}
