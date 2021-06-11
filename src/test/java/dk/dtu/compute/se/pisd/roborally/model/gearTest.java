package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
/**
 * @author Martin Koch, s182935@student.dtu.dk
 */
public class gearTest {
    Board board = new Board(8, 8);
    GameController gameController = new GameController(board);
    @Test
    public void Gear(){
        Board board = gameController.board;

        Player player1 = new Player(board,"yellow", "Carl");
        board.addPlayer(player1);
        //Player player1 = new Player(board, "yellow", "player1");

        player1.setHeading(Heading.SOUTH);
        board.addGear(board.getSpace(4, 5).getActions().add(new Gear(Heading.WEST)));
        board.addGear(board.getSpace(4, 6).getActions().add(new Gear(Heading.EAST)));
        Assertions.assertEquals("SOUTH", player1.getHeading().toString());
        player1.setSpace(board.getSpace(4,5));
        Space current = player1.getSpace();
        current.getActions().get(0).doAction(gameController,current);
        Assertions.assertEquals("EAST", player1.getHeading().toString());
        player1.setSpace(board.getSpace(4,6));
        current = player1.getSpace();
        current.getActions().get(0).doAction(gameController,current);
        Assertions.assertEquals("SOUTH", player1.getHeading().toString());
    }


}