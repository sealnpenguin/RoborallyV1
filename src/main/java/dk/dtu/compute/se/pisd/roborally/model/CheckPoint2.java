package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;


/**
 * Class to create checkpoints from 1 to x. When maximum checkpoint is reached by a player the game ends.
 * checkpoints are placed on the board.
 */

public class CheckPoint2 extends FieldAction {

    public final int n;

    public CheckPoint2(int n) { this.n =n; }


    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            player.RecentCheckpoint(this.n);
            if (player.getRecentCheckpoint() == gameController.board.getTotalCheckpoints()){
                System.out.println("You win");
            }
            //if player.getRecentCheckpoint ==  max checkpoint annoucnce winner eller noget???
            System.out.println(n);
        }
        return true;
    }
}
