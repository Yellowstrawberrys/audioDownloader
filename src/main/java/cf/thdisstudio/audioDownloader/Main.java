package cf.thdisstudio.audioDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Main extends JFrame implements Runnable {

    public static File tmp = new File(System.getProperty("java.io.tmpdir")+"/audioDownloader");
    public static File downloadFolder = new File(System.getProperty("user.home")+"/audioDownloader/");
    public static Main main = new Main();
    public static PlayerManager playerManager = new PlayerManager();

    public static void main(String[] args) {
        System.out.println("Starting YST Audio Downloader...");
        if(!tmp.exists())
            tmp.mkdirs();
    }

    MainPanel mainPanel = new MainPanel();

    public Main(){
        super("Fuck");
        setSize(960, 540);
        Thread th = new Thread(this);
        th.start();
        addWindowListener(new WindowListener());
        add(mainPanel);
        Dimension d = new Dimension();
        d.setSize(384, 216);
        setMinimumSize(d);
        setVisible(true);
    }

    public void updateUI(){
        validate();
        repaint();
    }

    @Override
    public void run() {
        while (true){
            mainPanel.setSize(getWidth(), getHeight());
            mainPanel.updateUI();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class WindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
