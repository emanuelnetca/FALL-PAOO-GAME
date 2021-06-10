package Collectables;

import Helpers.GameInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Collectable extends Sprite {

    private World world;
    private Fixture fixture;
    private String name;
    /*
    String name; - I need to tag the collectibles so that we know if it's the life or the coin because when collide I
    need to check the name of that collectable in order to see if I actually have collided with the coin or the life
    in order to add coin to the player or to add lives.
     */
    private Body body;

    public Collectable(World world, String name) {
        super(new Texture("Collectables/" + name + ".png"));
        this.world = world;
        this.name = name;
    }

    void createCollectableBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        /*
        The collectables are not going to move. The collectable items are going to stay in one place and I am going to
        collect them.
         */
        bodyDef.position.set((getX() - getWidth() / 2f - 20) / GameInfo.PPM, (getY() + getWidth() / 2f) / GameInfo.PPM);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2f) / GameInfo.PPM, (getHeight() / 2f) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameInfo.COLLECTABLE;
        /*
        This body has the category of collectible, so the player will be able to collide with the collectible items
         */
        fixtureDef.isSensor = true;
        /*
        This isSensor it will make the collectable items to collide with the player and it will allow the player to
        pass through them.
         */

        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(name); // coin or life collectible

        shape.dispose(); // I dispose the shape because is added in fixtureDef
    }

    public void setCollectablesPosition(float x, float y) {
        setPosition(x, y);
        createCollectableBody();
    }

    public void updateCollectable() {
        setPosition(body.getPosition().x * GameInfo.PPM, (body.getPosition().y - 0.2f) * GameInfo.PPM);
    }

    public void changeFilter() {
        Filter filter = new Filter();
        filter.categoryBits = GameInfo.DESTROYED;
        fixture.setFilterData(filter); // I am passing the filter
        /*
        I am creating a new filter and assign the category bits and then simply call the fixture set filter data and I
        have the new filter which will now sai that the category bits for this body to destroyed.
         */
    }

    public Fixture getFixture() {
        return fixture;
    }
}
