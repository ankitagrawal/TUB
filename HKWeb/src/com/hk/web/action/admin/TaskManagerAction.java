package com.hk.web.action.admin;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.task.TaskService;


/*@Secure(hasAnyPermissions = { PermissionConstants.RUN_ANT_BUILDS })*/
@Component
public class TaskManagerAction extends BaseAction {

  @Autowired
  TaskService             taskService;
/*
  @Autowired
  ProductCatalogServiceImpl productManager;
*/

  private String db_master_service;
  /*private static Logger logger                 = LoggerFactory.getLogger(TaskManagerAction.class);*/


  @DefaultHandler
  public Resolution pre(){
     return new ForwardResolution("/pages/admin/taskManager.jsp");
  }
  public Resolution runTask() {
        boolean status = taskService.execute(db_master_service);
        if(status){
          addRedirectAlertMessage(new SimpleMessage("DB Master ran successfully"));
        }
        else{
          addRedirectAlertMessage(new SimpleMessage("DB Master failed : please check logs for details"));
        }
        return new ForwardResolution("/pages/admin/taskManager.jsp");
  }

  public String getDb_master_service() {
    return db_master_service;
  }

  public void setDb_master_service(String db_master_service) {
    this.db_master_service = db_master_service;
  }
}