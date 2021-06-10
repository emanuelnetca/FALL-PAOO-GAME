package Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class GameManager { // SINGLETON PATTERN

    private static GameManager ourInstance = new GameManager(); // The instance of this class

    public GameData gameData;
    private Json json = new Json(); // This json is the object that is going to save the data.
    private FileHandle fileHandle = Gdx.files.local("bin/GameData.json"); // This is the path to game data

    public boolean gameStartedFromMainMenu, isPaused = true;
    public int lifeScore, coinScore, score;
    /*
    gameStartedFromMainMenu - It will indicate if the game just started so that I can set up initial values. If the
    game is started from the main menu the life count is going to be 2 (the player start with 2 lives), coin score
    count is going to be 0 and score is also going to be 0.
    If the player is going die, then I am going to set the score to current score (if the player has more lives to
    continue) that the player had so far.
     */

    private Music music; // Declared a Music variable (from libGDX)

    private GameManager() { // The constructor. It's private because I want only one instance

    }

    public void initializeGameData() {
        /*
        I am going to check if the file (from file handle PATH) exists. If the file exists that means that I already
        created the game data. If it does not exist that means that I did not create the data and this is the first time
        running the game on any device.
         */
        if(!fileHandle.exists()) { // if file handle does not exist, this is the first time when I run the game.
            gameData = new GameData(); // create a game data object

            // Initialize game data object
            gameData.setHighScore(0); // When I first run the game on any devices, the high score will be 0
            gameData.setCoinHighScore(0); // When I first run the game on any devices, the coin high score will be 0
            // Now I set the game difficulty. I want my game to start with medium difficulty.
            gameData.setEasyDifficulty(false);
            gameData.setMediumDifficulty(true);
            gameData.setHardDifficulty(false);

            gameData.setMusicOn(false); // When I first start the game I dont want to play the music.

            saveData(); // A method implemented below
        } else { // if the file already exist then just load it.
            loadData(); // A method implemented below
        }
    }

    // Method for saving data
    public void saveData() {
        if(gameData != null) { // If gameData is initialized
            fileHandle.writeString(Base64Coder.encodeString(json.prettyPrint(gameData)), false);
            /*
            base64 coder is going to encode the jason pretty print of the game data and it will not be readable from a
            person. If I don't use that encoder then the data will be readable and a person could easily change the data
            (like high score, coin high score etc.) from the game.
            json prettyPrint is going to print everything that is in the game data as a string
            false - this parameter means, do I want to override the existing data if there is some data that already
                    exists? So, do I want to override it or append to it. I actually want to override that data this is
                    why I set that parameter to false.
             */
        }
    }

    // Method for loading data
    public void loadData() {
        gameData = json.fromJson(GameData.class, Base64Coder.decodeString(fileHandle.readString()));
        /*
        File handle already has the path, where it's declared, so it will know where o locate that object that I have
        saved and read from it as a string.
        I need to decode it because above, in save data method, I encoded the data.
         */
    }

    public void checkForNewHighScores() { // A method that in going to check the score when the player dies.
        int oldHighScore = gameData.getHighScore();
        int oldCoinScore = gameData.getCoinHighScore();

        if(oldHighScore < score) { // If the old highScore is less than current score then I will have a new high score
            gameData.setHighScore(score); // I am passing the current score
        }

        if(oldCoinScore < coinScore) {
            /*
            If the old coin high core is less than current coin score then I will have a new coin high score
             */
            gameData.setCoinHighScore(coinScore); // I am passing the current coin score
        }

        saveData(); // A method implemented above
    }

    public void playMusic() {
        // This is going to initialize the music variable (if it's not initialize) and it's going to play the music
        if(music == null) { // If music is equal to null that means that I did not initialize the music variable
            // So it's means that I need to give it a path to the music files
            music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Background.mp3"));
        }

        if(!music.isPlaying()) { // If the music is not playing
            music.play(); // If the music is not playing, then play the music
        }
    }

    public void stopMusic() {
        // A method that is going to stop the music

        if(music.isPlaying()) { // If the music is playing
            music.stop(); // I stop the music
            music.dispose(); // I dispose the music. Next time, when I will play the music it will be initialize again
            // in playMusic() method
        }
    }

    public static GameManager getInstance() {
        return ourInstance;
    }

}