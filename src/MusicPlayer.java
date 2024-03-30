package src;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.*;

/**
 * A utility class for playing music in the game.
 */
public class MusicPlayer {
    /** The path to the music file. */
    public static final String MUSIC_PATH = "Music/music.wav";
    
    /** The audio clip used for playing the music. */
    private static Clip clip;

    /**
     * Plays the music.
     */
    public static void playMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(MUSIC_PATH));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loops the music
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                // Handle file not found exception
                System.err.println("File not found: " + MUSIC_PATH);
            } else {
                // Handle other exceptions
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the music.
     */
    public static void stopMusic() {
        clip.stop();
    }

    /**
     * Plays or stops the music based on its current state.
     */
    public static void playOrStopMusic() {
        if (clip != null && clip.isRunning()) {
            stopMusic();
            System.out.println("Music stopped.");
        } else {
            playMusic();
            System.out.println("Music started.");
        }
    }
}