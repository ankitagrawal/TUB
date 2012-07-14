package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.domain.courier.Awb;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 13, 2012
 * Time: 10:56:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class AwbServiceImpl implements AwbService {
    @Autowired
    AwbDao awbDao;
      public Awb find(Long id){
          return awbDao.get(Awb.class,id);
      }


}
