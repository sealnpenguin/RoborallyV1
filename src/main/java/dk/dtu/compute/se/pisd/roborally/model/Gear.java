package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;

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
