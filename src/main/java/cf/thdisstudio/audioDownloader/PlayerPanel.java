package cf.thdisstudio.audioDownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static cf.thdisstudio.audioDownloader.FontGetter.customFont;
import static cf.thdisstudio.audioDownloader.Tool.cropImage;
import static cf.thdisstudio.audioDownloader.Tool.getImgFromURL;

public class PlayerPanel extends JPanel {

    Graphics2D gr2D;
    ControlButton controlButton = new ControlButton();

    public PlayerPanel(){
        setBackground(Color.decode("#545454"));
        setIgnoreRepaint(true);
        setDoubleBuffered(true);
        setLayout(null);
        add(controlButton);
    }

    String id = "";
    BufferedImage background = null;
    BufferedImage thumbnail = null;
    BufferedImage layer1 = null;
//
    @Override
    public void paintComponent(Graphics g) {
        if(Main.playerManager.video != null) {
            if(!id.equals(Main.playerManager.video.id)){
                createBackground();
                id = Main.playerManager.video.id;
            }
            gr2D = (Graphics2D) g;

            try {
                gr2D.drawImage(cropImage(background, background.getWidth()-20, background.getHeight()-20, 10, 10), 0, 0, getWidth(), getHeight(), null);
                gr2D.drawImage(cropImage(thumbnail, thumbnail.getHeight(), thumbnail.getHeight(), thumbnail.getWidth() / 4, 0), (int) (getWidth()*0.05f), (int) (getHeight()*0.25f), (int) (getHeight()*0.35f), (int) (getHeight()*0.35f), null);
                gr2D.drawImage(layer1, 0, 0, getWidth(), getHeight(),null);
                if(Main.playerManager.video != null) {
                    gr2D.setStroke(new BasicStroke((int) (getHeight()*0.03f)));
                    gr2D.setColor(Color.WHITE);
                    gr2D.drawLine(50, (int) (getHeight()*0.7f), (int) ((getWidth() - 50) * ((float) (Main.playerManager.lastPosition+Main.playerManager.player.getPosition())) / (Main.playerManager.video.duration*1000f))+50, (int) (getHeight()*0.7f));
                    gr2D.setFont(customFont("font1", getHeight()*0.03f));
                    long d = Main.playerManager.lastPosition+Main.playerManager.player.getPosition();
                    gr2D.drawString(String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(d) -
                                    TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(d)), // The change is in this line
                            TimeUnit.MILLISECONDS.toMinutes(d) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(d)), // The change is in this line
                            TimeUnit.MILLISECONDS.toSeconds(d) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(d))), 10, (int) (getHeight()*0.75f));
                }
            } catch (Exception e) {
                if(e.getClass().getName().equals("java.lang.NullPointerException"))
                    createBackground();
                else e.printStackTrace();
            }
        }
    }

    public void createBackground(){
        id = Main.playerManager.video.id;
        int radius = 11;
        int size = radius * 2 + 1;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];
        try {
            thumbnail = ImageIO.read(getImgFromURL("http://i3.ytimg.com/vi/%s/maxresdefault.jpg".formatted(Main.playerManager.video.id)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Arrays.fill(data, weight);

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);

        background = op.filter(thumbnail, null);

        Graphics2D bacGr2D = background.createGraphics();
        bacGr2D.setColor(new Color(0, 0, 0, 0.4f));
        bacGr2D.fill(new Rectangle(10, 10, background.getWidth() - 10, background.getHeight() - 10));
        bacGr2D.dispose();

        Arrays.fill(data, weight);

        layer1 = new BufferedImage((int) (getWidth()*1.5), (int) (getHeight()*1.5), BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr2DL = layer1.createGraphics();
        gr2DL.setColor(Color.WHITE);
        gr2DL.setFont(customFont("font1", layer1.getHeight()*0.1f));
        gr2DL.drawString(Main.playerManager.video.title, (int) (layer1.getWidth()*0.35f), (int) (layer1.getHeight()*0.48f));

        gr2DL.setFont(customFont("font1", layer1.getHeight()*0.03f));

        long d = Main.playerManager.video.duration;
        gr2DL.drawString(String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(d) -
                        TimeUnit.DAYS.toHours(TimeUnit.SECONDS.toDays(d)), // The change is in this line
                TimeUnit.SECONDS.toMinutes(d) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(d)), // The change is in this line
                TimeUnit.SECONDS.toSeconds(d) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(d))), layer1.getWidth()-100, (int) (layer1.getHeight()*0.75f));

        gr2DL.setColor(Color.LIGHT_GRAY);
        gr2DL.setFont(customFont("font1", layer1.getHeight()*0.05f));
        gr2DL.drawString(Main.playerManager.video.uploader, (int) (layer1.getWidth()*0.34f), (int) (layer1.getHeight()*0.55f));

        //Draw ProgressBar
        gr2DL.setStroke(new BasicStroke((int) (layer1.getHeight()*0.03f)));
        gr2DL.setColor(new Color(0, 0, 0, 0.4f));
        gr2DL.drawLine(75, (int) (layer1.getHeight()*0.7f), layer1.getWidth()-75, (int) (layer1.getHeight()*0.7f));
        gr2DL.dispose();
    }

    public void resized(){
        if(Main.playerManager.video != null) {
            createBackground();
        }
        //controlButton.setPreferredSize(new Dimension((int) (getHeight()*0.2), (int) (getHeight()*0.3)));
        controlButton.setBounds((int) (getWidth()/2-(getHeight()*0.1)), (int) (getHeight()*0.8),(int) (getHeight()*0.1), (int) (getHeight()*0.1));
        controlButton.revalidate();
        controlButton.repaint();
    }

    public boolean isPlaying(){
        return Main.playerManager.isPlaying;
    }
}
