package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mike Patrick Nørløv Andersen, s205417@student.dtu.dk
 * @author Martin Koch, s182935@student.dtu.dk
 * @author Sebastian  Andreas Almfort s163922@student.dtu.dk
 */
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

        Player player = space.getPlayer();
        if (gameController.RebootToken.getPlayer() != null){
            gameController.pushPlayer(gameController.RebootToken.getPlayer(), this.heading);
            gameController.movePlayerToSpace(gameController.RebootToken,player);
            ((RebootToken) gameController.RebootToken.getActions().get(0)).destoyProgrammingCards(gameController.RebootToken.getPlayer());
        }
        else{
            gameController.movePlayerToSpace(gameController.RebootToken,player);
            ((RebootToken) gameController.RebootToken.getActions().get(0)).destoyProgrammingCards(gameController.RebootToken.getPlayer());
        }
        return false;
    }
}
