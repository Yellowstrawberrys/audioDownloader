package cf.thdisstudio.audioDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class Main extends JFrame implements Runnable {

    public static File tmp = new File(System.getProperty("java.io.tmpdir")+"/audioDownloader");
    public static File downloadFolder = new File(System.getProperty("user.home")+"/audioDownloader/");
    public static Main main;
    public static PlayerManager playerManager = new PlayerManager();

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        System.out.println("Starting YST Audio Downloader...");
        if(!tmp.exists())
            tmp.mkdirs();
        EventQueue.invokeAndWait(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
            }
            main = new Main();
        });
    }

    MainPanel mainPanel = new MainPanel();

    public Main(){
        super("Audio Downloader");
        setSize(960, 540);
        Thread th = new Thread(this);
        th.start();
        addWindowListener(new WindowListener());
        add(mainPanel);
        Dimension d = new Dimension();
        d.setSize(384, 216);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.playListPanel.resized();
                if(playerManager.video != null)
                    mainPanel.playerPanel.createBackground();
            }
        });
        setMinimumSize(d);
        setVisible(true);
        pack();
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
