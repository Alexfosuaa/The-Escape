//*******************************************************************
//
//   File: LandMines.java         Assignment No.: FINAL PROJECT
//
//   Author: bbb32
//
//   Class: LandMines
// 
//   --------------------
//      In this game, LandMines, the user first sees a map of the grid 
//  containgin three keypieces and 15 landmines planted. The umap is displayed for five seconds 
//  and then the user is supposed to collect the three keypieces and exit the room. The
//  user is given three lives. Eac step on a landmine costs one life. So
//  the game ends when the user steps on landmines thrice or exits the room.
//      
//
//*******************************************************************

import java.awt.Color;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.*;

public class LandMines extends Room {
    private static String[][] cell;
    private int lives;
    private int keyPieces;
    private int xCod;
    private int yCod;
    private HashMap<Character, int[]> translation = new HashMap<>();
    private int[][] values = { { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 0 } };
    public static boolean stillPlaying;
    public static Clip music;
    public static Clip SFX;
    public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static Graphics2D osg = offscreen.createGraphics();
    public static boolean internalLoseCheck;

    public LandMines() {
        xCoor = 0;
        yCoor = ROW / 2;
        cell = new String[ROW][COL];
        lives = 3;
        keyPieces = 0;
        translation.put('w', values[0]);
        translation.put('a', values[1]);
        translation.put('s', values[2]);
        translation.put('d', values[3]);

        // set all cells clear
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                cell[i][j] = "clear";
            }
        }
        cell[ROW / 2][0] = "start_position";
        cell[ROW / 2][COL - 1] = "exit";
    }

    public void start() {
        xCoor = 0;
        yCoor = ROW / 2;
        stillPlaying = true;
        drawGrid();
        internalLoseCheck = false;
        int i = 0;
        while (i < 3) {
            xCod = (int) (Math.random() * COL);
            yCod = (int) (Math.random() * ROW);
            if ((cell[yCod][xCod].equals("clear")) && ((xCod != 0) || (yCod != ROW / 2))) {
                cell[yCod][xCod] = "key";
                showBox(xCod, yCod, Color.GREEN);
                i++;
            }
        }

        i = 0;
        while (i < 25) {
            xCod = (int) (Math.random() * COL);
            yCod = (int) (Math.random() * ROW);
            if ((cell[yCod][xCod].equals("clear")) && ((xCod != 0) || (yCod != ROW / 2))) {
                cell[yCod][xCod] = "landMine";
                showBox(xCod, yCod, Color.RED);
                i++;
            }
        }

        PlayEscape.panel.sleep(5000); // pause screen for 5 seconds
        initMusic("TENSION.wav", true);
        canPlay = true;
        drawGrid();

        showBox(COL - 1, ROW / 2, Color.WHITE);
        while (stillPlaying) {
            System.out.print("");
        }
        canPlay = false;
        if (internalLoseCheck) {
            stopBackgroundMusic(music);
            initSFX("alert.wav");
            PlayEscape.panel.sleep(1000);
            showKeysAndBombs();
            initSFX("snd_joker_laugh0_ch1.wav");
            PlayEscape.panel.sleep(2000);
            initSFX("snd_joker_byebye_ch1.wav");
            PlayEscape.panel.sleep(1000);
        }
        stopBackgroundMusic(music);
        PlayEscape.changeRooms();
    }

    public void input(char ch) {
        if ((!((xCoor + (translation.get(ch))[0] == COL - 1) && (yCoor + (translation.get(ch))[1] == ROW / 2)))
                || keyPieces == 3) {
            int previousX = xCoor;
            int previousY = yCoor;
            String previousValue = cell[previousY][previousX];
            super.input(ch);
            if (cell[yCoor][xCoor].equals("landMine")) {
                explode();
            }
            if (cell[yCoor][xCoor].equals("key")) {
                keyCollect();
            }
            if (cell[yCoor][xCoor].equals("exit")) {
                stillPlaying = false;
            }
            if (previousValue.equals("xKey")) {
             
                }
            if (previousValue.equals("xLandMine")) {
                showBox(previousX, previousY, Color.RED);
            }
    }
    }

    public void explode() {
        lives--;
        showBox(xCoor, yCoor, Color.RED);
        playAudio("snd_explosion_firework.wav");
        cell[yCoor][xCoor] = "xLandMine";
        if (lives == 0) { // lose
            stillPlaying = false;
            internalLoseCheck = true;
            PlayEscape.setLost();
        }
    }

    public void keyCollect() {
        keyPieces++;
        showBox(xCoor, yCoor, Color.GREEN);
        cell[yCoor][xCoor] = "xKey";
        playAudio("snd_levelup.wav");
    }

    public void showBox(int xCod, int yCod, Color color) {
        PlayEscape.g.setColor(color);
        PlayEscape.g.fillRect(xCod * BOX_SIZE, yCod * BOX_SIZE, BOX_SIZE, BOX_SIZE);
    }

    public void reset() {
        showBox(xCoor, yCoor, Color.GRAY);
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.drawRect(xCoor * BOX_SIZE, yCoor * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        avatar(xCoor, yCoor, 'd');
    }

    public void playAudio(String path) {
        try {
            // Load the audio file
            File audioFile = new File(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a Clip instance
            Clip clip = AudioSystem.getClip();

            // Open the audio stream
            clip.open(audioStream);

            // Play the audio
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Handle any exceptions, such as file not found or unsupported audio format
        }
    }

    private static void initMusic(String pathName, boolean doLoop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(pathName));
            music = AudioSystem.getClip();
            music.open(audioInputStream);
            playClip(music);
            if (doLoop) {
                music.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            System.err.println("Error initializing audio: " + e.getMessage());
        }
    }

    private void initSFX(String pathName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(pathName));
            SFX = AudioSystem.getClip();
            SFX.open(audioInputStream);
            playClip(SFX);
        } catch (Exception e) {
            System.err.println("Error initializing audio: " + e.getMessage());
        }
    }

    public static void playClip(Clip clip) {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    public static void stopBackgroundMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setDifficulty(int numLives) {
        lives = numLives;
    }

    public String returnName() {
        return "landmines";
    }

    public static void showKeysAndBombs() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (cell[i][j].equals("key") || cell[i][j].equals("xKey") ) {
                    PlayEscape.g.setColor(Color.GREEN);
                    PlayEscape.g.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
                if (cell[i][j].equals("landMine") || cell[i][j].equals("xLandMine")) {
                    PlayEscape.g.setColor(Color.RED);
                    PlayEscape.g.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
        }
    }
}
