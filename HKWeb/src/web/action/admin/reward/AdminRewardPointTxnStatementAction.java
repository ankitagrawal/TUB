package web.action.admin.reward;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.manager.ReferrerProgramManager;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_USERS}, authActionBean = AdminPermissionAction.class)
@Component
public class AdminRewardPointTxnStatementAction extends BaseAction {

   ReferrerProgramManager referrerProgramManager;

  @Validate(required = true)
  private User user;

  private Double redeemablePoint;

  public Resolution pre() {
    redeemablePoint = referrerProgramManager.getTotalRedeemablePoints(user);
    return new ForwardResolution("/pages/admin/rewardPointTxnStatement.jsp");
  }

  public Double getRedeemablePoint() {
    return redeemablePoint;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
