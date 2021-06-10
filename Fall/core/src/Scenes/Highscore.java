package Scenes;

import Helpers.GameInfo;
import Huds.HighscoreButtons;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.manu.GameMain;

public class Highscore implements Screen {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    /*
    OrthographicCamera - this is going to display everything on the screen
    Viewport which is going to hold that camera
     */

    private Texture bg; // The texture I wan to draw on the screen

    private HighscoreButtons btns;

    public Highscore(GameMain game) {
        this.game = game;

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false, GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        bg = new Texture("Backgrounds/Highscore BG.png");

        btns = new HighscoreButtons(game);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // I need to clear the color and screen before I can draw anything on the screen
        Gdx.gl.glClearColor(1, 0, 0, 1); // Clear the color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        game.getBatch().begin();

        game.getBatch().draw(bg, 0, 0);

        game.getBatch().end();

        game.getBatch().setProjectionMatrix(btns.getStage().getCamera().combined);
        btns.getStage().draw();

    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        // It will resize the game viewport
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        bg.dispose();
        btns.getStage().dispose();
    }
}
