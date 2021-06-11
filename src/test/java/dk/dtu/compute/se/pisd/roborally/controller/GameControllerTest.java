package dk.dtu.compute.se.pisd.roborally.controller;

import com.mysql.cj.protocol.x.XMessage;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.PlayerView;
import javafx.scene.control.Button;
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
        Player player = board.getCurrentPlayer();

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player, board.getSpace(0, 4).getPlayer(), "Player " + player.getName() + " should be Space (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), " Space (0,0) should be empty");
        Assertions.assertEquals(player, board.getCurrentPlayer(), "Current player should be " + player.getName());

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

    @Test
    public void player_push_player() {
        Board board = gameController.board;

        Player player1 = gameController.board.getPlayer(0);
        Player player2 = gameController.board.getPlayer(1);
        player1.setSpace(gameController.board.getSpace(3,0));
        player1.setHeading(Heading.SOUTH);
        player2.setSpace(gameController.board.getSpace(3,1));
        Assertions.assertEquals(player1, board.getSpace(3, 0).getPlayer(),   player1.getName() + " should be Space (3,0)!");
        Assertions.assertEquals(player2, board.getSpace(3, 1).getPlayer(),   player2.getName() + " should be Space (3,1)!");

        gameController.moveForward(player1, player1.getHeading());

        Assertions.assertEquals(player1, board.getSpace(3,1).getPlayer(), player1.getName() + " should be at space (3,1)!");
        Assertions.assertEquals(player2, board.getSpace(3,2).getPlayer(), player2.getName() + " should be at space (3,2)!");

        //should not be able to push player through wall

        board.getSpace(3,3).addWall(Heading.NORTH);

        gameController.moveForward(player1, player1.getHeading());
        Space playerCoord = player1.getSpace();
        Assertions.assertEquals(player1, board.getSpace(3,1).getPlayer(), player1.getName() + " should be at space (3,1)!" + player1.getName() + " is at " +  playerCoord.x + "," + playerCoord.y);
        Assertions.assertEquals(player2, board.getSpace(3,2).getPlayer(), player2.getName() + " should be at space (3,2)!" + player2.getName() + " is at " +  playerCoord.x + "," + playerCoord.y);

    }
    @Test
    public void moveCurrentPlayerToSpaceTest(){
        Board board = gameController.board;
        Player player1 = gameController.board.getPlayer(0);
        player1.setSpace(board.getSpace(4,5));
        Assertions.assertEquals(board.getSpace(4,5), player1.getSpace());
        gameController.movePlayerToSpace(board.getSpace(4,7), player1);
        Assertions.assertEquals(board.getSpace(4,7), player1.getSpace());
    }
    @Test
    public void differentPhasesTest(){
        Board board = gameController.board;
        Player player1 = gameController.board.getPlayer(0);
        player1.setHeading(Heading.SOUTH);
        player1.setSpace(board.getSpace(2, 4));
        gameController.board.setPhase(Phase.INITIALISATION);
        Assertions.assertEquals("INITIALISATION", gameController.board.getPhase().toString());
        gameController.startProgrammingPhase();
        Assertions.assertEquals("PROGRAMMING", gameController.board.getPhase().toString());
        gameController.finishProgrammingPhase();
        Assertions.assertEquals("ACTIVATION", gameController.board.getPhase().toString());
        //should test the last phase but we need to set a variable for player for it to work
        player1.getProgramField(0).setCard(new CommandCard(Command.OPTION_LEFT_RIGHT));
        gameController.executeStep();
        Assertions.assertEquals("PLAYER_INTERACTION", gameController.board.getPhase().toString());
        board.setPhase(Phase.ACTIVATION);

        player1.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        board.getSpace(2,5).getActions().add(new CheckPoint2(1));
        gameController.executeStep();


    }


    @Test
    public void Gear(){
        Board board = gameController.board;

        Player player1 = gameController.board.getPlayer(0);
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