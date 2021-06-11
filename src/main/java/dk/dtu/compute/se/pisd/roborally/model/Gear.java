package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.GameController;
/**
 * This class holds the information of what happens when a player lands on a gear-field.
 * The player get spun 90 degress to either left or right indicated by west and east.
 * Extends fieldAction so it is able to execute this specific field Action.
 */

public class Gear extends FieldAction {

public Heading heading;

public void setHeading(Heading heading) {this.heading = heading;}

public Heading getHeading(){return heading;}

public Gear(Heading heading){ this.heading = heading;}


    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();

        switch(heading){
            case WEST:
                gameController.turnLeft(player);
                break;

            case EAST:
                gameController.turnRight(player);
                break;
        }
        return true;
    }
}
