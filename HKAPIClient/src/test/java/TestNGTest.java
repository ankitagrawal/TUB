
import com.hk.api.client.utils.HKAPIBaseUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/4/12
 * Time: 6:24 PM
 */

@Test
public class TestNGTest {
    public void test1(){
        String test="abc";
        Assert.assertEquals("abc",test);
    }
}
