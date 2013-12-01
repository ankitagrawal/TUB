package com.akube.framework.stripes.action;


/**
 * Abstract actionBean, needed for the RPC backend part of GWT
 */
public abstract class BaseGWTAction 
//extends RemoteServiceServlet implements ActionBean 
{

  /*private static final long serialVersionUID = 10001L;

  @SuppressWarnings({"GwtInconsistentSerializableClass"})
  private ActionBeanContext context;

  public Resolution defaultHandler() {
    return new Resolution() {
      public void execute(HttpServletRequest request, HttpServletResponse response)
          throws Exception {
        doPost(request, response);
      }
    };
  }

  public ActionBeanContext getContext() {
    return context;
  }

  public void setContext(final ActionBeanContext context) {
    this.context = context;
    // GWT servlet needs to be initialized with the servletConfig -
    // otherwise it can't access the servletContext
    try {
      super.init(new ServletConfig() {
        public String getInitParameter(String name) {
          return null;
        }

        public Enumeration<?> getInitParameterNames() {
          return new Enumeration<String>() {

            public boolean hasMoreElements() {
              return false;
            }

            public String nextElement() {
              throw new NoSuchElementException();
            }
          };
        }

        public ServletContext getServletContext() {
          return context.getServletContext();
        }

        public String getServletName() {
          return BaseGWTAction.class.getName();
        }
      });
    } catch (ServletException e) {
      throw new RuntimeException(e);
    }
  }

  
   ***************************************************************
   * copied from BaseAction as we cannot extend multiple classes
   * not sure if there's a DRY way to do it
   ***************************************************************
   
  public DefaultWebSecurityManager getSecurityManager() {
    return (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
  }

  protected void addValidationError(String resourceKey) {
    getContext().getValidationErrors().add(BaseUtils.getMD5Checksum(resourceKey), new LocalizableError(resourceKey));
  }

  protected void addValidationError(String resourceKey, String value) {
    getContext().getValidationErrors().add(BaseUtils.getMD5Checksum(resourceKey), new LocalizableError(resourceKey, value));
  }

  public List<ValidationError> getMessageList(ValidationErrors validationErrors) {
    List<ValidationError> messageList = new ArrayList<ValidationError>();
    Collection<List<ValidationError>> errorCol = validationErrors.values();
    for (List<ValidationError> errors : errorCol) {
      messageList.addAll(errors);
    }
    return messageList;
  }

  public void addRedirectAlertMessage(LocalizableMessage message) {
    List<net.sourceforge.stripes.action.Message> messages = getContext().getMessages("generalMessages");
    messages.add(message);
  }

  public PrincipalImpl getPrincipal() {
    return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
  }

  public Subject getSubject() {
    return SecurityUtils.getSubject();
  }

  public BreadcrumbInterceptor.Crumb getPreviousBreadcrumb() {
    return (BreadcrumbInterceptor.Crumb) getContext().getRequest().getSession().getAttribute(BreadcrumbInterceptor.BREADCRUMB_PREVIOUS_SESSION);
  }

  public void noCache() {
    HttpServletResponse response = getContext().getResponse();
    response.addHeader("Pragma", "no-cache");
    response.addHeader("Cache-Control", "no-cache");
    response.addHeader("Cache-Control", "private");
    response.addHeader("Cache-Control", "no-store");
    response.addHeader("Cache-Control", "max-age=0");
    response.addHeader("Cache-Control", "s-maxage=0");
    response.addHeader("Cache-Control", "must-revalidate");
    response.addHeader("Cache-Control", "proxy-revalidate");
  }
*/

}

