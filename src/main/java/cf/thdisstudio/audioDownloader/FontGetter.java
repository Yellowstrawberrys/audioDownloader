package cf.thdisstudio.audioDownloader;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class FontGetter {

    static HashMap<String, Font> fontCache = new HashMap<>();

    public static Font customFont(String name, float size){
        try {
            if(!fontCache.containsKey(name)) {
                InputStream myStream = null;
                try {
                    myStream = FontGetter.class.getResourceAsStream("/font/" + name + ".ttf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fontCache.put(name, Font.createFont(Font.TRUETYPE_FONT, myStream));
            }
            return fontCache.get(name).deriveFont(size);
        }catch (Exception e){
            return null;
        }
    }
}
