package com.hk.pact.dao.user;

import com.akube.framework.dao.Page;
import com.hk.dto.user.B2BUserFilterDto;
import com.hk.pact.dao.BaseDao;

public interface B2BUserDao extends BaseDao {


  public Page search(B2BUserFilterDto userFilterDto, int pageNo, int perPage);

}
