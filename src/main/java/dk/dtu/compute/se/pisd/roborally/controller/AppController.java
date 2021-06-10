/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.CheckPoint2;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
// Denne er udkommenteret. Ikke sikker på den skal være det.
//import jdk.jfr.internal.Repository;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

/**
 * ...
 *controller that creates all the necessary things to run the RoboRally game.
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<String> LOAD_OPTIONS = new ArrayList<>();
    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
    final private List<String> Map_OPTIONS =Arrays.asList("defaultboard", "NYBOARD","EXTRA CRISPY");

    final private RoboRally roboRally;
    private Board board;
    private BoardView boardView = null;
    private GameController gameController;

    //RepositoryAccess iRepository;
    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        this.board = null;
        this.boardView = null;
        this.gameController = null;
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            // her fra
            /* choose board */
            ChoiceDialog<String> mapDialog = new ChoiceDialog<String>(Map_OPTIONS.get(0), Map_OPTIONS);
            mapDialog.setTitle("Maps");
            mapDialog.setHeaderText("Select a map");
             mapDialog.showAndWait(); //necessary .showAndWait() to get result


            // hertil

            board = LoadBoard.loadBoard(mapDialog.getResult());
            board.boardName = mapDialog.getResult();
            System.out.println(board.getBoardName());
            gameController = new GameController(board);
            int no = result.get();

            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
            }

            countUpCheckPoints();
            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
            gameController.selectStartPosition();
        }
    }

    /**
     * Saves the game state and map into the local database.
     * Requries a database to work.
     */

    public void saveGame() {
        // XXX needs to be implemented eventually
        //iRepository.
        //Anne Sophie - If the game has been saved before, it will be saved by the same gameID (same place)
        try {
            RepositoryAccess.getRepository().updateGameInDB(gameController.board);
            LoadBoard.saveBoard(gameController.board,gameController.board.getBoardName());
            System.out.println("Updating save");
        } catch (Exception e) {
            TextInputDialog td = new TextInputDialog("Name your save");
            td.showAndWait();
            String filename = td.getResult();
            filename += " (" + gameController.board.getBoardName() + ")";
            RepositoryAccess.getRepository().createGameInDB(gameController.board, filename);
            LoadBoard.saveBoard(gameController.board,filename);
            System.out.println("Saving");
        }
    }

    /**
     * LoadGame gets the saved games from the local database.
     */
    public void loadGame() {
        // Anne Sophie - Can only load the first six saved games (hardcoded)
        if (gameController == null) {
            for (int i = 0; i < RepositoryAccess.getRepository().getGames().size(); i++) {
                LOAD_OPTIONS.add(i+": " + RepositoryAccess.getRepository().getGames().get(i).name);
            }
            ChoiceDialog<String> loadDialog = new ChoiceDialog<String>(LOAD_OPTIONS.get(0), LOAD_OPTIONS);
            loadDialog.setTitle("Saved games");
            loadDialog.setHeaderText("Select saved game");
            Optional<String> result = loadDialog.showAndWait(); //necessary .showAndWait() to get result
            try {
                System.out.println(LOAD_OPTIONS.indexOf(loadDialog.getResult()) + " Loader denne save");
                gameController = new GameController(RepositoryAccess.getRepository().loadGameFromDB(LOAD_OPTIONS.indexOf(loadDialog.getResult())));
                roboRally.createBoardView(gameController);
                 board = gameController.board;
            }
            catch(Exception e) {
                System.out.println("No saved game! Try a new one.");
                e.printStackTrace();
                newGame();
            }
            countUpCheckPoints();

        } else {
            //System.out.println(RepositoryAccess.getRepository().getGames());
            //RepositoryAccess.getRepository().loadGameFromDB(0);
        }

    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }


    /**
     * Option to exit the game
     * It will always ask the user if they want to save the game if they choose "ok" to exiting the game.
     */
    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    public void countUpCheckPoints(){
        for (int i = 0; i <board.width; i++) {
            for (int j = 0; j < board.height; j++) {
                for (int k = 0; k < board.getSpace(i, j).getActions().size(); k++) {
                    if(board.getSpace(i, j).getActions().get(k).getClass().toString().contains("CheckPoint2")){
                        board.countupcheckpoint();
                    }
                }
            }
        }
    }

}
