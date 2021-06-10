package dk.dtu.compute.se.pisd.roborally.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CheckpointTest {
    @Test
    public void player_has_to_go_through_checkpoints_in_order(){
        Board board = new Board(8, 8);
        Player player1 = new Player(board, "yellow", "player1");
        player1.RecentCheckpoint(3);
        Assertions.assertEquals(0, player1.getRecentCheckpoint(),  player1.getName() + " should be 0");
        player1.RecentCheckpoint(1);
        Assertions.assertEquals(1, player1.getRecentCheckpoint(),  player1.getName() + " should be 1");
        player1.RecentCheckpoint(2);
        player1.RecentCheckpoint(1);
        Assertions.assertEquals(2, player1.getRecentCheckpoint(),  player1.getName() + " should be 2");
    }
    @Test
    public void player_wins_if_max_checkpoint_reached(){
        Board board = new Board(8, 8);
        Player player1 = new Player(board, "yellow", "player1");
        board.countupcheckpoint();
        board.countupcheckpoint();
        board.countupcheckpoint();
        board.countupcheckpoint();
        player1.RecentCheckpoint(1);
        player1.RecentCheckpoint(2);
        player1.RecentCheckpoint(3);
        player1.RecentCheckpoint(4);
        Assertions.assertEquals(board.getTotalCheckpoints(), player1.getRecentCheckpoint(),  player1.getName() + " should be 4");
    }

}