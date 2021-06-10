package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class RebootToken extends FieldAction{

    //public final int n;

    public RebootToken(){
    }

    @Override
    public boolean doAction(GameController gameController, Space space){
        return true;
    }

    public void destoyProgrammingCards(Player player){
        for (int i = 0; i < 5; i++) {
           player.getProgramField(i).setCard(null);
        }
    }
}
