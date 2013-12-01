package com.hk.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hk.domain.catalog.product.Product;
import com.hk.pact.dao.BaseDao;
import com.hk.service.ServiceLocatorFactory;

public class LoadData {

    @SuppressWarnings("unused")
    private ClassPathXmlApplicationContext appContext;

    private BaseDao                        baseDao;

    public LoadData() {
        this.appContext = new ClassPathXmlApplicationContext("classpath:spring/spring*.xml");
        this.baseDao = ServiceLocatorFactory.getService(BaseDao.class);
    }
    
    public void test(){
        Product pr = baseDao.get(Product.class, "NUT304");
        System.out.println(pr.getName());
    }
    
    public static void main(String[] args) {
        LoadData load = new LoadData();
        load.test();
    }

}
