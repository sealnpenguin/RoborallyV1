package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;


/**
 * Class to create checkpoints from 1 to x. When maximum checkpoint is reached by the game ends.
 * checkpoints are placed on the board.
 * Extends fieldAction so it is able to execute this specific field Action.
 *
 */

public class CheckPoint2 extends FieldAction {

    public final int n;

    public CheckPoint2(int n) { this.n =n; }

    /**
     * Doaction fires when a player step on a field with an action. This action counts up a variable
     * in the player object that determines whether or not its the right checkpoint the player picked up.
     * When the player reaches the maximum checkpoints they get announced as winner of the game.
     * @param gameController the gameController of the respective game
     * @param space The space of the given checkpoint
     * @return
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            player.RecentCheckpoint(this.n);
            if (player.getRecentCheckpoint() == gameController.board.getTotalCheckpoints()){
                System.out.println("You win");
            }
            System.out.println(n);
        }
        return true;
    }
}
