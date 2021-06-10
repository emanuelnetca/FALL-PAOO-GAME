package Huds;

import Helpers.GameInfo;
import Helpers.GameManager;
import Scenes.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.manu.GameMain;

public class OptionsButtons {

    private GameMain game;
    private Viewport gameViewport;
    private Stage stage;

    private ImageButton easy, medium, hard, backBtn;
    private Image sign;

    public OptionsButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        createAndPositionButtons();
        addAllListeners();

        stage.addActor(easy);
        stage.addActor(medium);
        stage.addActor(hard);
        stage.addActor(backBtn);
        stage.addActor(sign);
    }

    void createAndPositionButtons() {
        // Create easy button
        easy = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Buttons/Easy.png"))));

        // Create medium button
        medium = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Buttons/Medium.png"))));

        // Create hard button
        hard = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Buttons/Hard.png"))));

        // Create back button
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Buttons/Back.png"))));

        // Create check sign on easy, medium and hard buttons
        sign = new Image(new Texture("Buttons/Options Buttons/Check Sign.png"));

        // Set position for easy button
        easy.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 40, Align.center);
        // Set position for medium button
        medium.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 40, Align.center);
        // Set position for hard button
        hard.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 120, Align.center);

        positionTheSign(); // This method is implemented below

    }

    void addAllListeners() {

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // save the difficulty
                game.setScreen(new MainMenu(game));
                /*
                When I click on the easy button I am going to reposition the sign on the easy button.
                 */
            }
        });

        easy.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(0); // Changing the difficulty to 0 (easy), method implemented below
                sign.setY(easy.getY() + 13); // I put the check sign on the easy button
            }
        });

        medium.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(1); // Changing the difficulty to 1 (medium), method implemented below
                sign.setY(medium.getY() + 13); // I put the check sign on the medium button
            }
        });

        hard.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeDifficulty(2); // Changing the difficulty to 2 (hard), method implemented below
                sign.setY(hard.getY() + 13); // I put the check sign on the hard button
            }
        });
    }

    void positionTheSign() {
        // This method is going to position the check sign based on the difficulty that is selected

        if(GameManager.getInstance().gameData.isEasyDifficulty()) {
            // If the easy difficulty is selected the set the position of the check sign on easy image (easy.getY)
            sign.setPosition(GameInfo.WIDTH / 2f + 80, easy.getY() + 13, Align.bottomLeft);
        }

        if(GameManager.getInstance().gameData.isMediumDifficulty()) {
            // If the easy difficulty is selected the set the position of the check sign on medium image (medium.getY)
            sign.setPosition(GameInfo.WIDTH / 2f + 80, medium.getY() + 13, Align.bottomLeft);
        }

        if(GameManager.getInstance().gameData.isHardDifficulty()) {
            // If the easy difficulty is selected the set the position of the check sign on hard image (hard.getY)
            sign.setPosition(GameInfo.WIDTH / 2f + 80, hard.getY() + 13, Align.bottomLeft);
        }
    }

    void changeDifficulty(int difficulty) {
        // With this method I am going to change the difficulty and save it in GameManager

        switch(difficulty) {

            case 0: // Setting the easy difficulty using the setters from gameData
                GameManager.getInstance().gameData.setEasyDifficulty(true);
                GameManager.getInstance().gameData.setMediumDifficulty(false);
                GameManager.getInstance().gameData.setHardDifficulty(false);
                break;

            case 1: // Setting the medium difficulty using the setters from gameData
                GameManager.getInstance().gameData.setEasyDifficulty(false);
                GameManager.getInstance().gameData.setMediumDifficulty(true);
                GameManager.getInstance().gameData.setHardDifficulty(false);
                break;

            case 2: // Setting the hard difficulty using the setters from gameData
                GameManager.getInstance().gameData.setEasyDifficulty(false);
                GameManager.getInstance().gameData.setMediumDifficulty(false);
                GameManager.getInstance().gameData.setHardDifficulty(true);
                break;

        }

        GameManager.getInstance().saveData(); // After I have set the difficulty now I need to save it, using saveData
    }

    public Stage getStage() {
        return this.stage;
    }

}
