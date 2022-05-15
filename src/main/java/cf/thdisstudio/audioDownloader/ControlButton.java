package cf.thdisstudio.audioDownloader;

import javazoom.jl.decoder.JavaLayerException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ControlButton extends JButton {
    BufferedImage play;
    BufferedImage pause;

    public ControlButton(){
        try {
            play = ImageIO.read(Main.class.getResourceAsStream("/img/Play_Button.png"));
            pause = ImageIO.read(Main.class.getResourceAsStream("/img/Pause_Button.png"));
            setVisible(true);
            setBackground(Color.WHITE);
            setFocusPainted(true);
            addMouseListener(new ClickEvent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage((Main.main.mainPanel.playerPanel.isPlaying() ? pause : play), 0, 0, getWidth(), getHeight(), null);
    }

    public class ClickEvent extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(Main.main.mainPanel.playerPanel.isPlaying())
                Main.playerManager.stop();
            else {
                try {
                    Main.playerManager.play();
                } catch (JavaLayerException ex) {
                    ex.printStackTrace();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}