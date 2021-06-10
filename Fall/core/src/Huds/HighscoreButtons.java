package Huds;

import Helpers.GameInfo;
import Helpers.GameManager;
import Scenes.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.manu.GameMain;

public class HighscoreButtons {

    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label scoreLabel, coinLabel;

    private ImageButton backBtn;

    public HighscoreButtons(GameMain game) {
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionUIElements();

        stage.addActor(backBtn);
        stage.addActor(scoreLabel);
        stage.addActor(coinLabel);
    }

    void createAndPositionUIElements() {
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(
                new Texture("Buttons/Options Buttons/Back.png"))));
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/Mostwasted.ttf"));
        // This is how you set the path to the font in font generator.
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50; // it will change the size of the font to 50 pixels

        // Now that I locate the font and resize it we can create the font
        // I generate the font with the parameter properties.
        BitmapFont scoreFont = generator.generateFont(parameter);
        BitmapFont coinFont = generator.generateFont(parameter);

        // Now that we have the font we can create the labels.
        scoreLabel = new Label("HighScore: " + String.valueOf(GameManager.getInstance().gameData.getHighScore()),
                new Label.LabelStyle(scoreFont, Color.WHITE));
        // I am going to get the high score from game data using game manager
        coinLabel = new Label("Coins: " + String.valueOf(GameManager.getInstance().gameData.getCoinHighScore()),
                new Label.LabelStyle(coinFont, Color.WHITE));
        // I am going to get the coin high score from game data using game manager
        /*
        I am using the free type font generator which is going to locate the font in my project, I need to pass
        the gdx pass internal and the path to my font.
        Now that I have the font, I need to create the parameter for that font. The parameter has some properties such
        as the colors, size, etc.
         */

        backBtn.setPosition(17, 17, Align.bottomLeft);

        scoreLabel.setPosition(GameInfo.WIDTH / 2f - 160, GameInfo.HEIGHT / 2f);
        coinLabel.setPosition(GameInfo.WIDTH / 2f - 80, GameInfo.HEIGHT / 2f - 110);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });
    }

    public Stage getStage() {
        return this.stage;
    }

}
