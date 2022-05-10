package cf.thdisstudio.audioDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainPanel extends JPanel {
    PlayListPanel playListPanel = new PlayListPanel();
    PlayerPanel playerPanel = new PlayerPanel();
    public MainPanel(){
        setLayout(new BorderLayout());
        add(playListPanel, BorderLayout.WEST);
        add(playerPanel, BorderLayout.CENTER);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        try{
//            playListPanel.repaint();
//            playerPanel.repaint();
            playListPanel.setPreferredSize(new Dimension((int) (getWidth()*0.2), getHeight()));
            playerPanel.setPreferredSize(new Dimension((int) (getWidth()*0.8), getHeight()));
        }catch (NullPointerException e){}
    }
}
