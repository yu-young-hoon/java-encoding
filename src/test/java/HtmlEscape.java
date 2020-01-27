import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

public class HtmlEscape {
    @Test
    public void htmlTest01() throws IOException {
        ObjectMapper om = new ObjectMapper();
        om.getJsonFactory().setCharacterEscapes(new HTMLCharacterEscapes());

        MappingJackson2HttpMessageConverter htmlEscapingConverter = new MappingJackson2HttpMessageConverter();
        htmlEscapingConverter.setObjectMapper(om);
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        User value = new User();
        String s = om.writeValueAsString(value);
        System.out.println("s = " + s);
    }

    public class User {

        private String name = "<하하";

        public String getName() {
            return name;
        }
    }

    public class HTMLCharacterEscapes extends CharacterEscapes {
        private final int[] asciiEscapes;

        private final CharSequenceTranslator translator;

        public HTMLCharacterEscapes() {
            // 1. XSS 방지 처리할 특수 문자 지정
            asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
            asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes['&'] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
            asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;

            // 2. XSS 방지 처리 특수 문자 인코딩 값 지정
            translator = new AggregateTranslator(
                    new LookupTranslator(EntityArrays.BASIC_ESCAPE()),  // <, >, &, " 는 여기에 포함됨
                    new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()),
                    new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE()),
                    new LookupTranslator(
                            new String[][]{
                                    {"(", "&#40;"},
                                    {")", "&#41;"},
                                    {"#", "&#35;"},
                                    {"\'", "&#39;"}
                            }
                    )
            );
        }

        @Override
        public int[] getEscapeCodesForAscii() {
            return asciiEscapes;
        }

        @Override
        public SerializableString getEscapeSequence(int ch) {
            return new SerializedString(translator.translate(Character.toString((char) ch)));
        }
    }
}
