import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiTest {

    @Test
    public void test01() {
        final String em = "\ud83d\ude00";
        System.out.println("em = " + em);
    }

    @Test
    public void test02() {
        byte[] a = {(byte) 0xF0,(byte)0x9F,(byte)0x98,(byte)0x80};
        System.out.println("em = " + new String(a, StandardCharsets.UTF_8));
    }

    @Test
    public void test03() {
        final String em = "\ud83d\ude00";
        Matcher matcher = EMOJI_PATTERN.matcher(em);
        String cleanString = matcher.replaceAll("");
        System.out.println("em = " + cleanString);
    }

    private static Pattern EMOJI_PATTERN = Pattern.compile(
            "[\ud800\udc00-\udbff\udfff]",
            Pattern.UNICODE_CASE | Pattern.CANON_EQ | Pattern.CASE_INSENSITIVE);
    @Test
    public void test04() {
        char[] a = {0xF0, 0x9F, 0x98, 0x80};
        final String em = String.valueOf(a);
        Matcher matcher = EMOJI_PATTERN.matcher(em);
        String cleanString = matcher.replaceAll("");
        System.out.println("em = " + cleanString);
    }

    @Test
    public void test05() {
        try{
            byte[] a = {(byte) 0xF0,(byte)0x9F,(byte)0x98,(byte)0x80};
            final String em = new String(a);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false&serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8","root","");
            Statement stmt=con.createStatement();
            stmt.executeUpdate("insert into yh_encoding.new_table (utf8a,utf16a) values (\""+em+"\", \""+em+"\");");
            con.close();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void test06() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306","root","");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select hex(`utf8a`), hex(utf16a) from yh_encoding.new_table;\n");
            while(rs.next())
                System.out.println(rs.getString(1)+"  "+rs.getString(2));
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
}
