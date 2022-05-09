package cf.thdisstudio.audioDownloader;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

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
    public Player player;

    public void play(File file) {
        try {
            if(player != null)
                player.close();
            player = new Player(new FileInputStream(file));
        }catch (Exception e){}
        new Thread(() -> {
            try {
                player.play();
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
