package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.jetbrains.annotations.NotNull;

public abstract class FieldAction {

    public abstract boolean doAction(GameController gameController, Space space);

}
/*
public class ConveyrBelt extends FieldAction{

    private Heading heading;

    //...

    /*
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space){


    }
     */
/*
}
*/

    /*
    final public Board board;

    Player player;

    public ActivationCards(Board board) {
        this.board = board;
    }

    public void greenConveyor(@NotNull String boardName){

        /*Tester placering af spiller i board. Så hvis denne bliver kaldt,
        * er det fordi der står en spiller på feltet. Så det gælder bare om at rykke spilleren */

    /*
        Space current = player.getSpace();
        if (player.board == current.board) {
            Space target = board.getNeighbour(current, player.getHeading());
            if(target != null && target.getPlayer() == null){
                //Her skal bestemmes hvad retning spiller skal rykkes.
                player.setSpace(target);
            }
            else{
                //funktion til at skubbe andre
                Player playerToPush = board.getSpace(target.x, target.y).getPlayer();
                pushPlayer(playerToPush, player.getHeading());
                //Her skal bestemmes hvad retning spiller skal rykkes.
                player.setSpace(target);
            }
        }
    }

    //Skal kigges igennem igen
    public void blueConveyor(@NotNull Player player){
        greenConveyor(player);
        greenConveyor(player);
    }

    public void GearsRight(@NotNull Player player){
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            player.setHeading(player.getHeading().next());
        }
    }

    public void GearsLeft(@NotNull Player player) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    private void pushPlayer(Player playerToPush, Heading heading) {
        Space playerToPushSpace = player.getSpace();
        Heading pusher;
        Space PushTarget = board.getNeighbour(playerToPushSpace, pusher);
        player.setSpace(PushTarget);
    }


    public void BoardLaser(@NotNull Player player){

    }
    */

