package web.action.admin.user;

import java.util.List;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.dao.user.UserCartDao;
import com.hk.dao.user.UserProductHistoryDao;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCart;
import com.hk.domain.user.UserProductHistory;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 1/2/12
 * Time: 1:07 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CustomerLifeCycleAction extends BaseAction {

  private User user;
  private List<UserProductHistory> userProductsList;
  private List<UserCart> userCartList;

   UserProductHistoryDao userProductHistoryDao;
   UserCartDao userCartDao;

  public Resolution pre() {
    userProductsList = userProductHistoryDao.findByUser(user);
    userCartList = userCartDao.findByUser(user);
    return new ForwardResolution("/pages/admin/customerLifeCycle.jsp");
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public List<UserProductHistory> getUserProductsList() {
    return userProductsList;
  }

  public void setUserProductsList(List<UserProductHistory> userProductsList) {
    this.userProductsList = userProductsList;
  }

  public List<UserCart> getUserCartList() {
    return userCartList;
  }

  public void setUserCartList(List<UserCart> userCartList) {
    this.userCartList = userCartList;
  }
}
