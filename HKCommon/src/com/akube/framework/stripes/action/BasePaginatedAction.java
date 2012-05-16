package com.akube.framework.stripes.action;
import java.util.Set;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.controller.LifecycleStage;

import org.springframework.stereotype.Component;

/**
 * Author: Kani
 * Date: Nov 26, 2008
 */
@Component
public abstract class BasePaginatedAction 
extends BaseAction 
{
  protected int pageNo;
  int perPage;

  public abstract int getPerPageDefault();
  public abstract int getPageCount();
  public abstract int getResultCount();
  public abstract Set<String> getParamSet();

  /**
   * When one is viewing sent messages and clicks on any of
   * the pagination links, the default handler is invoked.
   *
   * This parameter is being used to get around that.
   *
   */
  private String eventParam;

  public int getPageDefault() {
    return 1;
  }

  public int getPageNo() {
    return pageNo <= 0 ? 1 : pageNo;
  }

  public void setPageNo(int pageNo) {
    this.pageNo = pageNo;
  }

  public int getPerPage() {
    return perPage <= 0 ? getPerPageDefault() : perPage;
  }

  public void setPerPage(int perPage) {
    this.perPage = perPage;
  }

  public String getEventParam() {
    return eventParam;
  }
   /**
   * Setting the event name to a param so that pagination can render the even correctly
   */
  @After(stages = LifecycleStage.EventHandling)
  public void rememberEvent() {
    eventParam = getContext().getEventName();
  }
}
