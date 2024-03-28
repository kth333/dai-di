package src;

import java.io.File;

import javax.sound.sampled.*;

public class MusicPlayer{
    private Clip clip;

    public void playMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loops the music
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning())
            clip.stop();
    }
}