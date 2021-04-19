package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;




public class CheckPoint2 extends FieldAction {

    public final int n;

    public CheckPoint2(int n) { this.n =n; }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            player.RecentCheckpoint(this.n);
            //if player.getRecentCheckpoint ==  max checkpoint annoucnce winner eller noget???
        }
        return true;
    }
}
