package Clouds;

import Helpers.GameInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Cloud extends Sprite {

    private World world;
    private Body body;
    private String cloudName; // this is for every cloud name, I have 4 clouds images.

    private boolean drawLeft;

    public Cloud(World world, String cloudName) {
        super(new Texture("Clouds/" + cloudName + ".png"));
        this.world = world;
        this.cloudName = cloudName;
    }

    void createBody() {
        BodyDef bodyDef = new BodyDef(); // Body definition
        bodyDef.type = BodyDef.BodyType.StaticBody;
        /*
        Clouds will be a StaticBody because in this game, the clouds will be standing still in one place
         */
        bodyDef.position.set((getX() - 45) / GameInfo.PPM, getY() / GameInfo.PPM);
        /*
        I get the X and Y position of the sprite class and I am adding to that position the width of the cloud
         */
        body = world.createBody(bodyDef);
        /*
        First I defined the body, how I wanted to be (dynamic, static or kinematic) and its position, and now I can
        create the body.
         */
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2f - 25) / GameInfo.PPM, (getHeight() / 2f - 10) / GameInfo.PPM);
        /*
        I defined the body type and body's position and now I need to define the shape ( a polygon shape ). After I
        create the box, I set it asBox. In order to use this shape I need to assign it to a fixture and in order to
        assign it to a fixture I need to create a fixture definition.
         */
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        Fixture fixture = body.createFixture(fixtureDef);
        /*
        I defined the fixture and assign it a shape and after that I create the fixture (first I need to define and
        after that to create).
         */
        fixture.setUserData(cloudName);
        shape.dispose();
        /*
        Now that I assign the shape to the fixture (fixtureDef.shape = shape) I don't it anymore and I can dispose it.
         */
    }

    public void setSpritePosition(float x, float y) {
        setPosition(x, y);
        /*
        With this line of code I set the position of the Sprite because the position is used in createBody() function
        (getX(), getY()).
        */
        createBody();
    }

    // Getters and Setters
    public String getCloudName() {
        return this.cloudName;
    }

    public boolean getDrawLeft() {
        return drawLeft;
    }

    public void setDrawLeft(boolean drawLeft) {
        this.drawLeft = drawLeft;
    }
    // Getters and Setters

}
