package Clouds;

import Collectables.Collectable;
import Helpers.GameInfo;
import Helpers.GameManager;
import Player.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;


public class CloudsController {

    private World world;

    private Array<Cloud> clouds = new Array<Cloud>();
    private Array<Collectable> collectables = new Array<Collectable>();
    /*
    This array from libGDX are not fixed size. I can add or remove elements from them very easy.
     */

    private final float DISTANCE_BETWEEN_CLOUDS = 350f;
    /*
    This is dictance between clouds. First cloud will be at position of Y = 0, the second at Y = 250, third at Y = 500
    and so on. An if I need to change the value, I can change it right here without too much effort.
     */
    private float minX, maxX;
    private float lastCloudPositionY;
    private float cameraY;

    private Random random = new Random();

    public CloudsController(World world) {
        this.world = world;
        minX = GameInfo.WIDTH / 2f - 110;
        maxX = GameInfo.WIDTH / 2f + 110;
        /*
        GameInfo.WIDTH is the middle of the X axis, and -100 will be a point on the left side and +110 a point on the
        right side of the middle.
         */
        createClouds();
        positionClouds(true);
        /*
        I set this boolean to be true because this is the first time when I am arranging the clouds
         */
    }

    void createClouds() {
        /*
        This method is going to create  my clouds and put them inside this array (private Array<Cloud> clouds)
        I am going to have 2 dark clouds and 4 other clouds that my player can stand on and move on
         */
        for(int i = 0; i < 2; ++i) {
            /*
            I put i < 2 because every time I call this create function I am going to create 2 dark clouds
             */
            clouds.add(new Cloud(world, "Dark Cloud"));
        }


        int index = 1; // this index is for clouds, because I have Cloud 1, Cloud 2, ...
        for(int i = 0; i < 6; ++i) {
            /*
            I put i < 6 because every time I call this create function I am going to create 6 regular clouds
            2 * Cloud 1, 2 * Cloud 2, 2 * Cloud 3
             */
            clouds.add(new Cloud(world, "Cloud " + index));
//            index++;
//            if(index == 4) {
//                index = 1;
//            }
            /*
            The index will allow me to compose strings. Cloud 1, Cloud 2, Cloud 3, Cloud 1, Cloud 2, Cloud 3. In my
            assets/clouds folder, I have more than 1 cloud and that is more simple.
             */
        }
        /*
        After this two for loop the array will have DARK CLOUD, DARK CLOUD, CLOUD 1, CLOUD 2, CLOUD 3, CLOUD 1,
        CLOUD 2, CLOUD 3. I don't want to be in some order, so that's the reason why I SHUFFLE the elements.
         */
        clouds.shuffle();
    }

    public void positionClouds(boolean firstTimeArranging) {

        while(clouds.get(0).getCloudName() == "Dark Cloud" || clouds.get(1).getCloudName() == "Dark Cloud") {
            /*
            If the first cloud is a dark one, then the player will die instantly and I don't want that, so I reshuffle
            them till the first cloud is not dark.
            I rearrange because I don't want the second cloud be dark neither.
             */
            clouds.shuffle();

        }

        float positionY = 0;
        if(firstTimeArranging) {
            positionY = GameInfo.HEIGHT / 2f;
            /*
            I use this "GameInfo.HEIGHT / 2f" position for the cloud position on Y axis when its the first arranging
             */
        } else {
            positionY = lastCloudPositionY;
        }

        int controlX = 0;

        for(Cloud c : clouds) { // for each cloud in clouds
            if(c.getX() == 0 && c.getY() == 0) {
                /*
                When I create the clouds by default its position it's (0, 0)
                 */
                float tempX = 0;
                if (controlX == 0) {
                    tempX = randomBetweenNumbers(maxX - 60, maxX);
                    controlX = 1;
                    c.setDrawLeft(false);
                    /*
                    Here I set the cloud to the right side because the maximum is on the right side and setDrawLeft
                    is false
                     */
                } else if (controlX == 1) {
                    tempX = randomBetweenNumbers(minX + 60, minX);
                    controlX = 0;
                    c.setDrawLeft(true);
                    /*
                    Here I set the cloud to the left side because the minimum is on the right side and setDrawLeft
                    is true
                     */
                }
                /*
                This if - else if, will random put the clouds one in one side, the next one in the other side
                That controlX is because at very loop I want one cloud to be on the left side the next one on the right side
                and so on. left - right - left - etc.
                maxX/minx -/+ 60, on the X axis, will put my cloud in a range (minX, minX+60), (maxX-60, maxX)
                mixX...minX + 60...Center of X...maxX - 60...maxX
                 */
                c.setSpritePosition(tempX, positionY);
                positionY -= DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;
                /*
                DISTANCE_BETWEEN_CLOUDS is explained where its declared. I subtract the distance because the camera is
                going down, and down Y is negative.
                lastCloudPositionY is the next position where I should spawn the next cloud.
                 */

                if(!firstTimeArranging && c.getCloudName() != "Dark Cloud") {
                /*
                If this in not the first time when I am arranging the clouds and if the cloud is not the dark one,
                because I don't want to spawn the collectible items above a dark cloud. I am checking if I am not
                arranging for the first time because it can happen that the collectables items to be spawn on the first
                cloud.
                 */
                    int rand = random.nextInt(10); // a random number from 0 up to 9
                    if(rand > 5) {
                    /*
                    If I don't have this logic I will spawn the collectable items on every single cloud.
                    if(rand > 5) - The higher the number the less items will be spawn. The lower the number, it will
                    spawn collectible items more often.
                     */
                        int randomCollectable = random.nextInt(2); // a random number from 0 to 1
                        if(randomCollectable == 0) {
                            // spawn a life, if the life count is lower than 2.
                            if (GameManager.getInstance().lifeScore < 2) {
                                // If the lifeScore is less than 2 then I am going to spawn a life.
                                Collectable collectable = new Collectable(world, "Life");
                                collectable.setCollectablesPosition(c.getX(), c.getY() + 40);
                                collectables.add(collectable);
                            } else {
                                // If the player already have 2 lives, than I am going to spawn a coin instead of a life
                                Collectable collectable = new Collectable(world, "Coin");
                                collectable.setCollectablesPosition(c.getX(), c.getY() + 40);
                                collectables.add(collectable);
                            }
                        } else {
                            // spawn a coin
                            Collectable collectable = new Collectable(world, "Coin");
                            collectable.setCollectablesPosition(c.getX(), c.getY() + 40);
                            collectables.add(collectable);
                        }
                    }
                }
            }
        }
    }

    public void drawClouds(SpriteBatch batch) {
        /*
        After I set the position of the clouds (positionCloud()) now I need to draw them and exactly what this
        function does.
         */
        for(Cloud c : clouds) {
            if(c.getDrawLeft()) {
                batch.draw(c, c.getX() - c.getWidth() / 2f - 20, c.getY() - c.getHeight() / 2f);
            } else {
                batch.draw(c, c.getX() - c.getWidth() / 2f + 10, c.getY() - c.getHeight() / 2f);
            }
        }
    }

    public void drawCollectables(SpriteBatch batch) {
        for(Collectable c : collectables) {
            c.updateCollectable();
            batch.draw(c, c.getX(), c.getY());
        }
        // This is for drawing out collectable items
    }

    public void removeCollectables() {
        for(int i = 0; i < collectables.size; ++i) {
            if(collectables.get(i).getFixture().getUserData() == "Remove") {
                /*
                If the user data of the fixture is equal to "Remove" I am going to remove that fixture. In order to do
                that first I am going to change the filter (categoryBits in Collectable class) and inform the physics
                world that I am not going to collide with collectables anymore. This is why I created "DESTROYED" in
                GameInfo.
                 */
                collectables.get(i).changeFilter();
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }

    public void createAndArrangeNewClouds() {
        for(int i = 0; i < clouds.size; ++i) {
            if((clouds.get(i).getY() - GameInfo.HEIGHT / 2f - 20) > cameraY) {
                /*
                If the condition fi true then the the cloud is out of bounds, I need to delete it.
                That -20 is to be sure that the cloud is completely out of bounds when we remove it
                 */
                clouds.get(i).getTexture().dispose();
                /*
                Now I dispose the texture that is at element [i] with dispose function
                 */
                clouds.removeIndex(i);
                /*
                Now, I remove the element from array
                 */
            }
        }
        if(clouds.size == 4) {
            /*
            I have an array with 6 elements. When the last cloud in my array will show up, then 2 clouds will be out
            of bounds and that means, that the size of the array will be 4
             */
            createClouds();
            positionClouds(false);
        }
    }

    public void removeOffScreenCollectables() {
        for(int i = 0; i < collectables.size; ++i) {
            if((collectables.get(i).getY() - GameInfo.HEIGHT / 2f - 50) > cameraY) {
                /*
                The same thing with clouds (the function above)
                 */
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
                System.out.println("The collectable has been removed");
            }
        }
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    public Player positionThePlayer(Player player) {
        player = new Player(world, clouds.get(0).getX(), clouds.get(0).getY() + 78);
        /*
        With this I set the position of the player on top of the first cloud when the game starts. I am adding 100
        because I want to position the player above the cloud.
         */
        return player;
    }

    private float randomBetweenNumbers(float min, float max) {
        return random.nextFloat() * (max - min) + min;
        /*
        nextFloat() returns the next float number between 0.0 and 1.0
        random.nextFloat() * (max - min) + min - It will return a random number between MIN and MAX
         */
    }

}
