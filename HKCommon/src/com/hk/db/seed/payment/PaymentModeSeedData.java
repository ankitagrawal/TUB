package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumPaymentMode;
import mhc.domain.PaymentMode;
import mhc.service.dao.payment.PaymentModeDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class PaymentModeSeedData {

  @Inject
  PaymentModeDao paymentModeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    PaymentMode paymentMode = new PaymentMode();
      paymentMode.setName(name);
      paymentMode.setId(id);
    paymentModeDao.save(paymentMode);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumPaymentMode enumPaymentMode : EnumPaymentMode.values()) {

      if (pkList.contains(enumPaymentMode.getId())) throw new RuntimeException("Duplicate key "+enumPaymentMode.getId());
      else pkList.add(enumPaymentMode.getId());

      insert(enumPaymentMode.getName(), enumPaymentMode.getId());
    }
  }

}
