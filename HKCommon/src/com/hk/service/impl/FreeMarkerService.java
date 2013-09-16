package com.hk.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import com.hk.constants.core.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("#{hkEnvProps['" + Keys.Env.hybridRelease + "']}")
    private boolean           hybridRelease;

  /*private Configuration cfg = null;*/

  //TODO: rewrite
  /*@Autowired
  public FreeMarkerService(Configuration cfg) {
      this.cfg = cfg;
  }*/


  /**
   * This method takes a template's path, searches the corresponding template in the freemarker directory,
   * and then returns a freemarker template object for the template file found.
   *
   * @param templatePath
   * @return
   */
  public Template getCampaignTemplate(String templatePath) {
    if(isHybridRelease()){
        String ftlName = templatePath.split(".")[0];
        templatePath = ftlName + "Beta" + ".ftl";
    }
    Template template = null;
    try {
      //File freemarkerDir = new File(AppConstants.appBasePath + "/freemarker");
      File templateFile=new File(AppConstants.appBasePath+"/freemarker"+templatePath);
      Configuration cfg = new Configuration();
      cfg.setDirectoryForTemplateLoading(templateFile.getParentFile());
      cfg.setObjectWrapper(new DefaultObjectWrapper());
      // load a freemarker template from a pre-configured directory
      template = cfg.getTemplate(templateFile.getName());
    } catch (IOException e) {
      logger.error("IOException in getCampaignTemplate for template " + templatePath, e);
    }
    return template;
  }

  /**
   * This method takes the template content as a string and returns a freemarker template object corresponding to the same.
   *
   * @param templateContent
   * @return
   */
  public Template getCampaignTemplateFromString(String templateContent) {
    Template template = null;
    try {
      template = new Template("template", new StringReader(templateContent), new Configuration());
    } catch (IOException ioe) {
      logger.error("IOException in getCampaignTemplateFromString for template ", ioe);
    }
    return template;
  }

  /**
   * This method takes a freemarker template and template values and tries to process it and return a rendered message.
   * The first line is taken as the subject and the next line onwards is the message body.
   * The method eats any render errors and returns a null
   *
   * @param template
   * @param templateValues
   * @return
   */
  public RenderOutput processCampaignTemplate(Template template, Object templateValues) {
    RenderOutput renderOutput = null;
    try {
      StringWriter stringWriter = new StringWriter();
      template.process(templateValues, stringWriter);
      // the first line in the template is the email subject
      // the rest is the html body
      String out = stringWriter.toString();
      String subject = out.substring(0, out.indexOf(BaseUtils.newline));
      String body = out.substring(out.indexOf(BaseUtils.newline) + 1, out.length());

      renderOutput = new RenderOutput(subject, body);
    } catch (IOException e) {
      logger.error("IOException in getRenderOutputForTemplate for template ", e);
    } catch (TemplateException e) {
      logger.error("TemplateException in getRenderOutputForTemplate for template ", e);
    }
    return renderOutput;
  }

	public RenderOutput processSmsTemplate(Template template, Object templateValues) {
		RenderOutput renderOutput = null;
		try {
			StringWriter stringWriter = new StringWriter();
			template.process(templateValues, stringWriter);
			// the first line in the template is the email subject
			// the rest is the html body
			String body = stringWriter.toString();

			renderOutput = new RenderOutput("", body);
		} catch (IOException e) {
			logger.error("IOException in getRenderOutputForTemplate for template ", e);
		} catch (TemplateException e) {
			logger.error("TemplateException in getRenderOutputForTemplate for template ", e);
		}
		return renderOutput;
	}


	/**
   * This method takes a template's path and template values and tries to return a rendered message.
   *
   * @param template
   * @param templateValues
   * @return
   */
  public RenderOutput getRenderOutputForTemplate(Template template, Object templateValues) {
    return processCampaignTemplate(template, templateValues);
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

    public boolean isHybridRelease() {
        return hybridRelease;
    }
}
