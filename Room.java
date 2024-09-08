//*******************************************************************
//
//   File: Room.java          Assignment No.: FINAL PROJECT
//
//   Author: sb3234
//
//   Class: Room
// 
//   --------------------
//   This is the parent object for all Rooms contained in the game.
//   Alone, it has little playability or functionality.
//
//*******************************************************************
import java.awt.*;
import java.util.Scanner;
import java.awt.image.BufferedImage;

public class Room {
    static final int WIDTH = 901;
    static final int HEIGHT = 601;
    final int FRAME_T = 17;
    // public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    // public static Graphics2D osg = offscreen.createGraphics();

    static final int BOX_SIZE = 30;
    static final int COL = WIDTH / BOX_SIZE;
    static final int ROW = HEIGHT / BOX_SIZE;
    public boolean canPlay;

    // private boolean defaultA; for character customization, subject to change
    static int xCoor, yCoor; // character position
    Color backgroundColor;

    /* 
    public Image winScreen() {
            osPlayEscape.g.setColor(Color.WHITE);
            osPlayEscape.g.fillRect(0, 0, WIDTH, HEIGHT);
            osPlayEscape.g.setColor(Color.GREEN);
            osPlayEscape.g.setFont(new Font("Impact", Font.BOLD, 55)); 
            osPlayEscape.g.drawString("You Win!", 310, HEIGHT/2);
            osPlayEscape.g.drawString("Congratulations!", 220, HEIGHT/2 + 45);  
            return offscreen;
    }

    public Image loseScreen() {
        osPlayEscape.g.setColor(Color.WHITE);
        osPlayEscape.g.fillRect(0, 0, WIDTH, HEIGHT);
        osPlayEscape.g.setColor(Color.RED);
        osPlayEscape.g.setFont(new Font("Impact", Font.BOLD, 55)); 
        osPlayEscape.g.drawString("You Lost...", 330, HEIGHT/2);
        osPlayEscape.g.drawString("Better Luck Next Time!", 220, HEIGHT/2 + 45);  
        return offscreen;
}*/
    
    // various constructors depending on need
    public Room() {
        xCoor = 0;
        yCoor = ROW / 2;
        backgroundColor = Color.GRAY;
    }

    public Room(int row, int col) {
        xCoor = col;
        yCoor = row;
        backgroundColor = Color.GRAY;
    }

    public Room(Color color) {
        xCoor = 0;
        yCoor = ROW / 2;
        backgroundColor = color;
    }

    public Room(int row, int col, Color color) {
        xCoor = col;
        yCoor = row;
        backgroundColor = color;
    }

    public static void main(String[] args) {
        Room main = new Room();
        main.start();
    }

    // draws basic grid
    public void drawGrid() {
        PlayEscape.g.setColor(backgroundColor);
        PlayEscape.g.fillRect(0,  0, WIDTH, HEIGHT);
        for (int i = 0; i < COL; i++) {
            for (int j = 0; j < ROW;  j++) {
                drawBackground(i, j, backgroundColor);
            }
        }
        avatar(xCoor, yCoor, 'd');
    }

    // draws background
    public void drawBackground(int x, int y, Color color) {
        int positionX = (x * BOX_SIZE) + 1;
        int positionY = (y * BOX_SIZE) + 1;
        PlayEscape.g.setColor(color);
        PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE - 1, BOX_SIZE - 1);
        PlayEscape.g.setColor(Color.DARK_GRAY);
        PlayEscape.g.drawRect(positionX, positionY, (int) (BOX_SIZE * .67), (int) (BOX_SIZE * .33));
        PlayEscape.g.drawRect(positionX + (int) (BOX_SIZE * .67), positionY, (int) (BOX_SIZE * .33), (int) (BOX_SIZE * .67));
        PlayEscape.g.drawRect(positionX, positionY + (int) (BOX_SIZE * .33), (int) (BOX_SIZE * .33), (int) (BOX_SIZE * .67));
        PlayEscape.g.drawRect(positionX + (int) (BOX_SIZE * .33), positionY + (int) (BOX_SIZE * .67), (int) (BOX_SIZE * .67), (int) (BOX_SIZE * .33));
    }

    public void drawBackground(int x, int y, Color color, Graphics2D bg) {
        int positionX = (x * BOX_SIZE) + 1;
        int positionY = (y * BOX_SIZE) + 1;
        bg.setColor(color);
        bg.fillRect(positionX, positionY, BOX_SIZE - 1, BOX_SIZE - 1);
        bg.setColor(Color.DARK_GRAY);
        bg.drawRect(positionX, positionY, (int) (BOX_SIZE * .67), (int) (BOX_SIZE * .33));
        bg.drawRect(positionX + (int) (BOX_SIZE * .67), positionY, (int) (BOX_SIZE * .33), (int) (BOX_SIZE * .67));
        bg.drawRect(positionX, positionY + (int) (BOX_SIZE * .33), (int) (BOX_SIZE * .33), (int) (BOX_SIZE * .67));
        bg.drawRect(positionX + (int) (BOX_SIZE * .33), positionY + (int) (BOX_SIZE * .67), (int) (BOX_SIZE * .67), (int) (BOX_SIZE * .33));
    }

    // draws avatar
    public void avatar(int x, int y, char ch) {
        if (canPlay) {
            int positionX = (int) (BOX_SIZE * (x + .25));
            int positionY = (int) (BOX_SIZE * (y + .25));
    
            if (ch == 'd') { // right facing
                PlayEscape.g.setColor(new Color(0, 3, 86)); // shoulders
                PlayEscape.g.fillOval(positionX, positionY - (int) (BOX_SIZE * .125), (int) (BOX_SIZE * .375), (int) (BOX_SIZE * .75));
                PlayEscape.g.setColor(new Color(105, 110, 181)); // hoodie
                PlayEscape.g.fillOval(positionX - (int) (BOX_SIZE * .125), positionY + (int) (BOX_SIZE * .0625), (int) (BOX_SIZE * .375), (int) (BOX_SIZE * .4375));
                PlayEscape.g.setColor(Color.BLACK); // head
                PlayEscape.g.fillOval(positionX, positionY, (int) (BOX_SIZE / 1.7), BOX_SIZE / 2);
            } else if (ch == 'a') { // left facing
                PlayEscape.g.setColor(new Color(0, 3, 86)); // shoulders
                PlayEscape.g.fillOval(positionX + (int) (BOX_SIZE / 3.4), positionY - (int) (BOX_SIZE * .125), (int) (BOX_SIZE * .375), (int) (BOX_SIZE * .75));
                PlayEscape.g.setColor(new Color(105, 110, 181)); // hoodie
                PlayEscape.g.fillOval(positionX + (int) (BOX_SIZE / 1.7 - BOX_SIZE * .25), positionY + (int) (BOX_SIZE * .0625), (int) (BOX_SIZE * .375), (int) (BOX_SIZE * .4375));
                PlayEscape.g.setColor(Color.BLACK); // head
                PlayEscape.g.fillOval(positionX, positionY, (int) (BOX_SIZE / 1.7), BOX_SIZE / 2);
            } else if (ch == 'w') { // up facing
                PlayEscape.g.setColor(new Color(0, 3, 86)); // shoulders
                PlayEscape.g.fillOval(positionX - (int) (BOX_SIZE * .125), positionY + (int) (BOX_SIZE * .3125), (int) (BOX_SIZE * .75), (int) (BOX_SIZE * .375));
                PlayEscape.g.setColor(new Color(105, 110, 181)); // hoodie
                PlayEscape.g.fillOval(positionX + (int) (BOX_SIZE * .0625), positionY + (int) (BOX_SIZE * .375), (int) (BOX_SIZE * .4375),  (int) (BOX_SIZE * .375));
                PlayEscape.g.setColor(Color.BLACK); // head
                PlayEscape.g.fillOval(positionX, positionY, BOX_SIZE / 2, (int) (BOX_SIZE / 1.7));
            } else if (ch == 's') { // down facing
                PlayEscape.g.setColor(new Color(0, 3, 86)); // shoulders
                PlayEscape.g.fillOval(positionX - (int) (BOX_SIZE * .125), positionY - (int) (BOX_SIZE * .125), (int) (BOX_SIZE * .75), (int) (BOX_SIZE * .375));
                PlayEscape.g.setColor(new Color(105, 110, 181)); // hoodie
                PlayEscape.g.fillOval(positionX + (int) (BOX_SIZE * .0625), positionY - (int) (BOX_SIZE * .1875), (int) (BOX_SIZE * .4375),  (int) (BOX_SIZE * .375));
                PlayEscape.g.setColor(Color.BLACK); // head
                PlayEscape.g.fillOval(positionX, positionY, BOX_SIZE / 2, (int) (BOX_SIZE / 1.7));
            } else { // box
                PlayEscape.g.setColor(Color.YELLOW);
                PlayEscape.g.fillOval(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 2);
                PlayEscape.g.setColor(Color.BLACK);
                PlayEscape.g.fillOval(positionX + (int) (BOX_SIZE * .125), positionY + (int) (BOX_SIZE * .125), BOX_SIZE / 8, BOX_SIZE / 6);
                PlayEscape.g.fillOval(positionX + (int) (BOX_SIZE * .25), positionY + (int) (BOX_SIZE * .125), BOX_SIZE / 8, BOX_SIZE / 6);
            }
        }
    }

    // clears screen to default color
    public void clear(int x, int y, Color color) {
        drawBackground(x, y, color);
    }

    // takes in input to move avatar
    public void input(char ch) {
        if (canPlay) {
            clear(xCoor, yCoor, backgroundColor);
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
            avatar(xCoor, yCoor, ch);
        }
    }

    // unusued win and lose methods
    public void win() {
        System.out.println("Room cleared!");
    }

    public void lose() {
        canPlay = false;
        System.out.println("Room failed. Better luck next time!");
        PlayEscape.changeRooms();
    }

    public void start() {
        canPlay = true;
        drawGrid();
    }

    public void terminate() {
        System.exit(0);
    }

    
    public String returnName() {
        return "base";
    }

    // dummy method to avoid mouse input carrying over from Maze.java
    public void drawWall(int x, int y) {

    }
}