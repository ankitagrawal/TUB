package com.hk.admin.pact.task;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface TaskService {
  static Logger Logger                  = LoggerFactory.getLogger(TaskService.class);

    public boolean execute(String data) ;
}