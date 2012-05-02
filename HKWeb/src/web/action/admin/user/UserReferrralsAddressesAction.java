package web.action.admin.user;

import java.util.List;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.user.UserDao;
import com.hk.domain.user.User;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Sep 11, 2011
 * Time: 11:13:06 AM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.MODERATE_REWARD_POINTS}, authActionBean = AdminPermissionAction.class)
@Component
public class UserReferrralsAddressesAction extends BaseAction {
   UserDao userDao;
  List<User> referredUsers;
  User user;

  public Resolution pre() {
    referredUsers = userDao.referredUserList(user);
    return new ForwardResolution("/pages/admin/userReferralAddresses.jsp");
  }

  public Resolution referralList() {
    referredUsers = userDao.referredUserList(user);
    return new ForwardResolution("/pages/admin/userReferralsList.jsp");
  }

  public List<User> getReferredUsers() {
    return referredUsers;
  }

  public void setReferredUsers(List<User> referredUsers) {
    this.referredUsers = referredUsers;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
