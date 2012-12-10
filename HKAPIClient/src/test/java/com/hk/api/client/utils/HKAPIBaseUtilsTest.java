package com.hk.api.client.utils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/5/12
 * Time: 9:54 AM
 */
public class HKAPIBaseUtilsTest {

    @Test
    public void md5HashTest(){
        String test="abc";
        String hkhash= HKAPIBaseUtils.md5Hash(test,"",1) ;
        String shirohash=new Md5Hash(test, "", 1).toBase64();
        Assert.assertEquals(hkhash, shirohash);
    }
}
