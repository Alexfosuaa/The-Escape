//*******************************************************************
//
//   File: Endings.java          Assignment No.: FINAL PROJECT
//
//   Author: asl87
//
//   Class: Endings
// 
//   --------------------
//   This file contains the ending animations for victory and losing.
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

public class Endings extends Room {
    public static Clip music;
    public static Clip SFX;
    public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static Graphics2D osg = offscreen.createGraphics();
    public static boolean animationFinished;

    public static void main(String[] args) {
    }

    // plays win screen
    public static void playWinScreen() {
        animationFinished = false;
        PlayEscape.g.setColor(Color.WHITE);
        PlayEscape.g.fillRect(0, 0, WIDTH, HEIGHT);
        try {
            BufferedImage sun = ImageIO.read(new File("pixelsun.png"));
            PlayEscape.g.drawImage(sun, 280, -70, null);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        congratsScreen();
        PlayEscape.panel.sleep(100);
        initMusic("FALLENDOWN.wav", true);
        PlayEscape.panel.sleep(1400);
        clearBottom();
        goodEndingExposition();
        clearBottom();
        finalScreen();
        animationFinished = true;
    }

    // plays lose screen
    public static void playLoseScreen() {
        animationFinished = false;
        initMusic("DETERMINATION.wav", true);
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.fillRect(0, 0, WIDTH, HEIGHT);
        badEndingExposition();
    }

    // basic music playing method
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

    // initiates SFX
    public static void initSFX(String pathName) {
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

    // clears part of screen that does not contain sun jpg
    public static void clearBottom() {
        PlayEscape.g.setColor(Color.WHITE);
        PlayEscape.g.fillRect(0, HEIGHT / 3, WIDTH, 2 * HEIGHT / 3);
    }

    // screen for win animation
    public static void congratsScreen() {
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 150));
        PlayEscape.g.drawString("CONGRATS!", 0, HEIGHT / 2 + 40);
        initSFX("snd_impact.wav");
    }

    public static void playScreen(BufferedImage image) {
        PlayEscape.g.drawImage(image, 0, 0, null);
    }

    // exposition animation for good ending
    public static void goodEndingExposition() {
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.PLAIN, 50));
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("You've escaped your professors.", 140, HEIGHT / 2 - 60);
        PlayEscape.panel.sleep(1500);
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("You emerge onto Old Campus.", 160, HEIGHT / 2);
        PlayEscape.panel.sleep(1500);
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("It's a beautiful day!", 250, HEIGHT / 2 + 80);
        PlayEscape.panel.sleep(1500);
        PlayEscape.g.setColor(Color.RED);
        stopBackgroundMusic(music);
        initSFX("snd_wrongvictory.wav");
        PlayEscape.g.drawString("Now just for finals...", 240, HEIGHT / 2 + 160);
        PlayEscape.panel.sleep(2000);
        playClip(music);
        PlayEscape.panel.sleep(500);
    }

    // exposition animation for bad ending
    public static void badEndingExposition() {
        PlayEscape.g.setColor(Color.WHITE);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.PLAIN, 40));
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("Your professors got the best of you...", 150, HEIGHT / 2 - 60);
        PlayEscape.panel.sleep(1500);
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("You're condemned to work during commencement.", 40, HEIGHT / 2);
        PlayEscape.panel.sleep(1500);
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("Better not slack off next semester...", 180, HEIGHT / 2 + 80);
        PlayEscape.panel.sleep(1500);
        initSFX("snd_noise.wav");
        PlayEscape.g.drawString("Press E to quit and retry.", 250, HEIGHT / 2 + 160);
        PlayEscape.panel.sleep(2000);
        animationFinished = true;
    }

    // last screen for good ending
    public static void finalScreen() {
        try {
            BufferedImage OC = ImageIO.read(new File("OC.png"));
            PlayEscape.g.drawImage(OC, 30, 220, null);
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        PlayEscape.g.setColor(Color.BLACK);
        PlayEscape.g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
        initSFX("snd_bell.wav");
        PlayEscape.g.drawString("Press E to quit", 270, 3 * HEIGHT / 4 + 80);
        PlayEscape.panel.sleep(1000);
    }

    // quit
    public void input(char ch) {
        if (animationFinished) {
            if (ch == 'e') {
                System.exit(0);
            } 
        }
    }

    public String returnName() {
        return "victory";
    }
}
