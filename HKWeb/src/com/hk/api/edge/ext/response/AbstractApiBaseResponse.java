package com.hk.api.edge.ext.response;


import java.util.ArrayList;
import java.util.List;

public abstract class AbstractApiBaseResponse{

  protected boolean exception = false;
  protected List<String> msgs = new ArrayList<String>();

  public boolean isException() {
    return exception;
  }

  public void setException(boolean exception) {
    this.exception = exception;
  }

  public List<String> getMsgs() {
    return msgs;
  }

  public void setMsgs(List<String> msgs) {
    this.msgs = msgs;
  }
  
 
}
