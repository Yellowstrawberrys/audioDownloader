package cf.thdisstudio.audioDownloader;

import cf.ystapi.util.JsonReader;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static cf.thdisstudio.audioDownloader.Main.downloadFolder;
import static cf.thdisstudio.audioDownloader.Tool.cropImage;
import static cf.thdisstudio.audioDownloader.Tool.getImgFromURL;

public class Video extends Button {

    public final JSONObject info;
    public final String title;
    public final String id;
    public final String videoURL;
    public final String uploader;
    public final String thumbnail;
    public final File target;
    public final long duration;

    public String status;
    Loader loader;

    public Video(String url) throws IOException {
        info = JsonReader.ReadFromUrl("http://ystapiytdl.herokuapp.com/ytApi/info?url="+ URLEncoder.encode(url, StandardCharsets.UTF_8)).getJSONObject("info");
        title = info.getString("title");
        videoURL = info.getString("url");
        uploader = info.getString("uploader");
        thumbnail = info.getString("thumbnail");
        id = info.getString("id");
        duration = info.getLong("duration");
        target = new File(downloadFolder+"/"+title+".mp3");
        System.out.println("Finished Loading Things");
        setBackground(Color.decode("#969696"));
        System.out.println(getWidth());
        setVisible(true);
        loader = new Loader();
        loader.start();
        addMouseListener(new MouseListener());
    }

    @Override
    public void repaint() {
        super.repaint();
    }

    @Override
    public void paint(Graphics gr) {
        super.paint(gr);
        Graphics2D g = (Graphics2D) gr;
        try {
            BufferedImage img = ImageIO.read(getImgFromURL("http://i3.ytimg.com/vi/%s/maxresdefault.jpg".formatted(id)));
            g.drawImage(cropImage(img, img.getHeight(), img.getHeight(), img.getWidth()/4, 0), (int) (getWidth()*0.03), (int) (getHeight()*0.15),
                    (int) (getHeight()*0.65f), (int) (getHeight()*0.65f),null);
        }catch (Exception e){
            e.printStackTrace();
        }
        g.setColor(Color.WHITE);
        g.setFont(FontGetter.customFont("font1", (getHeight())*0.21f));
        g.drawString(title, (int) (getWidth()*0.21), (int) (getHeight()*0.5));

        g.setColor(Color.decode("#7d7d7d"));
        g.setFont(FontGetter.customFont("font1", (getHeight())*0.14f));
        g.drawString(uploader, (int) (getWidth()*0.209), (int) (getHeight()*0.66));

        g.setStroke(new BasicStroke((int) (getHeight()*0.06)));
        g.setColor(Color.GREEN);
        if(isEnabled())
            g.drawLine(0, (int) (getHeight()*0.97), getWidth(), (int) (getHeight()*0.97));
        else{
            g.setColor(new Color(0,0,0, 0.5f));
            g.fillRect(0,0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(FontGetter.customFont("font1", (getHeight())*0.4f));
            g.drawString("DOWNLOADING", getWidth()*0.15f, getHeight()*0.6f);
        }
    }

    class Loader extends Thread{
        boolean disabled = true;
        public void run(){
            try {
                setEnabled(!disabled);
                if(!target.exists()) {
                    File f = new File(Main.tmp + "/" + title + ".mp4");
                    f.createNewFile();
                    URL url = new URL(videoURL);
                    status = "Downloading";
                    FileUtils.copyURLToFile(url, f);
                    convert();
                }else
                    disabled = false;
                setEnabled(!disabled);
                repaint();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void convert() throws Exception{
            status = "Converting";
            File source = new File(Main.tmp+"/"+title+".mp4");

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp3");
            attrs.setAudioAttributes(audio);


            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);

            //Delete
            source.delete();

            disabled = false;
            status = "Finished";
            System.out.println(status);
        }
    }

    class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Main.playerManager.play(target);
            Main.playerManager.video = Video.this;
            Main.main.mainPanel.playerPanel.repaint();
        }
    }
}
