package cf.thdisstudio.audioDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static cf.thdisstudio.audioDownloader.Main.playerManager;

public class PlayListPanel extends JPanel {

    TextField url = new TextField();

    public PlayListPanel(){
        setBackground(Color.decode("#b4b4b4"));
        url.setBackground(Color.decode("#828282"));
        add(url);
        setLayout(null);
        url.addKeyListener(new KeyListener());
    }

    public void resized(){
        url.setPreferredSize(new Dimension(getHeight(), (int) (getHeight()*0.04)));
        url.setBounds(0, (int) (getHeight()*0.07)+5, getHeight(), (int) (getHeight()*0.04));
        int i = 0;
        for(Video video : playerManager.videos.values()) {
            int y = (int) (getHeight()*0.07)+5+(int) (getHeight()*0.04) + ((int) (getHeight() * 0.09) * i);
            video.setPreferredSize(new Dimension(getWidth(), (int) (getHeight()*0.09)));
            video.setSize(new Dimension(getWidth(), (int) (getHeight()*0.09)));
            video.setLocation(0, y);
            video.revalidate();
            video.repaint();
            i++;
        }
        revalidate();
        repaint();
    }

    public void onEnter(){
        System.out.println("Enter");
        try {
            if(!(url.getText().isBlank() && url.getText().isEmpty()))
                addVideo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addVideo() throws IOException {
        Video video = new Video(url.getText());
        url.setText("");
        if(!playerManager.videos.containsKey(video.id)) {
            playerManager.videos.put(video.id, video);
            add(video);
            resized();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Font f = FontGetter.customFont("font1", getWidth()*0.08f);
        g.setFont(f);
        g.drawString("PlayList", (int) (getWidth()*0.02), (int) (getHeight()*0.06));
    }

    class KeyListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getKeyCode() == 10){
                onEnter();
            }
        }
    }
}
