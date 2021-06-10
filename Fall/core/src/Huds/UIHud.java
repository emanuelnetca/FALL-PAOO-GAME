package Huds;

import Helpers.GameInfo;
import Helpers.GameManager;
import Scenes.MainMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.manu.GameMain;

public class UIHud {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Image coinImg, scoreImg, lifeImg, pausePanel;

    private Label coinLabel, lifeLabel, scoreLabel;

    private ImageButton pauseBtn, resumeBtn, quitBtn;

    public UIHud(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        /*
        Now that I have the viewport I can create the stage with it and along with the sprite batch.
         */
        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        if(GameManager.getInstance().gameStartedFromMainMenu) {
            // This is the first time starting the game, SET INITIAL VALUES
            GameManager.getInstance().gameStartedFromMainMenu = false;
            GameManager.getInstance().lifeScore = 2;
            GameManager.getInstance().coinScore = 0;
            GameManager.getInstance().score = 0;
            /*
            When I will start the game, the player will have 2 lives, the coinScore will be 0 and the general score
            will also be 0.
             */
        }

        createLabels();
        createImages();
        createBtnAddListener();

        Table lifeAndCoinTable = new Table();
        lifeAndCoinTable.top().left();
        lifeAndCoinTable.setFillParent(true);

        lifeAndCoinTable.add(lifeImg).padLeft(10).padTop(10);
        /*
        It will move the image 10 pixels from the left side and add 10 pixels from the top. It will move down 10 pixels
        from the top, the same with padLeft.
         */
        lifeAndCoinTable.add(lifeLabel).padLeft(5);
        /*
        I added the lifeImg now i need to add the lifeLabel. The pixels will be the same as above.
         */
        lifeAndCoinTable.row(); // It will add a new row to the table.
        lifeAndCoinTable.add(coinImg).padLeft(10).padTop(10);
        lifeAndCoinTable.add(coinLabel).padLeft(5);
        /*
        I added a new row, lifeAndCoinTable.row() and in this row i will put the coinImg and coinLabel.
         */

        // Now I need to create a new Table for score
        Table scoreTable = new Table();
        scoreTable.top().right();
        scoreTable.setFillParent(true);

        scoreTable.add(scoreImg).padRight(10).padTop(10);
        scoreTable.row(); // create a new row
        scoreTable.add(scoreLabel).padRight(20).padTop(15);

        stage.addActor(lifeAndCoinTable);
        /*
        Because I added coinImg, lifeImg, etc. in lifeAndCoinTable now when I add the only add the table as an actor
        in the stage.
         */
        stage.addActor(scoreTable); // the same as above
        stage.addActor(pauseBtn);
    }

    void createLabels() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        /*
        Free style parameter class is located inside of free type generator class.
         */
        parameter.size = 40;

        BitmapFont font = generator.generateFont(parameter);

        coinLabel = new Label("x" + GameManager.getInstance().coinScore, new Label.LabelStyle(font, Color.WHITE));
        lifeLabel = new Label("x" + GameManager.getInstance().lifeScore, new Label.LabelStyle(font, Color.WHITE));
        scoreLabel = new Label("" + GameManager.getInstance().score, new Label.LabelStyle(font, Color.WHITE));
    }

    void createImages() {
        coinImg = new Image(new Texture("Collectables/Coin.png"));
        lifeImg = new Image(new Texture("Collectables/Life.png"));
        scoreImg = new Image(new Texture("Buttons/Gameplay Buttons/Score.png"));
    }

    void createBtnAddListener() {
        pauseBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Gameplay Buttons/Pause.png"))));

        pauseBtn.setPosition(460, 15, Align.bottomRight);

        pauseBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // pause the game
                if(!GameManager.getInstance().isPaused) {
                    GameManager.getInstance().isPaused = true;
                    createPausePanel();
                }
            }
        });
    }

    void createPausePanel() {
        pausePanel = new Image(new Texture("Buttons/Pause/Pause Panel.png"));

        resumeBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Pause/Resume.png"))));

        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Pause/Quit 2.png"))));

        pausePanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);
        resumeBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 80, Align.center);

        resumeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(GameManager.getInstance().isPaused) {
                    GameManager.getInstance().isPaused = false;
                    removePausePanel();
                }
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

        stage.addActor(pausePanel);
        stage.addActor(resumeBtn);
        stage.addActor(quitBtn);
    }

    void removePausePanel() {
        pausePanel.remove();
        resumeBtn.remove();
        quitBtn.remove();
    }

    public void createGameOverPanel() {
        Image gameOverPanel = new Image (new Texture("Buttons/Pause/Show Score.png"));
        /*
        Now I need to create the font for the label. I order to create a font first I need to create the font generator
         */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        // Now in order to resize this font I need a font parameter.
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70; // The size of the font is now equal to 70 pixels.
        // Now I can create the font for the label.
        BitmapFont font = generator.generateFont(parameter);
        // I created the font and now I need to create the label (end score label).
        Label endScore = new Label(String.valueOf(GameManager.getInstance().score), new Label.LabelStyle(font, Color.WHITE));
        // I convert the score from GameManager to a string and set the style for the label.
        Label endCoinScore = new Label(String.valueOf(GameManager.getInstance().coinScore), new Label.LabelStyle(font, Color.WHITE));
        // I convert the coin score from GameManager to a string and set the style for the label.
        // Now I need to position and add them to the stage.
        gameOverPanel.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, Align.center);
        endScore.setPosition(GameInfo.WIDTH / 2f - 30, GameInfo.HEIGHT / 2f + 20, Align.center);
        endCoinScore.setPosition(GameInfo.WIDTH / 2f - 30, GameInfo.HEIGHT / 2f - 90, Align.center);
        stage.addActor(gameOverPanel);
        stage.addActor(endScore);
        stage.addActor(endCoinScore);
    }
    
    public void incrementScore(int score) {
        GameManager.getInstance().score += score;
        scoreLabel.setText("" + GameManager.getInstance().score);
    }

    public void incrementCoins() {
        ++GameManager.getInstance().coinScore;
        coinLabel.setText("x" + GameManager.getInstance().coinScore);
        incrementScore(200);
    }

    public void incrementLives() {
        ++GameManager.getInstance().lifeScore;
        lifeLabel.setText("x" + GameManager.getInstance().lifeScore);
        incrementScore(300);
    }

    public void decrementLife() {
        --GameManager.getInstance().lifeScore; // Decrement life count
        if(GameManager.getInstance().lifeScore >= 0) { // If the life count is not lower than 0
            /*
            This if statement will make sure that when the live count or the lives score is less than zero it will not
            display that because when i decrement the life, it will be at some point -1 and I don't want that.
             */
            lifeLabel.setText("x" + GameManager.getInstance().lifeScore);
        }
    }

    public Stage getStage() {
        return this.stage;
    }
}
