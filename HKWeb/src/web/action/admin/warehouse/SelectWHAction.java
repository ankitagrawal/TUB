package web.action.admin.warehouse;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.admin.AdminHomeAction;
import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.dao.user.UserDao;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.UserService;

@Component
public class SelectWHAction extends BaseAction {

  
  UserDao userDao;

  private UserService userService;

  private Warehouse setWarehouse;

  @DefaultHandler
  public Resolution pre() {
    //TODO #introducing gc as a hit n try solution for server performance
    System.gc();
    return new ForwardResolution("/pages/admin/selectWH.jsp");
  }

  @Secure(hasAnyRoles = {RoleConstants.WH_MANAGER_L1, RoleConstants.CATEGORY_MANAGER, RoleConstants.ADMIN}, authActionBean = AdminPermissionAction.class)
  public Resolution bindUserWithWarehouse() {
    User loggedOnUser = getPrincipalUser();
    Set<Warehouse> warehouses = new HashSet<Warehouse>();
    if (setWarehouse != null) {
      warehouses.add(setWarehouse);
    }
    loggedOnUser.setWarehouses(warehouses);
    userDao.save(loggedOnUser);

    return new RedirectResolution(AdminHomeAction.class);
  }

  public Resolution getUserWarehouse() {

    setWarehouse = userService.getWarehouseForLoggedInUser();
    return new ForwardResolution("/pages/admin/selectWH.jsp");
  }

  public Warehouse getSetWarehouse() {
    return setWarehouse;
  }

  public void setSetWarehouse(Warehouse setWarehouse) {
    this.setWarehouse = setWarehouse;
  }

  
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
  
}