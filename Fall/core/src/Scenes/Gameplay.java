package Scenes;

import Clouds.CloudsController;
import Helpers.GameInfo;
import Helpers.GameManager;
import Huds.UIHud;
import Player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.manu.GameMain;

public class Gameplay implements Screen, ContactListener {

    private GameMain game;

    private OrthographicCamera mainCamera;
    private Viewport gameViewport;
    /*
    the viewport that's going to take the mainCamera and display what I see on the screen
     */

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;
    /*
    for debugRenderer look at introduction, its for drawing on the screen -------------------------------------------
     */

    private World world; // This is the physics world which is going to hold all of our bodies inside

    private Sprite[] bgs; // A sprite array for my game backgrounds
    private float lastYPosition;

    private float cameraSpeed = 10; // A variable which is going to store the camera speed, for all three difficulty levels
    private float maxSpeed = 10; // A variable which is going to store the maximum camera speed
    private float acceleration = 10; // A variable which is going to give a boost to the speed

    private boolean touchedForTheFirstTime;

    private UIHud hud;

    private CloudsController cloudsController;

    private Player player;
    private float lastPlayerY; // This variable is going to store the last player Y position

    public Gameplay(GameMain game) {
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        /*
        I set the position of the camera at the center ( this is why I divide by 2 the X and Y ). That function take
        three parameters (X, Y and Z) but since my game is 2D I only need X and Y and Z = 0.
        Now I have to actually create my game viewport.
         */
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT);
        /*
        This viewport is going to stretch on any screen and make this game look nice on every screen
         */

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);
        /*
        I set the position of the box2DCamera at the center. That function take 3 parameters (X, Y and Z)
        but since my game is 2D I only need X and Y and Z = 0
         */

        debugRenderer = new Box2DDebugRenderer();

        hud = new UIHud(game);

        world = new World(new Vector2(0, -9.8f), true);
        /*
        Vector2 is a vector with only 2 elements, X and Y. It's a pair of coordinates.
        new Vector2(0, -9.8f) - With I set the gravity to be -9.8, the gravity of the earth.
        X = 0 - For the X-axis I don't want gravity at all
        Y = -9.8 it
        doSleep: true - this parameter will allow different bodies to sleep when nothing is happening to them (no force
        is applied to them)
         */
        world.setContactListener(this);
        /*
        Inform the world that the contact listener is the gameplay class
         */

        cloudsController = new CloudsController(world);

        player = cloudsController.positionThePlayer(player);
        /*
        This it will return a new player object with the position set to be above the first cloud.
         */

        createBackgrounds();
        setCameraSpeed();
    }

    void handleInput(float dt) {
        /*
        This function it will be called every frame because the function is called in update function and update
        function is called in render function which is called every frame.
        dt = delta time, the time between each frame.
        This function handle my input. If the key pressed is the left arrow then I subtract 2 from X because I want
        the player to move to the left and add 2 when I want the player to move to the right.
         */
        if(Gdx.input.isKeyPressed((Input.Keys.LEFT))) {
            player.movePlayer(-2);
        } else if(Gdx.input.isKeyPressed((Input.Keys.RIGHT))) {
            player.movePlayer(2);
        } else {
            player.setWalking(false);
        }
    }

    void checkForFirstTouch() {
        /*
        I am going to check when I touch the screen for the first time
         */
        if(!touchedForTheFirstTime) {
            if(Gdx.input.justTouched()) { // If I just touched on the screen
                touchedForTheFirstTime = true;
                GameManager.getInstance().isPaused = false;
                lastPlayerY = player.getY(); // Get the Y position of the player.
                // As soon as I start the game, I will get the player last Y position.
            }
        }
    }

    void update(float dt) { // dt = delta time
        /*
        This function ( checkBackgroundsOutOfBounds ) it will be called every frame, because the render method is
        called at each frame.
         */

        checkForFirstTouch();

        if(!GameManager.getInstance().isPaused) {
            handleInput(dt);
            moveCamera(dt);
            checkBackgroundsOutOfBounds();
            cloudsController.setCameraY(mainCamera.position.y);
            cloudsController.createAndArrangeNewClouds();
            cloudsController.removeOffScreenCollectables();
            /*
            If the game is not paused then I am going to run anything that's here. If it's paused then I am not going
            to run anything meaning the game will stop.
             */
            checkPlayersBounds();
            countScore();
        }
    }

    void moveCamera(float delta) {
        // delta = delta time (the time that takes for one frame to go to another frame)

        mainCamera.position.y -= cameraSpeed * delta;
        // Move the camera downwards by using the camera speed and multiplying that by delta

        cameraSpeed += acceleration * delta;
        // I am boosting the camera speed by adding to it the acceleration multiplied by delta

        if(cameraSpeed > maxSpeed) {
            // I am checking if the camera speed is not greater than maxSpeed
            cameraSpeed = maxSpeed; // If it's greater, then set the camera speed to the max speed
            // This will not allow the camera speed to go above maximum speed
        }
    }

    void setCameraSpeed() {
        // A method that is going to set the camera speed based on the difficulty that is selected

        if(GameManager.getInstance().gameData.isEasyDifficulty()) {
            // If the easy difficulty is selected
            cameraSpeed = 80; // The camera speed will be 80
            maxSpeed = 100; // The max camera speed will be 100
        }

        if(GameManager.getInstance().gameData.isMediumDifficulty()) {
            // If the medium difficulty is selected
            cameraSpeed = 100; // The camera speed will be 80
            maxSpeed = 120; // The max camera speed will be 100
        }

        if(GameManager.getInstance().gameData.isHardDifficulty()) {
            // If the hard difficulty is selected
            cameraSpeed = 120; // The camera speed will be 80
            maxSpeed = 140; // The max camera speed will be 100
        }

        // I will call this method into this class constructor
    }

    void createBackgrounds() {
        bgs = new Sprite[3]; // A sprite array with 3 backgrounds

        for(int i = 0; i < bgs.length; ++i) {
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            /*
             I set Y - "-(i * bgs[i].getHeight())" - to be negative because I will move the camera down,
             to the negative side
             */
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
            /*
             i = 0, first iteration
             i * bgs[i].getHeight()
             0 * 800 = 0, so the first background is going to be positioned at X = 0 and Y = 0
             *
             i = 1, second iteration
             i * bgs[i].getHeight()
             1 * 800 = 800, X = 0 and Y = 800, this will place the enxt background exactly below the first background
             *
             and so on
             */
            lastYPosition = Math.abs(bgs[i].getY());
            /*
            I will store the position of the last background inside lastYPosition variable
             */
        }
    }

    void checkBackgroundsOutOfBounds() {
        /*
        This function will check if my game backgrounds are out of camera's view
         */
        for(int i = 0; i < bgs.length; ++i) {
            if((bgs[i].getY() - bgs[i].getHeight() / 2f - 5) > mainCamera.position.y) {
                float newPosition = bgs[i].getHeight() + lastYPosition;
                /*
                The height of the background is 800
                The lastYPosition = 1600
                800 + 1600 = 2400
                Now the newPosition = 2400
                I set the position of the new background at 2400, right after my 3rd background
                I set the newPosition to be negative because the camera is moving down
                The lastYPosition = 2400
                800 + 2400 = 3200
                Now the newPosition = 3200
                And so on
                 */
                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }

    void checkPlayersBounds() {
        /*
        I need to strip the player so when he is out of the sight of the camera up, down, left or right I need to kill
        the player.
         */
        if(player.getY() - GameInfo.HEIGHT / 2f - player.getHeight() / 2f > mainCamera.position.y) {
            if(!player.isDead()) { // If the player is not dead
                playerDied();
            }
        }

        if(player.getY() + GameInfo.HEIGHT / 2f + player.getHeight() / 2f < mainCamera.position.y) {
            /*
            If the player position (Y axis) plus the half of the height of the game plus the half of the height of the
            is less than the main camera position Y, the player will be out of bounds but downwards.
             */
            if(!player.isDead()) { // If the player is not dead
                playerDied();
            }
        }

        if(player.getX() - 25 > GameInfo.WIDTH || player.getX() + 60 < 0) {
            // player out of bounds on the right side or player out of bounds left.
            if(!player.isDead()) { // If the player is not dead
                playerDied();
            }
        }
    }

    void countScore() {
        if(lastPlayerY > player.getY()) {
            /*
            If the player last Y position is greater that player current position (because when the player goes down
            he is going in the negative side on the Y axis).
             */
            hud.incrementScore(1);
            lastPlayerY = player.getY();
        }
    }

    void playerDied() {
        /*
        Inside this method I am going to put every code that The game need to execute when the player die.
        Remove the player from the screen, check if the player has more lives left to continue the game, check if the
        player has no more lives etc.
         */

        GameManager.getInstance().isPaused = true; // I stop the game, because the player has died.
        hud.decrementLife(); // When the player die, I decrement the lives count
        player.setDead(true); // Set the dead boolean to true, because the player in now dead.
        player.setPosition(1000, 1000); // This will simulate that the player is gone (being removed) from the game.

        if(GameManager.getInstance().lifeScore < 0) {
            /*
            If GameManager.getInstance().lifeScore is less than 0, player has no more lives left to continue the game.
             */

            // Check if I have a new high score
            GameManager.getInstance().checkForNewHighScores();

            // Show the end score
            hud.createGameOverPanel();
            // Load main menu
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenu(game));
                }
            });
            /*
            Actions are built in in libGDX and I can use them for example to fade the screen or to delay time or fade
            in the screen and so on.
            This runnable action will enable us to reload or practically put my own code there an execute it when I
            call this action.
             */

            SequenceAction sa = new SequenceAction();
            /*
            If I have multiple actions, in this case, I will have 3 actions, one for delaying the time, other one for
            fading the screen and the third one to loading the main menu.
             */
            sa.addAction(Actions.delay(3f)); // 3 seconds for delay
            sa.addAction(Actions.fadeOut(1f)); // 1 second for fade
            sa.addAction(run);
            /*
            This action sequence represents the order by which these actions are going to be executed.
            sa.addAction(Actions.delay(3f)); - first action that it will execute
            sa.addAction(Actions.fadeOut(1f)); - second action that it will execute
            sa.addAction(run); - third action that it will execute
            In order to execute this, I need to add these actions to the stage.
             */
            hud.getStage().addAction(sa);
        } else {
            // Reload the game so that the player can continue to play
            RunnableAction run = new RunnableAction();
            run.setRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new Gameplay(game));
                }
            });

            SequenceAction sa = new SequenceAction();
            sa.addAction(Actions.delay(3f)); // 3 seconds for delay
            sa.addAction(Actions.fadeOut(1f)); // 1 second for fade
            sa.addAction(run);
            hud.getStage().addAction(sa);
        }

    }

    void drawBackgrounds() {
        /*
        Now that I create our BGs ( createBackgrounds function ) I need to draw them and that's what this function does
         */
        for(int i = 0; i < bgs.length; ++i) {
            game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // render method is called at each frame

        update(delta);
        /*
        This delta time is the time between each frame; the time that takes to one frame to get to another frame.
         */

        // I need to clear the color and screen before I can draw anything on the screen
        Gdx.gl.glClearColor(0, 0, 0, 1); // Clear the color, the screen will be black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen

        game.getBatch().begin();

        drawBackgrounds();
        /*
        With these functions ( createBackgrounds and drawBackgrounds ) I can create thousands of these sprites
        very quickly and simple
         */
        cloudsController.drawClouds(game.getBatch());
        cloudsController.drawCollectables(game.getBatch());

        player.drawPlayerIdle(game.getBatch());
        player.drawPlayerAnimation(game.getBatch());
        /*
        This line of code is to draw the player on the screen.
         */

        game.getBatch().end();

//        debugRenderer.render(world, box2DCamera.combined);

        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();
        /*
         I need to call act method on the stage, in order to be able to execute those actions from playerDied method.
        */
        game.getBatch().setProjectionMatrix(mainCamera.combined);
        // That is going to return the projection matrix of the camera
        mainCamera.update();

        player.updatePlayer();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        /*
        ----- A fost explicat, velocity and position -----------------------------------------
        The delta time is equal to the difference between the frames or the time that takes from one frame to go to
        another frame.
         */
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        /*
        I need to dispose every objects that have the dispose() method.
         */
        world.dispose();
        for(int i = 0; i< bgs.length; ++i) {
            bgs[i].getTexture().dispose();
        }
        player.getTexture().dispose();
        debugRenderer.dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        /*
        The contact parameter holds the bodies or the fixtures of the bodies that contact with each
         */
        Fixture body1, body2;
        // body1 will be the player
        if(contact.getFixtureA().getUserData() == "Player") {
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body2 = contact.getFixtureA();
            body1 = contact.getFixtureB();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Coin") {
            // collided with the coin
            hud.incrementCoins();
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Life") {
            // collided with the life
            hud.incrementLives();
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Dark Cloud") {
            // When the player will collide with a dark cloud, then he will die.
            if(!player.isDead()) {
                playerDied();
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
