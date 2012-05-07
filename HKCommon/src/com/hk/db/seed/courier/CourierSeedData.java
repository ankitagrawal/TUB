package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumCourier;
import mhc.domain.Courier;
import mhc.service.dao.CourierDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class CourierSeedData {

  @Inject
  CourierDao courierDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    Courier courier = new Courier();
      courier.setName(name);
      courier.setId(id);
    courierDao.save(courier);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumCourier enumCourier : EnumCourier.values()) {

      if (pkList.contains(enumCourier.getId())) throw new RuntimeException("Duplicate key "+enumCourier.getId());
      else pkList.add(enumCourier.getId());

      insert(enumCourier.getName(), enumCourier.getId());
    }
  }

}
