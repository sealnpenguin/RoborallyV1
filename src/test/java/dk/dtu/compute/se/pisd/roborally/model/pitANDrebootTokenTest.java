package dk.dtu.compute.se.pisd.roborally.model;


import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
/**
 * @author Martin Koch, s182935@student.dtu.dk
 * @author Mike Patrick Nørlev Andersen, s205417@student.dtu.dk
 */
public class pitANDrebootTokenTest {
    Board board = new Board(8, 8);
    GameController gameController = new GameController(board);
    @Test
    public void Player_get_send_to_rebootToken(){
        Board board = gameController.board;
        Player player1 = new Player(board,"yellow","Carl");
        board.addPlayer(player1);

        gameController.board.setCurrentPlayer(player1);
        board.getSpace(2,2).getActions().add(new Pit());
        board.getSpace(5,5).getActions().add(new RebootToken());
        gameController.RebootToken = board.findToken();
        player1.setSpace(board.getSpace(2,1));
        player1.setHeading(Heading.SOUTH);
        Assertions.assertEquals(player1.getSpace(), board.getSpace(2,1));
        gameController.moveForward(player1, player1.getHeading());
        gameController.board.setPhase(Phase.ACTIVATION);
        gameController.executeStep();
        Assertions.assertEquals(board.getSpace(5,5), player1.getSpace());
    }
    @Test
    public void Player_already_on_rebootToken(){
        Board board = gameController.board;
        Player player1 = new Player(board, "yellow", "Car");
        Player player2 = new Player(board, "green", "Mike");
        board.addPlayer(player1);
        board.addPlayer(player2);
        gameController.board.setCurrentPlayer(player1);
        board.getSpace(2,2).getActions().add(new Pit());


        board.getSpace(5,5).getActions().add(new RebootToken());
        gameController.RebootToken = board.findToken();
        player1.setSpace(board.getSpace(2,1));
        player2.setSpace(board.getSpace(5,5));
        gameController.moveForward(player1, player2.getHeading());
        gameController.board.setPhase(Phase.ACTIVATION);
        gameController.executeStep();
        Assertions.assertEquals(board.getSpace(5,5), player1.getSpace());
        Assertions.assertEquals(board.getSpace(5,4), player2.getSpace());
    }


}