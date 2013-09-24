package com.hk.web.action.admin.reward;

import java.util.Arrays;
import java.util.Date;

import com.hk.constants.discount.RewardPointConstants;
import com.hk.domain.order.Order;
import com.hk.pact.service.order.OrderService;
import com.hk.web.action.admin.crm.MasterResolutionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.user.User;
import com.hk.exception.InvalidRewardPointsException;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.service.order.RewardPointService;
import com.hk.web.action.admin.user.SearchUserAction;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * User: rahul Time: 4 May, 2010 11:11:47 AM
 */
@Secure(hasAnyPermissions = { PermissionConstants.AWARD_REWARD_POINTS }, authActionBean = AdminPermissionAction.class)
@Component
public class AddRewardPointAction extends BaseAction {
    private static Logger      logger = LoggerFactory.getLogger(AddRewardPointAction.class);

    @Autowired
    RewardPointDao             rewardPointDao;

    @Autowired
    private RewardPointService rewardPointService;

    @Autowired
    private OrderService orderService;

    @Validate(required = true, on = "add")
    private Double             value;

    @Validate(required = true, on = "add")
    private String             comment;

    @Validate(required = true)
    private User               user;

    @Validate(required = true, on = "add")
    private RewardPointMode    rewardPointMode;

    @Validate(required = true, on = "add")
    private Date               expiryDate;

    @Validate(required = true, on = "add")
    private Long orderId;

    private Boolean rewardFlag;

    @DefaultHandler
    public Resolution pre() {
//      return new ForwardResolution("/pages/admin/addRewardPoint.jsp");
      return new ForwardResolution(MasterResolutionAction.class);
    }

    public Resolution add() {
        rewardFlag = true;
        User referredUser = getUserService().getUserById(getPrincipal().getId());
        // referredUser stores the id of the user who added reward points
        // this logs the user who has added reward points
        boolean rewardPointsAdded = true;
        if(user.equals(referredUser)){
            addRedirectAlertMessage(new SimpleMessage("A user cannot give reward points to himself"));
            return new RedirectResolution(SearchUserAction.class, "search");
        }
        RewardPoint rewardPoint = new RewardPoint();
        try {
            Order order = orderService.find(orderId);
            if (value >= RewardPointConstants.MAX_REWARD_POINTS) {
                throw new InvalidRewardPointsException(value);
            }
            rewardPoint = rewardPointDao.addRewardPoints(user, referredUser, order, value, comment, EnumRewardPointStatus.APPROVED, rewardPointMode);
        } catch (InvalidRewardPointsException e) {
            logger.error("Reward point cannot be added", e);
            rewardPointsAdded = false;
        }
        if (rewardPointsAdded) {
            getRewardPointService().approveRewardPoints(Arrays.asList(rewardPoint), expiryDate);
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

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

  public Boolean getRewardFlag() {
    return rewardFlag;
  }

  public void setRewardFlag(Boolean rewardFlag) {
    this.rewardFlag = rewardFlag;
  }
}
