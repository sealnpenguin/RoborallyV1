package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;
import dk.dtu.compute.se.pisd.roborally.model.Heading;


public class Pit extends FieldAction{

    private Heading heading = Heading.NORTH;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    @Override
    public boolean doAction(@NotNull GameController gameController,@NotNull Space space){
        gameController.moveCurrentPlayerToSpace(gameController.board.getSpace(6, 6));

        if(space.getPlayer() != null){
            gameController.moveCurrentPlayerToSpace(gameController.board.getSpace(6, 6));

        } else {
            gameController.pushPlayer(space.getPlayer(), this.getHeading());
        }

        return false;
    }
}
