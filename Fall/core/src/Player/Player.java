package Player;

import Helpers.GameInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    /*
    In order to draw and move the player we need the Player class to be as a sprite.
     */
    private World world;
    private Body body;

    private TextureAtlas playerAtlas;
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    /*
    I will use this variable for the animation object so that I will switch the frame from one another based on the
    delta time
     */
    private boolean isWalking, dead; // isWalking - if the player is walking, died - is the player died.

    public Player(World world, float x, float y) {
        /*
        The world, X and Y position of my player.
         */
        super(new Texture("Player/Player 1.png"));
        this.world = world;
        setPosition(x, y);
        /*
        Set position of the sprite. This function is available because we inherited SPRITE.
         */
        createBody();
        playerAtlas = new TextureAtlas("Player Animation/Player Animation.atlas");
        dead = false; // I want the player to be alive by default
    }

    void createBody() {
        /*
        If we don't type anything (public, private) by default this function it will be private.
        In order to create a body, the first thing that we need to do is to create the body definition.
         */
        BodyDef bodyDef = new BodyDef();    //  Definition of the body
        bodyDef.type = BodyDef.BodyType.DynamicBody;    //  The type of the body
        /*
        I want my player to be dynamic because I want to move him and I want him to be affected by gravity.
         */
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
        /*
        I divide everything with PPM because I want to create the ratio of 1 pixel / 100 meters.
        Now that I have the body definition I can create it.
         */
        body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        /*
        Now that I have create the body, I need to give it a shape.
        body.setFixedRotation(true) will not allow the body to rotate.
         */
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2f - 20) / GameInfo.PPM, (getHeight() / 2f) / GameInfo.PPM);
        /*
        In order to assign the shape to our body I need to assign that to the fixture, but in order to assign that to
        the fixture we need to create the fixture definition.
        I subtract 20 (getWidth() / 2f - 20)) because I want to make my box smaller.
         */
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4f; // This is the mass of the body
        fixtureDef.friction = 2f;   //  Will make player not slide on surfaces
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.PLAYER;
        fixtureDef.filter.maskBits = GameInfo.DEFAULT | GameInfo.COLLECTABLE;
        /*
        fixtureDef.density - This is the mass of the body
        fixtureDef.friction - This will make the player not slide on surfaces. The player will have friction when he is
        walking on another body.
        fixtureDef.shape - This is the shape that I defined it earlier.
        fixtureDef.filter.categoryBits - It will define the category of this body
        fixtureDef.filter.maskBits - Mask bits will define with which categories can this body collide with. The body
        can collide with the default and the collectible category bit mask.
        If I don't type category bit mask and I don't set it like above by default it will be GameInfo.DEFAULT
        The player category is going to collide with collectable category
        Now that I have the fixture definition I can create the fixture itself.
        fixtureDef.filter.maskBits = GameInfo.DEFAULT | GameInfo.COLLECTABLE; - The player can collide only with the
        DEFAULT and COLLECTABLE category. It will not collide with DESTROYED category bits.
         */
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");
        shape.dispose();
        /*
        I can dispose the shape because I already assigned it to the fixture definition and if I keep it, it will
        fill up memory and I don't want that.
         */
    }

    public void movePlayer(float x) {
        /*
        This function takes only one parameter, X, because I want to move my player only on its X axis (left or right)
         */
        if(x<0 && !this.isFlipX()) {
            // moving left, flip the player to face the left direction
            this.flip(true, false);
        } else if(x>0 && this.isFlipX()) {
            // moving right, flip the player to face the right direction
            this.flip(true, false);
        }
        /*
        This if from above, is for the player to stay flipped. Without it, when I release key LEFT or RIGHT key the
        the player will flip to it's original side.
         */
        body.setLinearVelocity(x, body.getLinearVelocity().y);
        /*
        I don't want to change the linear velocity for the Y axis, I want to be the same with the body current velocity.
        I am only going to move it on the X axis, this is why I change only the X value.
         */
        isWalking = true;
    }

    public void drawPlayerIdle(SpriteBatch batch) {
        if(!isWalking) {
            batch.draw(this, getX() + getWidth() / 2f - 15, getY() - getHeight() / 2f);
            /*
            I can put "this" in "region" parameter because this class extends the class SPRITE.
            This function is going to draw the player.
            If the player is not walking draw the player idle animation.
             */
        }
    }

    public void drawPlayerAnimation(SpriteBatch batch) {
        if(isWalking) {
            /*
            If the player is walking then I will animate it.
             */
            elapsedTime += Gdx.graphics.getDeltaTime();

            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();

            for(TextureRegion frame : frames) {
                if(body.getLinearVelocity().x < 0 && !frame.isFlipX()) {
                    /*
                    This to flip the player on the other side. In order to flip to the other side, I need to call
                    isFlipX(). If the player is moving to the left side, x < 0, and the texture region is not flipped
                    then I will flip it.
                     */
                    frame.flip(true, false);
                    /*
                    frame.flip() is to flip the player. And I want to flip only the X axis, not Y.
                     */

                } else if(body.getLinearVelocity().x > 0 && frame.isFlipX()) {
                    frame.flip(true, false);
                    /*
                    The same thing. But now, I want to flip it on the other side.
                     */

                }
            }

            animation = new Animation<TextureRegion>(1f/10f, playerAtlas.getRegions());
            /*
            frameDuration - 1/10, 10 frames per second. That will slow down or speed up my player animation.
            playerAtlas.getRegions() is the regions from "Player Animation.png". There are those regions.
             */
            batch.draw(animation.getKeyFrame(elapsedTime, true),
                    getX() + getWidth() / 2f - 20, getY() - getHeight() / 2f);
            /*
            Using GDX graphics that gets delta time, adding that to elapsed time it will switch between the animations
            every time, so it will cycle those animations.
            The true parameter is for looping through animations and simply change them by using elapsed time that will
            cycle all of those animations. It will go to one frame, to another frame, to another. When is finishes all
            of the framers it will go again to the first frame, second, third and so on.
             */
        }
    }

    public void updatePlayer() {
        if(body.getLinearVelocity().x > 0) {
            // going right
            setPosition(body.getPosition().x * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
            /*
            I multiply with PPM because I divide earlier with PPM. This will not affect my 1 pixel / 100 meters ratio that
            I want to create. This is only to position the player correctly on the body.
            This function is going to update the player.
             */
        } else if(body.getLinearVelocity().x < 0) {
            // going left
            setPosition((body.getPosition().x - 0.3f) * GameInfo.PPM, body.getPosition().y * GameInfo.PPM);
            /*
            I multiply with PPM because I divide earlier with PPM. This will not affect my 1 pixel / 100 meters ratio that
            I want to create. This is only to position the player correctly on the body.
            This function is going to update the player.
             */
        }
    }

    public void setWalking(boolean isWalking) { // method to set the isWalking bboolean
        this.isWalking = isWalking;
    }

    public void setDead(boolean dead) { // method to set the dead boolean
        this.dead = dead;
    }

    public boolean isDead() { // method to check if the player died
        return dead;
    }

}
