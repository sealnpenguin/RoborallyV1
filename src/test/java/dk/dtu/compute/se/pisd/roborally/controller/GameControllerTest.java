package dk.dtu.compute.se.pisd.roborally.controller;

import com.mysql.cj.protocol.x.XMessage;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(1,i));
            player.setHeading(Heading.values()[(i + 1) % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /*@Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should be Space (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), " Space (0,0) should be empty");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName());

    }*/


    @Test
    void moveForward() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();

        Assertions.assertEquals(player, board.getSpace(1, 0).getPlayer(), "Player " + player.getName() + " should be Space (1,0)!");

        gameController.moveForward(player, player.getHeading());

        Assertions.assertEquals(player, board.getSpace(0, 0).getPlayer(), "Player " + player.getName() + " should be Space (0,1)!");
        Assertions.assertNull(board.getSpace(1, 0).getPlayer(), " Space (1,0) should be empty");
        Assertions.assertEquals(Heading.WEST,player.getHeading() , "Player should head WEST ");

    }

    @Test
    void turnRight(){
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        Assertions.assertEquals(Heading.WEST, player.getHeading(), "Player " + player.getName() + " should be heading WEST");
        gameController.turnRight(player);
        Assertions.assertEquals(Heading.NORTH, player.getHeading(), "Player " + player.getName() + " should be heading NORTH");


    }

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        Assertions.assertEquals(Heading.WEST, player.getHeading(), "Player " + player.getName() + " should be heading WEST");
        gameController.turnLeft(player);
        Assertions.assertEquals(Heading.SOUTH, player.getHeading(), "Player " + player.getName() + " should be heading SOUTH");


    }
}