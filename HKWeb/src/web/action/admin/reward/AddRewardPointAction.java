package web.action.admin.reward;

import java.util.Arrays;
import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.admin.user.SearchUserAction;
import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.dao.reward.RewardPointDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.manager.ReferrerProgramManager;

/**
 * User: rahul
 * Time: 4 May, 2010 11:11:47 AM
 */
@Secure(hasAnyPermissions = {PermissionConstants.AWARD_REWARD_POINTS}, authActionBean = AdminPermissionAction.class)
@Component
public class AddRewardPointAction extends BaseAction {
  private static Logger logger = LoggerFactory.getLogger(AddRewardPointAction.class);

  
  UserDao userDao;
  
  RewardPointDao rewardPointDao;
  
  ReferrerProgramManager referrerProgramManager;

  @Validate(required = true, on = "add")
  private Double value;

  @Validate(required = true, on = "add")
  private String comment;

  @Validate(required = true)
  private User user;

  @Validate(required = true, on = "add")
  private RewardPointMode rewardPointMode;

  @Validate(required = true, on = "add")
  private Date expiryDate;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/addRewardPoint.jsp");
  }

  public Resolution add() {
    User referredUser = getUserService().getUserById(getPrincipal().getId());
    // referredUser stores the id of the user who added reward points
    // this logs the user who has added reward points
    boolean rewardPointsAdded = true;
    RewardPoint rewardPoint=new RewardPoint();
    try {
      rewardPoint = rewardPointDao.addRewardPoints(user, referredUser, null, value, comment, EnumRewardPointStatus.APPROVED, rewardPointMode);
    } catch (InvalidRewardPointsException e) {
      logger.error("Reward point cannot be added" ,e);
      rewardPointsAdded = false;
    }
    if (rewardPointsAdded) {
      referrerProgramManager.approveRewardPoints(Arrays.asList(rewardPoint), expiryDate);
      addRedirectAlertMessage(new SimpleMessage("Reward Points added successfully"));
       return new RedirectResolution(SearchUserAction.class, "search");
    } else {
      addRedirectAlertMessage(new SimpleMessage("Reward Points cannot be more than " + RewardPointDao.MAX_REWARD_POINTS));
    return new RedirectResolution(SearchUserAction.class, "search");
    }
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public RewardPointMode getRewardPointMode() {
    return rewardPointMode;
  }

  public void setRewardPointMode(RewardPointMode rewardPointMode) {
    this.rewardPointMode = rewardPointMode;
  }
}
