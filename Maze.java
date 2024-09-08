//*******************************************************************
//
//   File: Maze.java          Assignment No.: FINAL PROJECT
//
//   Author: sb3234
//
//   Class: Maze
// 
//   --------------------
//      The gqame plays in 2 stages. First the user draws on the 
//      grid. After a minimum number of tiles are drawn, the player 
//      can submit their work. The program then chooses 2 win 
//      condition tiles out of the (non-drawn) ones left. The player
//      must now walk over both win tiles and reach the end before 
//      time runs out, navigating the maze they drew.
//
//*******************************************************************

import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;

public class Maze extends Room {

    private static boolean[][] wall; // wall grid
    private int numWalls; // wall counter
    int MIN_NUM_WALLS; // to be drawn before player can submit

    private static boolean setupDone;

    private static boolean[][] winCon; // win condition grid
    private int haveWon;
    private int winConX1; // coordinates of first win condition
    private int winConY1;
    // for timer implementation
    private Timer timer;
    private int time;
    private boolean timerStarted;

    private boolean stillPlaying;
    private Random rand;

    public Maze() {
        super();
        wall = new boolean[ROW][COL];
        backgroundColor = new Color(214, 169, 4);
        setupDone = false;
        rand = new Random();
        winCon = new boolean[ROW][COL];
        haveWon = 0;
        winConX1 = -1; // nonsense initializer
        winConY1 = -1;
        timerStarted = false;
    }
    
    // other constructors
    public Maze(Color color) {
        super(color);
        wall = new boolean[ROW][COL];
        winCon = new boolean[ROW][COL];
        setupDone = false;
        rand = new Random();
        haveWon = 0;
        winConX1 = -1;
        winConY1 = -1;
        timerStarted = false;
        if (PlayEscape.hardMode) {
            MIN_NUM_WALLS = 150;
            time = 10;
        } else {
            MIN_NUM_WALLS = 100;
            time = 16;
        }
    }

    public Maze(int i) {
        super();
        wall = new boolean[ROW][COL];
        winCon = new boolean[ROW][COL];
        backgroundColor = Color.YELLOW;
        setupDone = false;
        rand = new Random();
        haveWon = 0;
        winConX1 = -1;
        winConY1 = -1;
        timerStarted = false;
        if (PlayEscape.hardMode) {
            MIN_NUM_WALLS = 150;
            time = 10;
        } else {
            MIN_NUM_WALLS = 100;
            time = 16;
        }
    }

    public static void main(String[] args) { // for testing purposes
        Maze maze = new Maze();
        maze.start();
    }

    public void setDifficulty(boolean hardMode) { // called by PlayEscape
        if (hardMode) {
            MIN_NUM_WALLS = 150;
            time = 10;
        } else {
            MIN_NUM_WALLS = 100;
            time = 16;
        }
    }

    public void animation() { // starting instruction to player
        drawGrid('d');
        for (double t = 0; t < 2.0; t += FRAME_T / 1000.0) {
            Color textColor = new Color(35, 94, 29);
            PlayEscape.g.setColor(textColor);
            PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 90));
            PlayEscape.g.drawString("DRAW!", 300, HEIGHT / 4);
            PlayEscape.panel.sleep(FRAME_T);
        }
    }

    public void start() {
        setupDone = false;
        stillPlaying = true;
        initializeGrid();
        animation();
        drawGrid('d');
        System.out.println("Draw carefully! Press Enter/Return to submit you drawing. Press R to restart.");

        PlayEscape.panel.onMouseClick((x, y) -> drawWall(x, y));
        PlayEscape.panel.onDrag((x, y) -> drawWall(x, y));
        // PlayEscape.panel.onKeyDown( (ch) -> input(ch)); called by PlayEscape
        while (!timerStarted){
            System.out.print("");
        }
        System.out.println("timer started"); // called when drawing is submitted
        timer.startMind(PlayEscape.panel, PlayEscape.g);
        while (stillPlaying) {
            System.out.print("");
        }
        PlayEscape.changeRooms();
    }

    private void initializeGrid() {
        // put the avatar at the start
        xCoor = 0;
        yCoor = ROW / 2;
        // clear the grid
        for (int i = 0; i < wall.length; i++) {
            for (int k = 0; k < wall[i].length; k++) {
                wall[i][k] = false;
                winCon[i][k] = false;
            }
        }
        numWalls = 0;
        System.out.println(numWalls);
    }

    public void drawGrid(char ch) {
        for (int i = 0; i < wall.length; i++) {
            for (int k = 0; k < wall[i].length;  k++) {
                if (wall[i][k]) {
                    drawBackground(k, i, Color.BLACK);
                } else if (winCon[i][k]) {
                    drawBackground(k, i, Color.BLUE);
                } else if (i == ROW / 2 && (k == 0 || k == COL - 1)) { // start and end tiles
                    drawBackground(k, i, Color.RED);
                } else {drawBackground(k, i, backgroundColor);}
                PlayEscape.g.setColor(Color.BLACK);
                PlayEscape.g.drawRect(k * BOX_SIZE, i * BOX_SIZE, BOX_SIZE, BOX_SIZE);
            }
        }
        avatar(xCoor, yCoor, ch);  
    }

    public void avatar(int x, int y, char ch) {
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
            PlayEscape.g.setColor(Color.BLACK);
            PlayEscape.g.fillRect(positionX, positionY, BOX_SIZE / 2, BOX_SIZE / 4);
            PlayEscape.g.setColor(Color.RED);
            PlayEscape.g.fillRect(positionX, positionY + BOX_SIZE / 4, BOX_SIZE / 2, BOX_SIZE / 4);
        }
    }

    public void clear(int x, int y) { // clears previous tile where avatar was
        if (wall[y][x]) {
            drawBackground(x, y, Color.BLACK);
        } else if (winCon[y][x]) {
            drawBackground(x, y, Color.BLUE);
        } else {drawBackground(x, y, backgroundColor);}
    }


    public void drawWall(int x, int y) { // drawing maze method
        if (!setupDone) {
        int col = x / BOX_SIZE;
        int row = y / BOX_SIZE;
        
        if (row != ROW / 2 || (col != COL - 1 && col != 0)) {
            if (!wall[row][col] && !hooked(row, col)) {
                wall[row][col] = true;
                numWalls++;
                if (numWalls == MIN_NUM_WALLS) {
                    StdAudio2.playInBackground("120tile.wav");
                    System.out.println("Mininum number of walls reached!");
                } else {StdAudio2.playInBackground("tile_placed.wav");}
                System.out.println(numWalls);
            } else if (wall[row][col] && surrounded(row, col)) {
                wall[Math.floorMod(row + 1, ROW)][col] = false;
                numWalls--;
                StdAudio2.playInBackground("tile_placed.wav");
                System.out.println(numWalls);
            }
        }
        
        drawGrid('d');
       
    }
    }

    private boolean surrounded(int row, int col) { // if the chosen tile is surrounded (which is likely impossible at the point, but eh)
        return wall[row][Math.floorMod(col - 1, COL)] && wall[row][Math.floorMod(col + 1, COL)] && wall[Math.floorMod(row - 1, ROW)][col] && wall[Math.floorMod(row + 1, ROW)][col];
    }

    private boolean hooked(int row, int col) { // if the chosen tile has two or more adjacent wall tiles
        boolean aligned = (wall[row][Math.floorMod(col - 1, COL)] && wall[row][Math.floorMod(col + 1, COL)]) || (wall[Math.floorMod(row - 1, ROW)][col] && wall[Math.floorMod(row + 1, ROW)][col]);
        boolean corner = (wall[row][Math.floorMod(col - 1, COL)] && wall[Math.floorMod(row - 1, ROW)][col]) || (wall[row][Math.floorMod(col - 1, COL)] && wall[Math.floorMod(row + 1, ROW)][col]) || (wall[row][Math.floorMod(col + 1, COL)] && wall[Math.floorMod(row - 1, ROW)][col]) || (wall[row][Math.floorMod(col + 1, COL)] && wall[Math.floorMod(row + 1, ROW)][col]);
        return aligned || corner;
    }

    public void input(char ch) {
            if (setupDone) { // maze section
                clear(xCoor, yCoor);

                if (ch >= 65 && ch <= 90) { // for caps
                    ch = (char) ((int) ch + 32);
                }
                // WASD movement
                if (ch == 'w') {
                    if (yCoor - 1 == -1 || wall[yCoor - 1][xCoor]) {
                        StdAudio2.playInBackground("bird.wav");
                    } else {yCoor = yCoor - 1;}
    
                }
                else if (ch == 'a') {
                    if (xCoor - 1 == -1 || wall[yCoor][xCoor - 1]) {
                        StdAudio2.playInBackground("bird.wav");
                    } else {xCoor = xCoor - 1;}
                }
                else if (ch == 'd') {
                    if (xCoor + 1 == COL || wall[yCoor][xCoor + 1]) {
                        StdAudio2.playInBackground("bird.wav");
                    } else {xCoor = xCoor + 1;}
                }
                else if (ch == 's') {
                    if (yCoor + 1 == ROW || wall[yCoor + 1][xCoor]) {
                        StdAudio2.playInBackground("bird.wav");
                    } else {yCoor = yCoor + 1;}
                }
                else if (ch == 'e') {
                    System.out.println("Nothing to see here.");
                }
                avatar(xCoor, yCoor, ch);
                // win condition checks
                // save location of first winTile
                if (winCon[yCoor][xCoor] && haveWon == 0) {
                    winConX1 = xCoor;
                    winConY1 = yCoor;
                    haveWon++;
                    System.out.println("First goal!");
                    // check against coordinates of second winTile to make sure it's not the same tile
                } else if (winCon[yCoor][xCoor] && (winConX1 != xCoor && winConY1 != yCoor)) {
                    haveWon++;
                    System.out.println("Second goal!");
                }
                if (winCondition()) { // you win
                    StdAudio2.stopInBackground();
                    System.out.println("Maze cleared!!");
                    Endings.initSFX("snd_dumbvictory.wav");
                    stillPlaying = false;
                    timer.won = true;
                } else if (loseCondition()) { // you loses
                    System.out.println("Ha ha! You're too slow!");
                    timer.won = true;
                    stillPlaying = false;
                    PlayEscape.setLost();
                }
                
            } else { // drawing section
                if (ch == 'r' || ch == 'R') { // restart (to clear walls)
                    start();
                }
                if (ch == '\n' && numWalls >= MIN_NUM_WALLS) { // save the maze
                    setupDone = true;
                    establishWinCon();
                    drawGrid('d');

                    System.out.println("Go!");
                }
            }
    }

    public boolean winCondition() {
        return haveWon >= 2 && (xCoor == COL - 1 && yCoor == ROW / 2);
    }

    public boolean loseCondition() {
        return timer.currentTime() == -1;
    }

    private void establishWinCon() { // randomly picks 2 non wall tiles for win condition
        int n = 0;
        while (n < 2) {
            int winRow = rand.nextInt(winCon.length);
            int winCol = rand.nextInt(winCon[0].length);
            if (!wall[winRow][winCol]) {
                winCon[winRow][winCol] = true;
                n++;
            }    
        }
        // timer music
        if (PlayEscape.hardMode) {
            StdAudio2.playInBackground("timer10.wav");
        } else {
            StdAudio2.playInBackground("timer16.wav");
        }
        
        // timer
        if (setupDone) {
            timer = new Timer(time);
            System.out.println("new timer");
            timerStarted = true;
        }
        
        
    }

    public String returnName() {
        return "maze";
    }

}
