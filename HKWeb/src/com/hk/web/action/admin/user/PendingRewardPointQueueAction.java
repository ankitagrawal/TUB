package com.hk.web.action.admin.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.discount.EnumRewardPointStatus;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.domain.offer.rewardPoint.RewardPointStatus;
import com.hk.pact.dao.reward.RewardPointDao;
import com.hk.pact.service.order.RewardPointService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.MODERATE_REWARD_POINTS }, authActionBean = AdminPermissionAction.class)
@Component
public class PendingRewardPointQueueAction extends BasePaginatedAction {

    Page                       rewardPointPage;
    List<RewardPoint>          rewardPointList;
    @Autowired
    RewardPointDao             rewardPointDao;
    @Autowired
    private RewardPointService rewardPointService;

    private Integer            defaultPerPage = 30;
    private RewardPointMode    rewardPointMode;

    @SuppressWarnings("unchecked")
    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        List<RewardPointMode> rpmList = new ArrayList<RewardPointMode>();
        if (rewardPointMode != null) {
            rpmList.add(rewardPointMode);
        }
        List<RewardPointStatus> rpsList = new ArrayList<RewardPointStatus>();
        rpsList.add(rewardPointService.getRewardPointStatus(EnumRewardPointStatus.PENDING));

        rewardPointPage = rewardPointDao.searchRewardPoints(rpmList, rpsList, getPageNo(), getPerPage());
        if (rewardPointPage != null) {
            rewardPointList = rewardPointPage.getList();
        }
        return new ForwardResolution("/pages/admin/pendingRewardPointQueue.jsp");
    }

    public Resolution save() {
        rewardPointService.approveRewardPoints(rewardPointList, null);
        return new RedirectResolution(PendingRewardPointQueueAction.class).addParameter("rewardPointMode", rewardPointMode);
    }

    public List<RewardPoint> getRewardPointList() {
        return rewardPointList;
    }

    public void setRewardPointList(List<RewardPoint> rewardPointList) {
        this.rewardPointList = rewardPointList;
    }

    public RewardPointMode getRewardPointMode() {
        return rewardPointMode;
    }

    public void setRewardPointMode(RewardPointMode rewardPointMode) {
        this.rewardPointMode = rewardPointMode;
    }

    public int getPerPageDefault() {
        return defaultPerPage;
    }

    public int getPageCount() {
        return rewardPointPage == null ? 0 : rewardPointPage.getTotalPages();
    }

    public int getResultCount() {
        return rewardPointPage == null ? 0 : rewardPointPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        HashSet<String> params = new HashSet<String>();
        params.add("rewardPointMode");
        return params;
    }
}
