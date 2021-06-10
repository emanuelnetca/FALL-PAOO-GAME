package Huds;

import Helpers.GameInfo;
import Helpers.GameManager;
import Scenes.Gameplay;
import Scenes.Highscore;
import Scenes.MainMenu;
import Scenes.Options;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.manu.GameMain;

public class MainMenuButtons {

    private GameMain game;
    private Stage stage;
    /*
    A stage is similar to a Screen. I can put my elements like textures or buttons inside of it and I can draw that by
    using the stage.
     */
    private Viewport gameViewport;

    private ImageButton playBtn;
    private ImageButton highscoreBtn;
    private ImageButton optionsBtn;
    private ImageButton quitBtn;
    private ImageButton musicBtn;


    public MainMenuButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());
        /*
        The FitViewport is going to fit the screen.
         */
        stage = new Stage(gameViewport, game.getBatch());
        /*
        This is what we need to pass to the stage so that I can actually draw it.
        In order to display these buttons on the screen what I need to do is to call the stage and I need to call add
        actor. All o these buttons are called actor.
         */
        Gdx.input.setInputProcessor(stage);
        /*
        This means who is going to process the buttons touches and it will going to be the stage. The stage will be the
        one who is going to register when we press on the screen. The stage is the one who holds all of the buttons.
        The stage adds or the stage holds every single button and it can process if that button is touched, it will be
        informed and we do that by adding the listeners.
         */

        createAndPositionButtons();
        addAllListeners();

        stage.addActor(playBtn);
        stage.addActor(highscoreBtn);
        stage.addActor(optionsBtn);
        stage.addActor(quitBtn);
        stage.addActor(musicBtn);

        checkMusic(); // A method implemented below
    }

    void createAndPositionButtons() {
        playBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu Buttons/Start Game.png"))));
        highscoreBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu Buttons/Highscore.png"))));
        optionsBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu Buttons/Options.png"))));
        quitBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu Buttons/Quit.png"))));
        musicBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Main Menu Buttons/Music On.png"))));

        playBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f +  150, Align.center);
        highscoreBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f + 50, Align.center);
        optionsBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 50, Align.center);
        quitBtn.setPosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f - 150, Align.center);
        musicBtn.setPosition(GameInfo.WIDTH - 13, 13, Align.bottomRight);

        /*
        I set the image and position of the buttons with this function.
         */

    }

    void addAllListeners() {
        /*
        These listeners are actually those processors that listen for the input.
         */
        playBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*
                Any code that I type here it will be executed when I press the play button.
                 */
                GameManager.getInstance().gameStartedFromMainMenu = true;
                RunnableAction run = new RunnableAction();
                run.setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new Gameplay(game));
                    }
                });
                SequenceAction sa = new SequenceAction();
                sa.addAction(Actions.fadeOut(1f)); // 1 second for fade
                sa.addAction(run);

                stage.addAction(sa);

            }
        });

        highscoreBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*
                Any code that I type here it will be executed when I press the highScore button.
                 */
                game.setScreen(new Highscore(game));
            }
        });

        optionsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Any code that I type here it will be executed when I press the options button.
                game.setScreen(new Options(game));
            }
        });

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                /*
                Any code that I type here it will be executed when I press the quit button.
                 */
                Gdx.app.exit();
            }
        });

        musicBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Any code that I type here it will be executed when I press the music button.
                if(GameManager.getInstance().gameData.isMusicOn()) {
                    // If the music is on, when I will press on music button, I want to stop the music.
                    GameManager.getInstance().gameData.setMusicOn(false); // I set the musicOn to false
                    GameManager.getInstance().stopMusic(); // I stop the music
                } else {
                    // If the music is not playing I need to turn it on
                    GameManager.getInstance().gameData.setMusicOn(true); // I set the musicOn to true
                    GameManager.getInstance().playMusic(); // I play the music
                }
                GameManager.getInstance().saveData(); // If I either stop or play the music, I need to save data
            }
        });

    }

    void checkMusic() {
        // A method to check if the music is turned on in saved data
        if(GameManager.getInstance().gameData.isMusicOn()) {
            /*
            If I have turned on the music then quit the game, the data it will be saved and the music it will be on
            by default. If the music is on, when I start the game, I need to play it.
             */
            GameManager.getInstance().playMusic(); // I play the music
        }
        // I need to call this method inside of this class's constructor
    }

    public Stage getStage() {
        /*
        I need to access the stage in order to draw everything that I put inside of it.
         */
        return this.stage;
    }

}
