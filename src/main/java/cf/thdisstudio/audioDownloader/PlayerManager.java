package cf.thdisstudio.audioDownloader;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class PlayerManager {
    public HashMap<String, Video> videos = new HashMap<>();
    public Video video;
    public AdvancedPlayer player;
    public boolean isPlaying = false;
    public Thread playThread;
    public int lastFrame = 0;
    public int lastPosition = 0;

    public void play(Video video) {
        try {
            if(player != null)
                player.close();
            if(playThread != null)
                playThread.stop();
            player = new AdvancedPlayer(new FileInputStream(Main.downloadFolder+"/"+video.title+".mp3"));
            player.setPlayBackListener(new AudioEventHandler());
        }catch (Exception e){
            e.printStackTrace();
        }
        playThread = new Thread(() -> {
            try {
                player.play();
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        });
        playThread.start();
    }

    public void play() throws JavaLayerException, FileNotFoundException {
        if(player != null)
            player.close();
        if(playThread != null)
            playThread.stop();
        player = new AdvancedPlayer(new FileInputStream(Main.downloadFolder+"/"+video.title+".mp3"));
        player.setPlayBackListener(new AudioEventHandler());
        playThread = new Thread(() -> {
            try {
                System.out.println(lastPosition/(video.duration*1000f));
                player.play(lastPosition/100+50, (int) video.duration*1000);
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        });
        playThread.start();
    }

    public void stop(){
        player.stop();
    }
}
