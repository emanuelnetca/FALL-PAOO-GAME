package Helpers;

public class GameData {
    /*
    In this class I am going to model the data that I need to save. I am going to save it as an object.
     */

    private int highScore; // A private integer to keep track of the high score.
    private int coinHighScore; // A private integer to keep track of the coin score.

    // Some private booleans that are going to track which difficulty is selected.
    private boolean easyDifficulty; //
    private boolean mediumDifficulty;
    private boolean hardDifficulty;

    private boolean musicOn; // A private boolean which is going to track if the music is on or off.

    // Getters and setters
    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getCoinHighScore() {
        return coinHighScore;
    }

    public void setCoinHighScore(int coinHighScore) {
        this.coinHighScore = coinHighScore;
    }

    public boolean isEasyDifficulty() {
        return easyDifficulty;
    }

    public void setEasyDifficulty(boolean easyDifficulty) {
        this.easyDifficulty = easyDifficulty;
    }

    public boolean isMediumDifficulty() {
        return mediumDifficulty;
    }

    public void setMediumDifficulty(boolean mediumDifficulty) {
        this.mediumDifficulty = mediumDifficulty;
    }

    public boolean isHardDifficulty() {
        return hardDifficulty;
    }

    public void setHardDifficulty(boolean hardDifficulty) {
        this.hardDifficulty = hardDifficulty;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }
}
