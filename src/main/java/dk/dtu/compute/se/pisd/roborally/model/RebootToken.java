package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class RebootToken extends FieldAction{

    //public final int n;

    public RebootToken(){
    }

    @Override
    public boolean doAction(GameController gameController, Space space){
        Player player = space.getPlayer();
        //Skal fjerne de resterende kort i den pågældende runde

        return true;
    }
}
