package net.sourceforge.stripes.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class PermanentRedirectResolution extends RedirectResolution {
  public PermanentRedirectResolution(String url) {
    super(url);
  }

  public PermanentRedirectResolution(String url, boolean prependContext) {
    super(url, prependContext);
  }

  public PermanentRedirectResolution(Class<? extends ActionBean> beanType) {
    super(beanType);
  }

  public PermanentRedirectResolution(Class<? extends ActionBean> beanType, String event) {
    super(beanType, event);
  }

  @Override
  public void execute(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
      System.out.println("in  PERMA RESOULTION");
    res.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);

    HttpServletResponseWrapper resWrap = new
        HttpServletResponseWrapper(res) {
          @Override
          public void setStatus(int sc) {
            // Short circuit allowing the status to ever change (already set to SC_MOVED_PERMANENTLY)
          }

          @Override
          public void sendRedirect(String location) throws IOException {
              System.out.println("EXCEUTING  PERMA RESOULTION");
            setHeader("Location", location);
          }
        };
    super.execute(req, resWrap);
  }


}
