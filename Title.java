//*******************************************************************
//
//   File: FileName.java          Assignment No.: 6
//
//   Author: <netid>
//
//   Class: ClassName
// 
//   Time spent on this problem: 
//   --------------------
//      Please give a description about your design. 
//
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

public class Title extends Room {
    public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static Graphics2D osg = offscreen.createGraphics();
    public static boolean stillPlaying;
    private static Clip backgroundMusic;

    public Title() {
        xCoor = HEIGHT / 2;
        yCoor = ROW / 2;
        backgroundColor = Color.WHITE;
    }

    public static void main(String[] args) {
        Title title = new Title();
        title.start();
    }

    public void start() {
        stillPlaying = true;
        canPlay = true;
        osg.setColor(Color.WHITE);
        osg.fillRect(0, 0, WIDTH, HEIGHT);
        animateTitleScreen();
        while(stillPlaying) {
            System.out.print("");
        }
        stopBackgroundMusic();
        initAudio("snd_levelup.wav", false);
    }

    public void animateTitleScreen() {
        playScreen(ohNoScreen());
        initAudio("mus_intronoise.wav", false);
        PlayEscape.panel.sleep(1500);
        clearScreen();
        playScreen(teachersUpset1());
        initAudio("mus_note1.wav", false);
        PlayEscape.panel.sleep(2000);
        
        playScreen(teachersUpset2());
        initAudio("mus_note2.wav", false);
        PlayEscape.panel.sleep(2000);
 
        playScreen(teachersUpset3());
        initAudio("mus_note3.wav", false);
        PlayEscape.panel.sleep(2000);
        
        playScreen(noOptions());
        initAudio("mus_sfx_cinematiccut.wav", false);
        PlayEscape.panel.sleep(2000);
        clearScreen();
        animateEscapeLogo();
        drawGrid('d');
        initAudio("mus_sfx_eyeflash.wav", false);
        PlayEscape.panel.sleep(500);
        initAudio("WHITESPACE.wav", true);
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 100));
        PlayEscape.g.drawString("ESCAPE!", 230, HEIGHT/2 - 200);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 50));
        PlayEscape.g.setColor(Color.GREEN);
        PlayEscape.g.drawString("Green for Easy Mode", 220, HEIGHT/2 - 150);
        PlayEscape.g.setColor(Color.RED);
        PlayEscape.g.drawString("Red for Hard Mode", 240, HEIGHT/2 - 100);
        avatar(xCoor, yCoor, 'd');
    }

    private void initAudio(String pathName, boolean doLoop) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(pathName));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            playBackgroundMusic();
            if (doLoop) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            System.err.println("Error initializing audio: " + e.getMessage());
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public BufferedImage ohNoScreen() {
        osg.setColor(Color.WHITE);
        osg.fillRect(0, 0, WIDTH, HEIGHT);
        osg.setColor(Color.RED);
        osg.setFont(new Font("Times New Roman", Font.BOLD, 200));
        osg.drawString("OH NO!", 80, HEIGHT / 2 + 30);
        return offscreen;
    }

    public BufferedImage teachersUpset1() {
        osg.setColor(Color.BLACK);
        osg.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        osg.drawString("Your professors are fed up!", 170, HEIGHT / 2 - 80 - 20);
        return offscreen;
    }

    public BufferedImage teachersUpset2() {
        osg.drawString("You've goofed off all semester.", 140, HEIGHT / 2 - 20);
        return offscreen;
    }

    public BufferedImage teachersUpset3() {
        osg.drawString("They've trapped you in a", 190, HEIGHT / 2 + 80 - 20);
        osg.setColor(Color.RED);
        osg.drawString("mysterious dungeon.", 240, HEIGHT / 2 + 160 - 20);
        return offscreen;
    }

    public BufferedImage noOptions() {
        osg.setColor(Color.WHITE);
        osg.fillRect(0, 0, WIDTH, HEIGHT);
        osg.setColor(Color.BLACK);
        osg.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        osg.drawString("You have no option left but to...", 140, HEIGHT / 2);
        return offscreen;
    }

    public void playScreen(BufferedImage image) {
        PlayEscape.g.drawImage(image, 0, 0, null);
    }

    public void clearScreen() {
        osg.setColor(Color.WHITE);
        osg.fillRect(0, 0, WIDTH, HEIGHT);
    }

    public void animateEscapeLogo() {
        initAudio("mus_sfx_sparkles.wav", false);
        double y0 = HEIGHT / 2;
        double y1;
        for (double t = 0; t < 2; t += FRAME_T / 1000.0) {
            // compute next position and previous
            y1 = y0 - 100*t;
            
            osg.setColor(Color.WHITE);
            osg.fillRect(0, 0, WIDTH, HEIGHT);
            writeEscapeLogo(y1);

            // change drawCar to draw offscreen with osg instead of g
            // drawCar(x1, y1, SIZE / 4);
            // copy buffered image to the screen and pause
            PlayEscape.panel.sleep(FRAME_T);
        }
        
    }

    public static void writeEscapeLogo(double y) {
        osg.setColor(Color.BLACK);
        osg.setFont(new Font("Times New Roman", Font.BOLD, 100));
        osg.drawString("ESCAPE!", 230, (int)y);
        PlayEscape.g.drawImage(offscreen, 0, 0, null);
    }

    public void drawGrid(char ch) {
        PlayEscape.g.setColor(Color.WHITE);
        PlayEscape.g.fillRect(0,  0, WIDTH, HEIGHT);
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW;  j++) {
                drawBackground(i, j, backgroundColor);
            }
        }
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 100));
        PlayEscape.g.drawString("ESCAPE!", 230, HEIGHT/2 - 200);
        // PlayEscape.g.setColor(Color.RED);
        drawBackground(3, 18, Color.RED);
        drawBackground(3, 17, Color.RED);
        drawBackground(3, 16, Color.RED);
        drawBackground(4, 17, Color.RED);
        drawBackground(2, 17, Color.RED);
        // PlayEscape.g.fillRect(3 * BOX_SIZE, 18 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(3 * BOX_SIZE, 17 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(3 * BOX_SIZE, 16 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(2 * BOX_SIZE, 17 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(4 * BOX_SIZE, 17 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.setColor(Color.GREEN);
        drawBackground(26, 18, Color.GREEN);
        drawBackground(26, 17, Color.GREEN);
        drawBackground(26, 16, Color.GREEN);
        drawBackground(25, 17, Color.GREEN);
        drawBackground(27, 17, Color.GREEN);
        // PlayEscape.g.fillRect(26 * BOX_SIZE, 18 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(26 * BOX_SIZE, 17 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(26 * BOX_SIZE, 16 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(25 * BOX_SIZE, 17 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        // PlayEscape.g.fillRect(27 * BOX_SIZE, 17 * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        avatar(xCoor, yCoor, ch);
    }

    public void input(char ch) {
        if (canPlay) {
        drawBackground(xCoor, yCoor, backgroundColor);
        if (ch >= 65 && ch <= 90) { // for caps
            ch = (char) ((int) ch + 32);
        }
        if (ch == 'w') {
            if (yCoor - 1 != -1) {
                yCoor = yCoor - 1;
            } else {StdAudio.play("bird.wav");}
        }
        else if (ch == 'a') {
            if (xCoor - 1 != -1) {
                xCoor = xCoor - 1;
            } else {StdAudio.play("bird.wav");}
        }
        else if (ch == 'd') {
            if (xCoor + 1 != COL) {
                xCoor = xCoor + 1;
            } else {StdAudio.play("bird.wav");}
        }
        else if (ch == 's') {
            if (yCoor + 1 != ROW) {
                yCoor = yCoor + 1;
            } else {StdAudio.play("bird.wav");}
        }
        else if (ch == 'e') {
            System.out.println("Nothing to see here.");
        }
        if (checkGreenTile(xCoor, yCoor)) {
            PlayEscape.setHardMode(false);
            stillPlaying = false;
        }
        if (checkRedTile(xCoor, yCoor)) {
            PlayEscape.setHardMode(true);
            stillPlaying = false;
        }
        avatar(xCoor, yCoor, ch);
    }
    }

    public boolean checkGreenTile(int x, int y) {
        if (xCoor == 25 && yCoor == 17) {
            return true;
        }
        if (xCoor == 26 && (yCoor == 17 || yCoor ==16 || yCoor == 18)) {
            return true;
        }
        if (xCoor == 27 && yCoor == 17) {
            return true;
        }
        else return false;
    }
    public boolean checkRedTile(int x, int y) {
        if (xCoor == 2 && yCoor == 17) {
            return true;
        }
        if (xCoor == 3 && (yCoor == 17 || yCoor ==16 || yCoor == 18)) {
            return true;
        }
        if (xCoor == 4 && yCoor == 17) {
            return true;
        }
        else return false;
    }

    // public void avatar(int x, int y) {
    //     if (canPlay) {
    //     int positionX = (int) (BOX_SIZE * (x + .25));
    //     int positionY = (int) (BOX_SIZE * (y + .25));
    //     PlayEscape.g.setColor(Color.BLACK);
    //     PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 4);
    //     PlayEscape.g.setColor(Color.RED);
    //     PlayEscape.g.fillRect(positionX, positionY + BOX_SIZE / 4, BOX_SIZE / 2, BOX_SIZE / 4);
    //     }
    // }

    public void clear(int x, int y) {
        int positionX = (int) (BOX_SIZE * (x + .25));
        int positionY = (int) (BOX_SIZE * (y + .25));
        PlayEscape.g.setColor(backgroundColor);
        if (checkRedTile(x, y)) {
            PlayEscape.g.setColor(Color.RED);
        }
        if (checkGreenTile(x, y)) {
            PlayEscape.g.setColor(Color.GREEN);
        }
        PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 2);
    }

}
