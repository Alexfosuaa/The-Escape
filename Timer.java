//*******************************************************************
//
//   File: Timer.java          Assignment No.: FINAL PROJECT
//
//   Author: sb3234
//
//   Class: Timer
// 
//   Dependency: Maze
//   --------------------
//      Timer for Maze: works through "animation" (doesn't actually
//      draw anything, though there is a method for a separate panel
//      timer.)
//
//*******************************************************************

import java.awt.*;
import java.awt.image.*;

public class Timer {
    // static final int WIDTH = 180;
    // static final int HEIGHT = 90;
    static final int FRAME_T = 250; // in ms

    // Setup DrawingPanel
    // public DrawingPanel panel = new DrawingPanel(WIDTH, HEIGHT);
    // public Graphics2D tg = panel.getGraphics();

    private BufferedImage offscreen;
    private Graphics2D osg;

    int countdown;
    int currentTime;
    int boxSize;
    int x;
    int y;

    boolean won;


    public Timer(int gridX, int gridY, int length) {
        boxSize = length;
        x = gridX * boxSize;
        y = gridY * boxSize;
        offscreen = new BufferedImage(boxSize, boxSize, BufferedImage.TYPE_INT_RGB);
        osg = offscreen.createGraphics();
        countdown = 30;
        currentTime = 30;
        won = false;
    }

    public Timer(int gridX, int gridY, int length, int custom) {
        boxSize = length;
        x = gridX * boxSize;
        y = gridY * boxSize;
        offscreen = new BufferedImage(boxSize, boxSize, BufferedImage.TYPE_INT_RGB);
        osg = offscreen.createGraphics();
        countdown = custom;
        currentTime = 30;
        won = false;
    }

    public Timer() {
        boxSize = 1;
        x = 0;
        y = 0;
        offscreen = new BufferedImage(boxSize, boxSize, BufferedImage.TYPE_INT_RGB);
        osg = offscreen.createGraphics();
        countdown = 30;
        currentTime = 30;
        won = false;
    }

    public Timer(int custom) {
        boxSize = 1;
        x = 0;
        y = 0;
        offscreen = new BufferedImage(boxSize, boxSize, BufferedImage.TYPE_INT_RGB);
        osg = offscreen.createGraphics();
        countdown = custom;
        currentTime = custom;
        won = false;
    }

    public void start(DrawingPanel panel, Graphics2D tg) { // drawn timer (not used)
        for (int i = countdown * 4 + 3; i > 0; i--) {
            System.out.println(i / 4 + "");
            // clear the screen
            osg.setColor(Color.WHITE);
		    osg.fillRect(x, y, boxSize, boxSize);
            // buffer the timer
		    drawTimer(i);
            // draw the timer
            tg.drawImage(offscreen, 0, 0, null);
            // pause
		    panel.sleep(FRAME_T);
        }
    }

    public void startMind(DrawingPanel panel, Graphics2D tg) { // "animated" timer
        while (!won) {
            currentTime--;
            System.out.println(currentTime + "");
            // freeze
	        PlayEscape.panel.sleep(FRAME_T * 4);
        }
    }
    

    private void drawTimer(int i) { // timer drawing (not used)
        currentTime = i;
        if (i % 2 == 0) {
            osg.setColor(Color.BLACK);
        } else {osg.setColor(Color.GRAY);}
        osg.fillRect(x, y, boxSize, boxSize);
        osg.setColor(Color.GREEN);
        osg.setFont(new Font("Impact", Font.BOLD, boxSize));
        osg.drawString(currentTime + "", x, y + boxSize);
        
    }

    public int currentTime() {
        return currentTime;
    }


    // aesthetic
}