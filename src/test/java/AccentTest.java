import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.text.Normalizer;

public class AccentTest {
    @Test
    public void accent01() {
        String nfc = "\u00c8";
        // 분해
        String nfcToNfd = Normalizer.normalize(nfc, Normalizer.Form.NFD);
        Assert.assertNotEquals(nfc, nfcToNfd);

        // 결합
        String nfdToNfc = Normalizer.normalize(nfcToNfd, Normalizer.Form.NFC);
        Assert.assertEquals(nfc, nfdToNfc);
    }

    @Test
    public void accent02() {
        String nfc = "\u00c8";

        String nfcToNfd = Normalizer.normalize(nfc, Normalizer.Form.NFD);
        String removedAccent = nfcToNfd.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        String removedAccent2 = StringUtils.stripAccents(nfcToNfd);
        Assert.assertEquals("E", removedAccent);
        Assert.assertEquals("E", removedAccent2);
    }
}
