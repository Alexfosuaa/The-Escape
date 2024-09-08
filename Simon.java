//*******************************************************************
//
//   File: Simon.java          Assignment No.: FINAL PROJECT
//
//   Author: asl87
//
//   Class: Simon
// 
//   --------------------
//   This is a basic simon says-style game. It flashes a number of
//   colors (must be 4 or 8) depending on the input difficulty. It 
//   varies the time of each flash depending on difficulty (1s for 4 flashes)
//   or (0.5s for 8 flashes). The player must maneuver to each tile in the
//   correct order and interact with them.
//*******************************************************************

import java.awt.*;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.Random;

public class Simon extends Room {
    public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static Graphics2D osg = offscreen.createGraphics();
    public static final int FRAME_T = 17; // in ms
    private String[][] simonGrid = new String[COL][ROW]; // col = 30, row = 20
    public String[] playerGuesses;
    public String[] currentPattern;
    public static int roundTracker;
    public static int numGuesses;
    public Color altRed = new Color(173, 39, 29);
    public static boolean stillPlaying;
    public static int difficulty;
    public static double difficultyTime;
    public static Clip music;
    public static Clip SFX;

    public static void main(String[] args) {
        Simon simon = new Simon();
        simon.start();
    }

    public void start() {
        initMusic("SHOWTIME.wav", true);
        numGuesses=0;
        xCoor = 0;
        yCoor = ROW/2;
        stillPlaying = true;
        canPlay = true;
        initializeSimon(); // initializes the board state
        drawSimon(); // draws the initial board state
        currentPattern = generateSimonPattern(difficulty); // generates a random pattern
        roundStartAnimation(roundTracker + 1, 3); 
        roundStart(currentPattern, difficulty); // draws starting animation based on that pattern
        avatar(xCoor, yCoor, 'd'); // initializes avatar in starting position
        while(stillPlaying) {
            System.out.print("");
        }
        stopBackgroundMusic(music);
        PlayEscape.changeRooms();
    }

    public void openEventHandler() {
        PlayEscape.panel.onKeyDown((ch) -> input(ch));
    }

    public void setDifficulty(int level) {
        difficulty = level;
        if (level==4) {
            difficultyTime = 1;
        }
        if (level==8) {
            difficultyTime = 0.5;
        }
    }

    // public void clear(int x, int y) {
    //     if (simonGrid[x][y].equals("BLANK")) {
    //         PlayEscape.g.setColor(backgroundColor);
    //     }
    //     if (simonGrid[x][y].equals("RED")) {
    //         PlayEscape.g.setColor(altRed);
    //     }
    //     if (simonGrid[x][y].equals("BLUE")) {
    //         PlayEscape.g.setColor(Color.BLUE);
    //     }
    //     if (simonGrid[x][y].equals("GREEN")) {
    //         PlayEscape.g.setColor(Color.GREEN);
    //     }
    //     if (simonGrid[x][y].equals("YELLOW")) {
    //         PlayEscape.g.setColor(Color.YELLOW);
    //     }
    //     int positionX = (int) (BOX_SIZE * (x + .25));
    //     int positionY = (int) (BOX_SIZE * (y + .25));
    //     PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 2);
    // }

    public void clear(int x, int y) {
        if (simonGrid[x][y].equals("BLANK")) {
            drawBackground(x, y, backgroundColor);
        }
        if (simonGrid[x][y].equals("RED")) {
            drawBackground(x, y, altRed);
        }
        if (simonGrid[x][y].equals("BLUE")) {
            drawBackground(x, y, Color.BLUE);
        }
        if (simonGrid[x][y].equals("GREEN")) {
            drawBackground(x, y, Color.GREEN);
        }
        if (simonGrid[x][y].equals("YELLOW")) {
            drawBackground(x, y, Color.YELLOW);
        }
        // int positionX = (int) (BOX_SIZE * (x + .25));
        // int positionY = (int) (BOX_SIZE * (y + .25));
        // PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 2);
    }

    public void initializeSimon() {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                simonGrid[i][j] = "BLANK";
            }
        }
        for (int i = 2; i < 13; i++) {
            for (int j = 2; j < 8; j++) {
                simonGrid[i][j] = "RED";
            }
        }
        for (int i = 2; i < 13; i++) {
            for (int j = 12; j < 18; j++) {
                simonGrid[i][j] = "BLUE";
            }
        }
        for (int i = 17; i < 28; i++) {
            for (int j = 2; j < 8; j++) {
                simonGrid[i][j] = "GREEN";
            }
        }
        for (int i = 17; i < 28; i++) {
            for (int j = 12; j < 18; j++) {
                simonGrid[i][j] = "YELLOW";
            }
        }
        osg.setColor(backgroundColor);
        osg.fillRect(0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                PlayEscape.g.setColor(Color.BLACK);
                PlayEscape.g.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
            }
        }
    }

    public void drawSimon() {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                // if (simonGrid[i][j].equals("BLANK")) {
                //     PlayEscape.g.setColor(backgroundColor);
                // }
                // if (simonGrid[i][j].equals("RED")) {
                //     PlayEscape.g.setColor(altRed);
                // }
                // if (simonGrid[i][j].equals("BLUE")) {
                //     PlayEscape.g.setColor(Color.BLUE);
                // }
                // if (simonGrid[i][j].equals("GREEN")) {
                //     PlayEscape.g.setColor(Color.GREEN);
                // }
                // if (simonGrid[i][j].equals("YELLOW")) {
                //     PlayEscape.g.setColor(Color.YELLOW);
                // }
                // PlayEscape.g.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                if (simonGrid[i][j].equals("BLANK")) {
                    drawBackground(i, j, backgroundColor);
                }
                if (simonGrid[i][j].equals("RED")) {
                    drawBackground(i, j, altRed);
                }
                if (simonGrid[i][j].equals("BLUE")) {
                    drawBackground(i, j, Color.BLUE);
                }
                if (simonGrid[i][j].equals("GREEN")) {
                    drawBackground(i, j, Color.GREEN);
                }
                if (simonGrid[i][j].equals("YELLOW")) {
                    drawBackground(i, j, Color.YELLOW);
                }
                PlayEscape.g.setColor(Color.BLACK);
                PlayEscape.g.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
            }
        }
    }

    public void animateFlash(String inputColor, double inputTime) {
        for (double t = 0; t < 0.5; t += FRAME_T / 1000.0) {
            PlayEscape.g.drawImage(simonDefault(), 0, 0, null);
            PlayEscape.panel.sleep(FRAME_T);
        }
        initSFX("snd_bell.wav");
        for (double t = 0; t < inputTime; t += FRAME_T / 1000.0) {
            PlayEscape.g.drawImage(flashColor(inputColor), 0, 0, null);
            PlayEscape.panel.sleep(FRAME_T);
        }
        PlayEscape.g.drawImage(simonDefault(), 0, 0, null);
    }

    public String[] generateSimonPattern(int instances) {
        Random rand = new Random();
        String[] simonPattern = new String[instances];
        for (int i = 0; i < instances; i++) {
            int simonChoice = rand.nextInt(4);
            if (simonChoice == 0) {
                simonPattern[i] = "RED";
                System.out.println("RED");
            }
            if (simonChoice == 1) {
                simonPattern[i] = "BLUE";
                System.out.println("BLUE");
            }
            if (simonChoice == 2) {
                simonPattern[i] = "GREEN";
                System.out.println("GREEN");
            }
            if (simonChoice == 3) {
                simonPattern[i] = "YELLOW";
                System.out.println("YELLOW");
            }
        }
        return simonPattern;
    }

    public void roundStart(String[] pattern, int difficulty) {
        canPlay = false;
        for (String color : pattern) {
            animateFlash(color, difficultyTime);
        }
        playerGuesses = new String[difficulty];
        canPlay = true;
    }

    public BufferedImage simonDefault() {
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW; j++) {
                // if (simonGrid[i][j].equals("BLANK")) {
                //     osg.setColor(backgroundColor);
                // }
                // if (simonGrid[i][j].equals("RED")) {
                //     osg.setColor(altRed);
                // }
                // if (simonGrid[i][j].equals("BLUE")) {
                //     osg.setColor(Color.BLUE);
                // }
                // if (simonGrid[i][j].equals("GREEN")) {
                //     osg.setColor(Color.GREEN);
                // }
                // if (simonGrid[i][j].equals("YELLOW")) {
                //     osg.setColor(Color.YELLOW);
                // }
                // osg.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                if (simonGrid[i][j].equals("BLANK")) {
                    drawBackground(i, j, backgroundColor, osg);
                }
                if (simonGrid[i][j].equals("RED")) {
                    drawBackground(i, j, altRed, osg);
                }
                if (simonGrid[i][j].equals("BLUE")) {
                    drawBackground(i, j, Color.BLUE, osg);
                }
                if (simonGrid[i][j].equals("GREEN")) {
                    drawBackground(i, j, Color.GREEN, osg);
                }
                if (simonGrid[i][j].equals("YELLOW")) {
                    drawBackground(i, j, Color.YELLOW, osg);
                }
                // osg.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                osg.setColor(Color.BLACK);
                osg.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
            }
        }
        return offscreen;
    }

    public BufferedImage flashColor(String inputColor) {
        if (inputColor.equals("RED")) {
            Color flashedRed = new Color(255, 156, 156);
            for (int i = 2; i < 13; i++) {
                for (int j = 2; j < 8; j++) {
                    osg.setColor(flashedRed);
                    osg.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                    osg.setColor(Color.BLACK);
                    osg.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
        }
        if (inputColor.equals("BLUE")) {
            Color flashedBlue = new Color(120, 212, 255);
            for (int i = 2; i < 13; i++) {
                for (int j = 12; j < 18; j++) {
                    osg.setColor(flashedBlue);
                    osg.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                    osg.setColor(Color.BLACK);
                    osg.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
        }
        if (inputColor.equals("GREEN")) {
            Color flashedGreen = new Color(167, 252, 169);
            for (int i = 17; i < 28; i++) {
                for (int j = 2; j < 8; j++) {
                    osg.setColor(flashedGreen);
                    osg.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                    osg.setColor(Color.BLACK);
                    osg.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
        }
        if (inputColor.equals("YELLOW")) {
            Color flashedYellow = new Color(237, 225, 161);
            for (int i = 17; i < 28; i++) {
                for (int j = 12; j < 18; j++) {
                    osg.setColor(flashedYellow);
                    osg.fillRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                    osg.setColor(Color.BLACK);
                    osg.drawRect(i * BOX_SIZE, j * BOX_SIZE, BOX_SIZE, BOX_SIZE);
                }
            }
        }
        return offscreen;
    }

    public void input(char ch) {
        if (canPlay) {
            clear(xCoor, yCoor);
            if (ch >= 65 && ch <= 90) { // for caps
                ch = (char) ((int) ch + 32);
            }
            if (ch == 'w') {
                if (yCoor - 1 != -1) {
                    yCoor = yCoor - 1;
                } else {
                    StdAudio.play("bird.wav");
                }
            } else if (ch == 'a') {
                if (xCoor - 1 != -1) {
                    xCoor = xCoor - 1;
                } else {
                    StdAudio.play("bird.wav");
                }
            } else if (ch == 'd') {
                if (xCoor + 1 != COL) {
                    xCoor = xCoor + 1;
                } else {
                    StdAudio.play("bird.wav");
                }
            } else if (ch == 's') {
                if (yCoor + 1 != ROW) {
                    yCoor = yCoor + 1;
                } else {
                    StdAudio.play("bird.wav");
                }
            } else if (ch == 'e') {
                if (simonGrid[xCoor][yCoor].equals("RED")) {
                    makeGuess("RED");
                }
                if (simonGrid[xCoor][yCoor].equals("BLUE")) {
                    makeGuess("BLUE");
                }
                if (simonGrid[xCoor][yCoor].equals("GREEN")) {
                    makeGuess("GREEN");
                }
                if (simonGrid[xCoor][yCoor].equals("YELLOW")) {
                    makeGuess("YELLOW");
                } 
                if (numGuesses == playerGuesses.length) {
                    boolean isPlayerCorrect = true;
                    for (int i = 0; i < playerGuesses.length; i++) {
                        if (!playerGuesses[i].equals(currentPattern[i])) {
                            isPlayerCorrect = false;
                        }
                    }
                    if (isPlayerCorrect) {
                        System.out.println("Simon cleared!");
                        roundTracker++;
                        initSFX("snd_dumbvictory.wav");
                        stillPlaying = false;
                    }
                    if (!isPlayerCorrect) {
                        System.out.println("Oops! You lost!");
                        roundTracker++;
                        initSFX("snd_wrongvictory.wav");
                        stillPlaying = false;
                        PlayEscape.setLost();
                    }
                }
            }
        }
        avatar(xCoor, yCoor, ch);
    }

    public void makeGuess(String guessColor) {
        initSFX("snd_bell.wav");
        if (numGuesses < playerGuesses.length) {
            playerGuesses[numGuesses] = guessColor;
            System.out.println("You have guessed: " + guessColor);
            numGuesses++;
        }
    }

    public void roundStartAnimation(int roundNum, int totalRounds) {
        for (double t = 0; t < 0.5; t += FRAME_T / 1000.0) {
            PlayEscape.g.drawImage(simonDefault(), 0, 0, null);
            PlayEscape.panel.sleep(FRAME_T);
        }
        initSFX("snd_noise.wav");
        for (double t = 0; t < 2.0; t += FRAME_T / 1000.0) {
            Color textColor = new Color(237, 236, 230);
            PlayEscape.g.setColor(textColor);
            PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 80));
            PlayEscape.g.drawString("Welcome to Simon Says!", 40, HEIGHT / 2);
            PlayEscape.panel.sleep(FRAME_T);
        }
        PlayEscape.g.drawImage(simonDefault(), 0, 0, null);
        initSFX("snd_noise.wav");
        for (double t = 0; t < 2.0; t += FRAME_T / 1000.0) {
            PlayEscape.g.drawString("Get Ready!", 270, HEIGHT / 2);
            PlayEscape.panel.sleep(FRAME_T);
        }
        PlayEscape.g.drawImage(simonDefault(), 0, 0, null);
    }

    public void avatar(int x, int y) {
        if (canPlay) {
        int positionX = (int) (BOX_SIZE * (x + .25));
        int positionY = (int) (BOX_SIZE * (y + .25));
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 4);
        PlayEscape.g.setColor(Color.RED);
        PlayEscape.g.fillRect(positionX, positionY + BOX_SIZE / 4, BOX_SIZE / 2, BOX_SIZE / 4);
        }
    }

    private void initMusic(String pathName, boolean doLoop) {
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

    public void playClip(Clip clip) {
        if (clip!= null && !clip.isRunning()) {
            clip.start();
        }
    }
    
    public void stopBackgroundMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public String returnName() {
        return "simon";
    }
}
