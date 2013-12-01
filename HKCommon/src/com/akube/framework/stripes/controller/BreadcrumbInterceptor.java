package com.akube.framework.stripes.controller;

import java.io.Serializable;
import java.util.Stack;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.StripesConstants;
import net.sourceforge.stripes.util.bean.BeanUtil;
import net.sourceforge.stripes.util.bean.EvaluationException;
import net.sourceforge.stripes.util.bean.ParseException;

import org.apache.commons.lang.StringUtils;

import com.hk.web.AppConstants;

/**
 * Author: Kani
 * Date: Jan 16, 2009
 */
@Intercepts(value = LifecycleStage.ResolutionExecution)
public class BreadcrumbInterceptor implements Interceptor {
  public static final String BREADCRUMB_STACK_SESSION = "breadcrumbSession";
  public static final String BREADCRUMB_PREVIOUS_SESSION = "breadcrumbPreviousSession";

  @SuppressWarnings("serial")
public static class Crumb implements Serializable {
    private final int level;
    private final String url;
    private final String name;
    private final String context;

    public Crumb(int level, String url, String name, String context) {
      this.level = level;
      this.url = url;
      this.name = name;
      this.context = context;
    }

    public int getLevel() {
      return level;
    }

    public String getUrl() {
      return url;
    }

    public String getName() {
      return name;
    }

    public String getContext() {
      return context;
    }
  }

  public Resolution intercept(ExecutionContext executionContext) throws Exception {
    Stack<Crumb> breadcrumbStack = getBreadcrumbStack(executionContext);
    if (executionContext.getActionBean().getClass().isAnnotationPresent(Breadcrumb.class)) {
      Breadcrumb breadcrumb = executionContext.getActionBean().getClass().getAnnotation(Breadcrumb.class);
      String name = buildBreadcrumbNameFast(breadcrumb.name(), executionContext.getActionBean());
      updateBreadcrumb(executionContext, breadcrumbStack, breadcrumb.level(), name, breadcrumb.context());
    } else {
      // should we destroy breadcrumb?? not too sure about this. better would be to maybe add a crumb with default size
    }

    return executionContext.proceed();
  }

  /**
   * Method to inject bean properties into breadcrumb name dynamically
   *
   * For eg.
   *  if a breadcrumb name is given as
   *  Album: {album.title}
   *
   *  the the album.title property will be looked up in the action bean and replaced.
   *
   * @param breadcrumbUri
   * @param bean
   * @return
   */
  private String buildBreadcrumbNameFast(String breadcrumbUri, Object bean) {
    StringBuffer fullName = new StringBuffer();
    StringBuffer currentPlaceholder = new StringBuffer();

    int currentChar = 0;
    boolean placeholderStarted = false;

    while (currentChar < breadcrumbUri.length()) {
      switch (breadcrumbUri.charAt(currentChar)) {
        case '{':
          placeholderStarted = true;
          break;
        case '}':
          Object value = null;
          try {
            value = BeanUtil.getPropertyValue(currentPlaceholder.toString(), bean);
            fullName.append(value);
          } catch (ParseException e) {
          } catch (EvaluationException e) {
          }
          placeholderStarted = false;
          currentPlaceholder = new StringBuffer();
          break;
        default:
          if (!placeholderStarted) fullName.append(breadcrumbUri.charAt(currentChar));
          else currentPlaceholder.append(breadcrumbUri.charAt(currentChar));
      }

      currentChar ++;
    }

    return fullName.toString();
  }

  // this method updates the breadcrumb stack stored in the session
  private void updateBreadcrumb(ExecutionContext executionContext, Stack<Crumb> breadcrumbStack, int level, String name, String crumbContext) {
    Crumb currentTopCrumb = null;
    if (breadcrumbStack.size() >= 1) {
      currentTopCrumb = breadcrumbStack.peek();
    }

    // if crumbContext is blank, then inherit crumbContext
    if (StringUtils.isBlank(crumbContext) && currentTopCrumb != null && currentTopCrumb.getLevel() < level) {
      crumbContext = currentTopCrumb.getContext();
    }

    // check context
    while (currentTopCrumb != null) {
      if (StringUtils.isBlank(currentTopCrumb.getContext()) ||
          currentTopCrumb.getContext().equals(crumbContext)) {
        break;
      }
      breadcrumbStack.pop();
      if (breadcrumbStack.size() >= 1) {
        currentTopCrumb = breadcrumbStack.peek();
      } else {
        currentTopCrumb = null;
      }
    }

    while (currentTopCrumb != null && currentTopCrumb.getLevel() >= level) {
      breadcrumbStack.pop();
      if (breadcrumbStack.size() >= 1) {
        currentTopCrumb = breadcrumbStack.peek();
      } else {
        currentTopCrumb = null;
      }
    }

    HttpSession session = executionContext.getActionBeanContext().getRequest().getSession();
    session.setAttribute(BREADCRUMB_PREVIOUS_SESSION, currentTopCrumb);

    StringBuilder urlBuilder = new StringBuilder().append(executionContext.getActionBeanContext().getRequest().getRequestURI());
    if (executionContext.getActionBeanContext().getRequest().getQueryString() != null) {
      
      // remove __fsk from query string
      String qs = executionContext.getActionBeanContext().getRequest().getQueryString();
      if (qs.indexOf(StripesConstants.URL_KEY_FLASH_SCOPE_ID) != -1) {
        // rebuild qs without __fsk (the stripes flash scope constant)
        String[] qsParams = qs.split("&");
        qs = "";
        for (String qsParam : qsParams) {
          if (qsParam.indexOf(StripesConstants.URL_KEY_FLASH_SCOPE_ID) != 0) {
            qs += qsParam + "&";
          }
        }
        // remove the last &
        qs = qs.length()>0?qs.substring(0, qs.length()-1):"";
      }
      urlBuilder.append("?").append(qs);

    }
    Crumb newCrumb = new Crumb(level, urlBuilder.toString(), name, crumbContext);
    breadcrumbStack.push(newCrumb);
  }

  @SuppressWarnings("unchecked")
private Stack<Crumb> getBreadcrumbStack(ExecutionContext executionContext) {
    HttpSession session = executionContext.getActionBeanContext().getRequest().getSession();
    //noinspection unchecked
    Stack<Crumb> stack = (Stack<Crumb>) session.getAttribute(BREADCRUMB_STACK_SESSION);
    if (stack == null) {
      stack = getDefaulBreadcrumb(session);
    }
    return stack;
  }

  private static Stack<Crumb> getDefaulBreadcrumb(HttpSession session) {
    Stack<Crumb> stack;
    stack = new Stack<Crumb>();
    String contextPath = AppConstants.contextPath;
    //String url = new StringBuilder().append(contextPath).append(StripesFilter.getConfiguration().getActionResolver().getUrlBinding(HomeAction.class)).toString();
    String url = new StringBuilder().append(contextPath).append("/Home.action").toString();
    Crumb homeCrumb = new Crumb(0, url, "Home", null);
    stack.push(homeCrumb);
    session.setAttribute(BREADCRUMB_STACK_SESSION, stack);
    
    return stack;
  }

  @SuppressWarnings("unchecked")
public static Crumb getCurrentBreadcrumb(HttpSession session) {
    Stack<Crumb> stack = (Stack<Crumb>) session.getAttribute(BreadcrumbInterceptor.BREADCRUMB_STACK_SESSION);
    if (stack == null) {
      stack = getDefaulBreadcrumb(session);
    }
    return stack.peek();
  }

  public static Crumb getPreviousBreadcrumb(HttpSession session) {
    Crumb previousCrumb = (Crumb) session.getAttribute(BreadcrumbInterceptor.BREADCRUMB_PREVIOUS_SESSION);
    if (previousCrumb == null) {
      previousCrumb = getCurrentBreadcrumb(session);
    }
    return previousCrumb;
  }

}
