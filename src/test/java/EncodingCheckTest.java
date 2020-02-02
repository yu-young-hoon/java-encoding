import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EncodingCheckTest {
    @Test
    public void encoding01() {

        String text = "안녕하세요";
        Charset[] charSet = {
                StandardCharsets.UTF_8,
                StandardCharsets.ISO_8859_1,
                Charset.forName("euc-kr"),
                StandardCharsets.UTF_16
        };

        for (Charset charsetLeft : charSet) {
            for (Charset charsetRight : charSet) {
                System.out.println(charsetLeft + " >> " + charsetRight + " : " + new String(text.getBytes(charsetLeft), charsetRight));
            }
        }
    }
}
