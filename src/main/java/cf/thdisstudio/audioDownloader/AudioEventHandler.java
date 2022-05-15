package cf.thdisstudio.audioDownloader;

import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class AudioEventHandler extends PlaybackListener {
    @Override
    public void playbackStarted(PlaybackEvent evt) {
        Main.playerManager.isPlaying = true;
        System.out.println("Started" + evt.getFrame());
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        Main.playerManager.isPlaying = false;
        System.out.println("Stopped" + evt.getFrame()/evt.getSource().getPosition());
        Main.playerManager.lastFrame += evt.getFrame();
        Main.playerManager.lastPosition += evt.getSource().getPosition();
    }
}
