//*******************************************************************
//
//   File: WordleHard.java          Assignment No.: FINAL PROJECT
//
//   Author: afo6
//
//   Class: WordleHard
// 
//   Dependencies: wordsHard.txt
//   --------------------
//      In this game, a player guesses 3 words at the same time. They have 
//      9 guesses. The cells are coded grey (when the letter is not i9n the word),
//      yellow (when the letter is in the word but placed wrongly) and green 
//      when the letter is in the word and placed at the right place. This game
//      also has special sound effects.
//      This particular code is for the hard version of the game, where the words 
//      have 5 letters. 
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

public class WordleHard extends Room {
    // variables declaration
    private final int GUESSES;
    private final int WORD_LENGTH;
    private final int FRAME_T;
    private static int numGrid;
    private static Color backgroundColor;
    private static ArrayList<String> wordList;
    private static ArrayList<String> guessedWords;
    private static String[] actualWords;
    private static int guessesLeft;
    private static StringBuilder guessedWord;
    private static String myGuess;
    private static String guessFont;
    private Clip backgroundMusic;
    public static boolean stillPlaying;
    public static boolean animatingWinScreen;
    public static Clip SFX;

    public static BufferedImage offscreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static Graphics2D osg = offscreen.createGraphics();

    // constructor
    public WordleHard(){
        // variables declaration
        super();
        wordList = new ArrayList<>();
        guessedWords = new ArrayList<>();
        numGrid = 3;
        actualWords = new String[numGrid];
        backgroundColor = Color.BLACK;
        guessFont = "Times New Roman";
        GUESSES = 9;
        WORD_LENGTH = 5;
        FRAME_T = 17;
        guessesLeft = GUESSES;
        loadWords("wordsHard.txt");
        guessedWord = new StringBuilder("".repeat(WORD_LENGTH));
        generateWord();
        myGuess = guessedWord.toString();
    } 

    public void start() {
        initAudio();
        stillPlaying = true;
        animatingWinScreen = true;
        animateInt(3);
        drawGrid();   
        while(stillPlaying) {
            System.out.print("");
        }
        initSFX("snd_dumbvictory.wav");
        animateWinningScreen();
        while(animatingWinScreen) {
            System.out.print("");
        }
        stopBackgroundMusic();
        PlayEscape.changeRooms();
    }

    // load background audio
    private void initAudio() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("background.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Error initializing audio: " + e.getMessage());
        }
    }

    // methopd for background audio
    // when to start
    public void playBackgroundMusic() {
        if (backgroundMusic != null && !backgroundMusic.isRunning()) {
            backgroundMusic.start();
        }
    }
    
    // when to stop
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    // load sound effects
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

    // load words
    public void loadWords(String filename){
        try {
            Scanner scanner = new Scanner(new File(filename));
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                wordList.add(word);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found.");
        }
    }

    // generate words
    public void generateWord() {
        for (int i = 0; i < numGrid; i++){
            actualWords[i] = wordList.get((int)(Math.random()*wordList.size()));
            System.out.println(actualWords[i]);
        }    
    }
    

    // animate the iniatial (welcome) screen
    public void animateInt(double countdown) {
        for (double t = 0; t <= countdown * 1000; t += FRAME_T) {
            Color[] colors = { Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA };
    
            try {
                BufferedImage wordleLogo = ImageIO.read(new File("wordle3.jpg"));
                osg.drawImage(wordleLogo, 340, 50, null);
            } catch (IOException e) {
                System.err.println("Error loading image: " + e.getMessage());
            }

            osg.setFont(new Font("Times New Roman", Font.BOLD, 90));
            String word = "WORDLE";
            int x = 230; 
            int y = 350; 
            for (int i = 0; i < word.length(); i++) {
                osg.setColor(colors[i % colors.length]); 
                osg.drawString(Character.toString(word.charAt(i)), x, y);
                x += osg.getFontMetrics().charWidth(word.charAt(i))  + 5; 
            }
            osg.setColor(Color.WHITE);
            osg.setFont(new Font("Times New Roman", Font.PLAIN, 28));
            osg.drawString("Get 9 chances to guess three 5-letter words at once", 170, 420); 
            osg.setColor(Color.RED);
            osg.drawString("(Hard Mode)", 375, 450); 

           PlayEscape.g.drawImage(offscreen, 0, 0, null);
            PlayEscape.panel.sleep(FRAME_T);
        }
    }

    // method for drawing one grid
    // takes parameters for the size of the cells and the starting x and y positions 
    private void intDrawGrid(int x, int y) {
        int cellSize = 40;
        int boxSpace = 5;
        int rows = GUESSES;      
        int cols = WORD_LENGTH;     

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int cellX = x + col * (cellSize + boxSpace);
                int cellY = y + row * (cellSize + boxSpace);
               PlayEscape.g.drawRect(cellX, cellY, cellSize, cellSize);
            }
        }
    }
       

    // method for calling the grids
    public void drawGrid() {   
       PlayEscape.g.setColor(backgroundColor);
       PlayEscape.g.fillRect(0, 0, WIDTH, HEIGHT);
    
       PlayEscape.g.setColor(Color.WHITE);
        
        intDrawGrid(45, 100);
        intDrawGrid(335, 100);
        intDrawGrid(625, 100);

       PlayEscape.g.setColor(Color.RED);
       PlayEscape.g.setFont(new Font(guessFont, Font.PLAIN, 28)); 
       PlayEscape.g.drawString("Guesses left: " + guessesLeft, 595, 560); 
    }

    /// method for taking input
    public void input(char ch) {
        myGuess = guessedWord.append(Character.toUpperCase(ch)).toString();
        System.out.println(myGuess);

        if (myGuess.length() == WORD_LENGTH && guessesLeft > 0) { // checks if word is 5 letters long and player still has guesses left
            if (wordList.contains(myGuess)){
                guessesLeft--; 
                updatePanel(myGuess); // calls method to update the grids with letters and colored cells
            } 
            else{
                System.out.println("Your guess is not in the word list! ");
            } 
            // reset 
            myGuess = "";
            guessedWord = new StringBuilder("".repeat(WORD_LENGTH)); 
            myGuess = guessedWord.toString();  
        }    
    }
    
    // method for updating the grid
    // takes parameters for 
    private void initUpdatePanel(int x, int y, String word){
        int cellSize = 40;
        int boxSpace = 5; 
        
        if (guessedWords.contains(word)) {
            return;
        }
    
        for (int col = 0; col < WORD_LENGTH; col++) {
            int cellX = x + col * (cellSize + boxSpace);
            int cellY = y + (GUESSES - guessesLeft - 1) * (cellSize + boxSpace); 

            char guessChar = myGuess.charAt(col);
            char actualChar = word.charAt(col);

            Color boxColor = Color.GRAY; // gray when letter is absent
            if (guessChar == actualChar) {
                boxColor = Color.GREEN; // green when letter is present + at the right position
            } else if (word.contains(Character.toString(guessChar))) {
                boxColor = Color.YELLOW; // yellow if present but at wrong position
            }

           PlayEscape.g.setColor(boxColor);
           PlayEscape.g.fillRect(cellX + 1, cellY + 1, cellSize - 1, cellSize - 1);

           PlayEscape.g.setColor(Color.BLACK);
           PlayEscape.g.setFont(new Font("Times New Roman", Font.BOLD, 20));
            int charX = cellX + (cellSize -PlayEscape.g.getFontMetrics().stringWidth(Character.toString(guessChar))) / 2;
            int charY = cellY + (cellSize +PlayEscape.g.getFontMetrics().getAscent()) / 2;
           PlayEscape.g.drawString(Character.toString(guessChar), charX, charY);
        }
        boolean wordGuessed = true;
        for (int col = 0; col < WORD_LENGTH; col++) {
            char guessChar = myGuess.charAt(col);
            char actualChar = word.charAt(col);
            if (guessChar != actualChar) {
                wordGuessed = false;
                break;
            }
        }
    
        if (wordGuessed && wordList.contains(word)) {
            guessedWords.add(word); 
            return;
        }         
    }
    
    // calls the update for each grid of word
    // updates guesses left
    public void updatePanel(String myGuess) {
       PlayEscape.g.setColor(backgroundColor);
       PlayEscape.g.fillRect(550,530, 600, 50);    

       PlayEscape.g.setColor(Color.RED);
       PlayEscape.g.setFont(new Font(guessFont, Font.PLAIN, 28));
       PlayEscape.g.drawString("Guesses left: " + guessesLeft, 595, 560);
    
       PlayEscape.g.setColor(Color.WHITE);
        for (int i = 0; i < numGrid; i++){
            String word1 = actualWords[0];
            String word2 = actualWords[1];
            String word3 = actualWords[2];

            initUpdatePanel(45, 100, word1);
            initUpdatePanel(335, 100, word2);
            initUpdatePanel(625, 100, word3);
        }
      
        checkWin(myGuess);
    }

    // checks if player guessed all words right
    public void checkWin(String myGuess){
        boolean allWordsGuessed = true;
        for (int i = 0; i < numGrid; i++){
            if (!guessedWords.contains(actualWords[i])){
                allWordsGuessed = false;
                break;
            }
        }
        
        // they win when all words are right
        if (allWordsGuessed) {
            stillPlaying = false;
            System.out.println("Congratulations! You guessed all three words correctly.");
        }

        // lose when not all words are right and they have 0 guesses left
        else if (!allWordsGuessed && guessesLeft == 0){
            System.out.println("Oops, you lost!");
            System.out.println("The words were:");
            for (int i = 0; i < numGrid; i++) {
                System.out.println(actualWords[i]);
            }
            stillPlaying = false;
            PlayEscape.setLost();
        }
    }
    
    // draws animation for the win screen
    public void animateWinningScreen() {
        for (double t = 0; t < 2; t += FRAME_T / 1000.0) {
           PlayEscape.panel.sleep(FRAME_T);  
        } 
        for (double t = 0; t < 3; t += FRAME_T / 1000.0) {
            for (int j = 0; j < 100; j++) {
                int x = (int) (Math.random() * WIDTH);
                int y = (int) (Math.random() * HEIGHT);
                Color color = new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
    
               PlayEscape.g.setColor(color);
               PlayEscape.g.fillRect(x, y, 5, 5); 
            } 
           PlayEscape.panel.sleep(FRAME_T);  
        } 
        animatingWinScreen = false;
    }

    public String returnName() {
        return "wordle";
    }
    
    
    public static void main(String[] args) {
        WordleHard game = new WordleHard();
        game.start();
    }

    // overrides avatar with nothing
    public void avatar(int x, int y) {} 

    // overrides clear with nothing
    public void clear(int x, int y) {}
}