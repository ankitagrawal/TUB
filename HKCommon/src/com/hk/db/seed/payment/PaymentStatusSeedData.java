package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumPaymentStatus;
import mhc.domain.PaymentStatus;
import mhc.service.dao.payment.PaymentStatusDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class PaymentStatusSeedData {

  @Inject
  PaymentStatusDao paymentStatusDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    PaymentStatus paymentStatus = new PaymentStatus();
      paymentStatus.setName(name);
      paymentStatus.setId(id);
    paymentStatusDao.save(paymentStatus);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumPaymentStatus enumPaymentStatus : EnumPaymentStatus.values()) {

      if (pkList.contains(enumPaymentStatus.getId())) throw new RuntimeException("Duplicate key "+enumPaymentStatus.getId());
      else pkList.add(enumPaymentStatus.getId());

      insert(enumPaymentStatus.getName(), enumPaymentStatus.getId());
    }
  }

}
