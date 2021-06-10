package Helpers;

public class GameInfo {
    /*
    This is a class where is set values like width and height. It will help me in the development of the game and in
    case I ever have to change something ( like the width or/and the height ) then I can make everything simple and
    fast using this class.
     */

    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    public static final int PPM = 100; // pixel per meter
    /*
    LibGDX has a ratio of one by one ( one pixel = 1 meter ) and because of that, I will need a lot of force to push or
    move and object. That object it will be huge ( For example my player will be 125 meters tall, it will be huge ) and
    this is the reason why i set the ratio to be 1 / 100.
     */

    public static final short DEFAULT = 1;
    /*
    I need this because of the filters. The filter is the category mask and the big mask.
     */
    public static final short PLAYER = 2;
    public static final short COLLECTABLE = 4;
    public static final short DESTROYED = 6;

}
