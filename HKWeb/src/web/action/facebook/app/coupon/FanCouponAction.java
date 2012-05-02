package web.action.facebook.app.coupon;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.google.code.facebookapi.FacebookException;
import com.hk.admin.manager.FanCouponManager;

@Component
public class FanCouponAction extends BaseAction {

   FanCouponManager fanCouponManager;

  public Resolution pre() throws FacebookException {
    return new ForwardResolution("/pages/facebook/app/coupon/fanCoupon.jsp");
  }

}
