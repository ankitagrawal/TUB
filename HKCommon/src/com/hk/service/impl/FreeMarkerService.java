package com.hk.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.akube.framework.util.BaseUtils;
import com.hk.web.AppConstants;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class FreeMarkerService {

  private static Logger logger = LoggerFactory.getLogger(FreeMarkerService.class);

  private Configuration cfg = null;

  //TODO: rewrite
  /*@Autowired
  public FreeMarkerService(Configuration cfg) {
      this.cfg = cfg;
  }*/


  /**
   * This method takes a template and template values and tries to return a rendered message The first line is taken
   * as the subject and the next line onwards is the message body The method eats any render errors and returns a null
   *
   * @param templatePath
   * @param templateValues
   * @return
   */
  public RenderOutput getRenderOutputForTemplate(String templatePath, Object templateValues) {
        Template template;
        RenderOutput renderOutput = null;
        try {
            File freemarkerDir = new File(AppConstants.appBasePath + "/freemarker");
            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(freemarkerDir);
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            // load a freemarker template from a pre-configured directory

            template = cfg.getTemplate(templatePath);

            // process template with the given input
            StringWriter stringWriter = new StringWriter();
            template.process(templateValues, stringWriter);

            // the first line in the template is the email subject
            // the rest is the html body
            String out = stringWriter.toString();
            String subject = out.substring(0, out.indexOf(BaseUtils.newline));
            String body = out.substring(out.indexOf(BaseUtils.newline) + 1, out.length());

            renderOutput = new RenderOutput(subject, body);
        } catch (IOException e) {
            logger.error("IOException in getRenderOutputForTemplate for template " + templatePath, e);
        } catch (TemplateException e) {
            logger.error("TemplateException in getRenderOutputForTemplate for template " + templatePath, e);
        }
        return renderOutput;
    }

  public RenderOutput getRenderOutputFromString(String template, Object templateValues) {
    Template templateGenerated;
    RenderOutput renderOutput = null;
    try {
        //generate template from a string
        templateGenerated = new Template("template", new StringReader(template), new Configuration());

      // process templateGenerated with the given input
      StringWriter stringWriter = new StringWriter();
      templateGenerated.process(templateValues, stringWriter);

      // the first line in the templateGenerated is the email subject
      // the rest is the html body
      String out = stringWriter.toString();
      String body = out.substring(out.indexOf(BaseUtils.newline) + 1, out.length());

      renderOutput = new RenderOutput(null, body);
    } catch (IOException e) {
      logger.error("IOException in getRenderOutputFromString for templateGenerated ", e);
    } catch (TemplateException e) {
      logger.error("TemplateException in getRenderOutputFromString for templateGenerated ", e);
    }
    return renderOutput;
  }

  public class RenderOutput {
    private String subject;
    private String message;

    public RenderOutput(String subject, String message) {
      this.subject = subject;
      this.message = message;
    }

    public String getSubject() {
      return subject;
    }

    public void setSubject(String subject) {
      this.subject = subject;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }

}
