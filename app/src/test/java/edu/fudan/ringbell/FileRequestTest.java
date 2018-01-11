package edu.fudan.ringbell;

import org.junit.Test;

import java.io.File;

import edu.fudan.ringbell.http.FileRequest;
import edu.fudan.ringbell.http.UserRequest;

/**
 * Created by pc on 2018/1/11.
 */

public class FileRequestTest {
    @Test
    public void testUpload() {
        UserRequest.login("aaaaaa3949", "testnewpwd");
        int i = FileRequest.upload(new File("C:\\Users\\pc\\Pictures\\Camera Roll\\sylvanas.png"), "sylvanas");
    }
}
