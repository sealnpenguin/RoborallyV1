package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class EnergyCubes extends FieldAction{

    private Heading heading = Heading.NORTH;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    boolean beenOn = true;

    @Override
    public boolean doAction(GameController gameController, Space space) {
        if(space != null){
            gameController.pushPlayer(space.getPlayer(), getHeading());
            /* Skal her teste hvilken iteration man er på.
            if(){
            }
            */
        }

        return true;
    }
}
