package game.upload;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageLoader {
    Map<String, Image> imgPool = new HashMap<>();
    {
        File assertFile = new File("asset");
        System.out.println(assertFile.getAbsolutePath());
        for (File f : assertFile.listFiles()) {
            try {
                Image img = ImageIO.read(f);
                imgPool.put(f.getName(), img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Image getImg(String name) {
        return imgPool.get(name);
    }
}
