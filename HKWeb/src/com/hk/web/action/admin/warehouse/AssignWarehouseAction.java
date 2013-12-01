package com.hk.web.action.admin.warehouse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.Breadcrumb;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.CHANGE_USER_ROLES}, authActionBean = AdminPermissionAction.class)
@Breadcrumb(level = 3, name = "Warehouse for user: {user.login}", context = HealthkartConstants.BreadcrumbContext.admin)
@Component
public class AssignWarehouseAction extends BaseAction{

  private static Logger auditLogger = LoggerFactory.getLogger("adminAuditLogger");

  @Autowired
   UserDao userDao;
  @Autowired 
  WarehouseDaoImpl warehouseDao;

  @Validate(required = true)
  private User user;

  private List<Warehouse> allWarehouses;

  private Warehouse userWarehouse;

  @DefaultHandler
  public Resolution pre() {
    allWarehouses = warehouseDao.getAllWarehouses();
    userWarehouse = user.getSelectedWarehouse();
    return new ForwardResolution("/pages/admin/assignWarehouse.jsp");
  }

  public Resolution change() {
    Set<Warehouse> warehouses = new HashSet<Warehouse>();
    warehouses.add(userWarehouse);
    user.setWarehouses(warehouses);
    getUserService().save(user);
    auditLogger.info("Admin ["+getPrincipal().getId()+"] - "+getPrincipal().getEmail()+" has updated roles for user ["+user.getId()+"] - "+user.getLogin());
    addRedirectAlertMessage(new LocalizableMessage("/AssignWarehouse.action.warehouse.assigned.successfully"));
    return new RedirectResolution(AssignWarehouseAction.class).addParameter("user", user.getId());
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Warehouse getUserWarehouse() {
    return userWarehouse;
  }

  public void setUserWarehouse(Warehouse userWarehouse) {
    this.userWarehouse = userWarehouse;
  }

  public List<Warehouse> getAllWarehouses() {
    return allWarehouses;
  }

  public void setAllWarehouses(List<Warehouse> allWarehouses) {
    this.allWarehouses = allWarehouses;
  }
}