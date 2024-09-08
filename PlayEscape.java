//*******************************************************************
//
//   File: PlayEscape.java          Assignment No.: FINAL PROJECT
//
//   Author: asl87
//
//   Class: PlayEscape
// 
//   Dependencies: Simon.java, WordleHard.java, WordleEasy.java, Title.java
//   Endings.java, LandMines.java, Maze.java
//   --------------------
//   This is the driver file for the ESCAPE! game. It connects all the
//   games together and provides transitions between games as well as
//   access to the title and ending screens.
//
//*******************************************************************

import java.util.Random;
import java.util.Arrays;
import java.awt.*;
import java.util.Scanner;
import java.awt.image.BufferedImage;


public class PlayEscape {
    static final int WIDTH = 901;
    static final int HEIGHT = 601;
    public static Room current;
    public static Room[] Rooms;
    public static int roomTracker;
    public static Room[] RoomsShuffled;
    public static boolean eventHandlerOpened;
    public static DrawingPanel panel = new DrawingPanel(WIDTH, HEIGHT);
    public static Graphics2D g = panel.getGraphics();
    public static boolean hardMode;
    public static boolean lost;

    // initiates new game
    public static void main(String[] args) {
        Title title = new Title();
        Simon simon = new Simon();
        Maze maze = new Maze();
        LandMines landmines = new LandMines();
        openEventHandler();
        current = title;
        current.start();
        if (hardMode) {
        System.out.println("Hard mode selected!");
        WordleHard wordleHard = new WordleHard();
        Rooms = new Room[] {wordleHard, simon, maze, landmines};
        simon.setDifficulty(8);
        maze.setDifficulty(true);
        }
        else if (!hardMode) {
            WordleEasy wordleEasy = new WordleEasy();
            System.out.println("Easy mode selected!");
            Rooms = new Room[] {wordleEasy, simon, maze, landmines};
            simon.setDifficulty(4);
            maze.setDifficulty(false);
        }
        RoomsShuffled = ShuffleRooms(Rooms, true);
        roomTracker = 0;
        current = RoomsShuffled[0];
        transitionAnimation(current);
        current.start();
    }

    // opens event handler for playability
    public static void openEventHandler() {
        panel.onKeyDown((ch) -> current.input(ch));
        panel.onMouseClick((x, y) -> current.drawWall(x, y));
        panel.onDrag((x, y) -> current.drawWall(x, y));
    }

    // shuffles room array into a random order
    public static Room[] ShuffleRooms(Room[] Rooms, boolean doShuffle) {
		if (!doShuffle) {
            return Rooms;
        }
		Random rand = new Random();
		
		for (int i = 0; i < Rooms.length; i++) {
			int randomIndexToSwap = rand.nextInt(Rooms.length);
			Room temp = Rooms[randomIndexToSwap];
			Rooms[randomIndexToSwap] = Rooms[i];
			Rooms[i] = temp;
		}
		System.out.println(Arrays.toString(Rooms));
        return Rooms;
    }
    
    // changes to next room
    public static void changeRooms() {
        roomTracker++;
        if (lost) { // if lose, transition to lose screen
            Endings end = new Endings();
            current = end;
            Endings.playLoseScreen();
        }
        if (roomTracker == RoomsShuffled.length && !lost) { // if we won, transition to win screen
            Endings end = new Endings();
            transitionAnimation(end);
            current = end;
            Endings.playWinScreen();
        }
        else if (!lost) { // otherwise, transition to next room
        System.out.println("New Room!");
        current=RoomsShuffled[roomTracker];
        transitionAnimation(current);
        current.start();
        }
    }   

    // boolean method to set difficulty
    public static void setHardMode(boolean hard) {
       if (hard) {
        hardMode = true;
       }
       if (!hard) {
        hardMode = false;
       }
    }

    // boolean method to set status of losing
    public static void setLost() {
        lost = true;
     }

    // method which provides unique transition animations depending on the next room
    public static void transitionAnimation(Room nextRoom) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 100));
        PlayEscape.g.drawString("Room " + (roomTracker+1), WIDTH/2 - 180, HEIGHT/2 - 150);
        Endings.initSFX("snd_impact.wav");
        panel.sleep(1500);
        g.setFont(new Font("Times New Roman", Font.BOLD, 150));
        if (nextRoom.returnName().equals("simon")) {
            Endings.initSFX("snd_levelup.wav");
            PlayEscape.g.drawString("Simon Says", 100, HEIGHT/2 + 50);
        }
        if (nextRoom.returnName().equals("maze")) {
            Endings.initSFX("snd_hero.wav");
            PlayEscape.g.drawString("Maze", 275, HEIGHT/2 + 50);
        }
        if (nextRoom.returnName().equals("wordle")) {
            Endings.initSFX("snd_coin_ch1.wav");
            PlayEscape.g.drawString("Wordle", 215, HEIGHT/2 + 50);
        }
        if (nextRoom.returnName().equals("landmines")) {
            Endings.initSFX("snd_joker_laugh0_ch1.wav");
            PlayEscape.g.drawString("Land Mines", 75, HEIGHT/2 + 50);
        }
        if (nextRoom.returnName().equals("victory")) {
            Endings.initSFX("snd_drumroll_ch1.wav");
            PlayEscape.g.drawString("????", 300, HEIGHT/2 + 50);
        }
        panel.sleep(1500);
    }

}