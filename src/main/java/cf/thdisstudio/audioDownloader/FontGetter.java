package cf.thdisstudio.audioDownloader;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class FontGetter {
    public static Font customFont(String name, float size){
        try {
            InputStream myStream = null;
            try {
                myStream = FontGetter.class.getResourceAsStream("/font/"+name+".ttf");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont.deriveFont(size);
        }catch (Exception e){
            return null;
        }
    }
}
