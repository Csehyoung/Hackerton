package and.bfop.kftc.com.useorgsampleapprenewal;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import and.bfop.kftc.com.useorgsampleapprenewal.util.StringUtil;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test01(){

        String s = "한국 모바일 인증 어쩌구 789456 으헤헤헤헤";
        String ret = s.replaceAll("(.*)(\\d{6})(.*)", "$2");
        System.out.println("["+ret+"]");
    }

    @Test
    public void test02() throws UnsupportedEncodingException {

        String s = "%B0%A1%20%C0%AF%C8%BF%C7%CF%C1%F6%20%BE%CA%C0%BA%20%B0%E6%BF%EC";
        String d = URLDecoder.decode(StringUtil.defaultString(s), "EUC-KR");
        System.out.println(d);
    }




}