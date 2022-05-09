package cf.thdisstudio.audioDownloader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static cf.thdisstudio.audioDownloader.FontGetter.customFont;
import static cf.thdisstudio.audioDownloader.Tool.cropImage;
import static cf.thdisstudio.audioDownloader.Tool.getImgFromURL;

public class PlayerPanel extends Panel {

    Graphics2D gr2D;

    public PlayerPanel(){
        setBackground(Color.decode("#545454"));
        setIgnoreRepaint(true);
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        update(g);
    }
    @Override
    public void update(Graphics g) {
        System.out.println("Paint");
        if(Main.playerManager.video != null) {
            System.out.println("FUCKING PAINT");
            gr2D = (Graphics2D) g;

            try {
                BufferedImage thumbnail = ImageIO.read(getImgFromURL("http://i3.ytimg.com/vi/%s/maxresdefault.jpg".formatted(Main.playerManager.video.id)));

                int radius = 11;
                int size = radius * 2 + 1;
                float weight = 1.0f / (size * size);
                float[] data = new float[size * size];

                Arrays.fill(data, weight);

                System.out.println(thumbnail != null);
                Kernel kernel = new Kernel(size, size, data);
                ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);

                BufferedImage background = op.filter(thumbnail, null);

                Graphics2D bacGr2D = background.createGraphics();
                bacGr2D.setColor(new Color(0, 0, 0, 0.4f));
                bacGr2D.fill(new Rectangle(10, 10, background.getWidth() - 10, background.getHeight() - 10));
                bacGr2D.dispose();

                gr2D.drawImage(cropImage(background, background.getWidth()-20, background.getHeight()-20, 10, 10), 0, 0, getWidth(), getHeight(), null);
                gr2D.drawImage(cropImage(thumbnail, thumbnail.getHeight(), thumbnail.getHeight(), thumbnail.getWidth() / 4, 0), (int) (getWidth()*0.05f), (int) (getHeight()*0.25f), (int) (getHeight()*0.35f), (int) (getHeight()*0.35f), null);

                gr2D.setColor(Color.WHITE);
                gr2D.setFont(customFont("font1", getHeight()*0.1f));
                gr2D.drawString(Main.playerManager.video.title, (int) (getWidth()*0.3f), (int) (getHeight()*0.48f));

                gr2D.setFont(customFont("font1", getHeight()*0.03f));

                long d = Main.playerManager.video.duration;
                gr2D.drawString(String.format("%02d:%02d:%02d",
                        TimeUnit.SECONDS.toHours(d) -
                                TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(d)), // The change is in this line
                        TimeUnit.SECONDS.toMinutes(d) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(d)), // The change is in this line
                        TimeUnit.SECONDS.toSeconds(d) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(d))), getWidth()-100, (int) (getHeight()*0.75f));

                gr2D.setColor(Color.LIGHT_GRAY);
                gr2D.setFont(customFont("font1", getHeight()*0.05f));
                gr2D.drawString(Main.playerManager.video.uploader, (int) (getWidth()*0.3f), (int) (getHeight()*0.55f));

                //Draw ProgressBar
                gr2D.setStroke(new BasicStroke((int) (getHeight()*0.03f)));
                gr2D.setColor(new Color(0, 0, 0, 0.4f));
                gr2D.drawLine(50, (int) (getHeight()*0.7f), getWidth()-50, (int) (getHeight()*0.7f));
                if(Main.playerManager.video != null && !Main.playerManager.player.isComplete()) {
                    gr2D.setColor(Color.WHITE);
                    gr2D.drawLine(50, (int) (getHeight() * 0.7f), (int) ((getWidth() - 50) * ((Main.playerManager.player.getPosition()/1000) / Main.playerManager.video.duration)), (int) (getHeight() * 0.7f));
                }
                gr2D.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
