package db.seed.master;


import com.google.inject.Inject;
import mhc.common.constants.EnumEmailType;
import mhc.domain.EmailType;
import mhc.service.dao.EmailTypeDao;
import java.util.List;
import java.util.ArrayList;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class EmailTypeSeedData {

  @Inject
  EmailTypeDao emailTypeDao;

  public void insert(java.lang.String name, java.lang.Long id) {
    EmailType emailType = new EmailType();
      emailType.setName(name);
      emailType.setId(id);
    emailTypeDao.save(emailType);
  }

  public void invokeInsert(){
    List<Long> pkList = new ArrayList<Long>();

    for (EnumEmailType enumEmailType : EnumEmailType.values()) {

      if (pkList.contains(enumEmailType.getId())) throw new RuntimeException("Duplicate key "+enumEmailType.getId());
      else pkList.add(enumEmailType.getId());

      insert(enumEmailType.getName(), enumEmailType.getId());
    }
  }

}
