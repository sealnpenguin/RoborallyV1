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

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ...
 * The GameController interacts when a player do an action
 * int the GUI. It's the controlling part of the game.
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    private List<Space> startingSpot = new ArrayList<>();
    private List<Integer> startSpotInt = new ArrayList<>();
     public Board board;
    public Space RebootToken;


    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    //Testes for Pit og RebootToken
    public void moveCurrentPlayerToSpace(@NotNull Space space)  {
        Player player = this.board.getCurrentPlayer();
        //System.out.println(space.getPlayer());
        if(space.getPlayer() == null) {
            player.setSpace(space);
            board.increaseCounter();
            this.board.setCurrentPlayer(this.board.getPlayer(board.getCounter() % board.getPlayersNumber()));
            board.setCount(board.getCount() + 1);
        }
    }

    /**
     * ...
     * This is where we initilize all the elements on the board.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        RebootToken = board.findToken();

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    /**
     * Generates random commandcards
     * @return returns the randomly generated commandcard
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);

    }

    /**
     * updates when the player taps the finnish button
     * the execute button becomes active.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }
    /**
     * selectStartPosition gets all of available starting positions adding them to the startSpot array.
     *Also opens dialog box where player can choose the specific starting spot
     */
    public void selectStartPosition(){
        int k = 0;
        while(k < 6) {

            for (int i = 0; i < board.width; i++) {
                for (int j = 0; j < board.height; j++) {
                    try {
                        if (board.getSpace(i, j).getActions().get(0).getClass().toString().contains("StartField") && ((StartField) board.getSpace(i, j).getActions().get(0)).number == k) {
                            startingSpot.add(board.getSpace(i, j));
                            k++;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }

                }

            }
        }
        for (int i = 0; i < 6; i++) {
            startSpotInt.add(i);
        }
         for (int i = 0; i < board.getPlayersNumber(); i++) {
            ChoiceDialog<Integer> options = new ChoiceDialog<>(startSpotInt.get(0), startSpotInt);
            options.setTitle("Player number " + i+1);
            options.setHeaderText("Select starting space");
            Optional<Integer> result = options.showAndWait();

            board.getPlayer(i).setSpace(startingSpot.get(result.get()));
             startSpotInt.remove(options.getResult());
        }



    }

    /**
     * makeProgramFieldsVisible turns all of the players cards visible.
     *
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * makeProgramFieldsInvisible turns all of the players cards that is selected invisible so it only shows one
     * command to be performed at at time.
     *
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Makes the move the player request.
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    /**
     * executes all commandcards in all registers.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }


    /**
     * executeNextStep executes the commandcard in the current register.
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        Space current;
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    //Fires commandcard
                    executeCommand(currentPlayer, command);
                }

                //  Fires if activiation field
                current = currentPlayer.getSpace();
                if(current != null && current.getActions().size() != 0){
                    current.getActions().get(0).doAction(this,current);
                }

                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }
    /**
     *Execute the moves the players make when they press the execute button
     * it itterates through all players untill the players have no registers left to execute.
     */
    public void executeCommandOptionAndContinue(@NotNull Command option){
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null && board.getPhase() == Phase.PLAYER_INTERACTION && option != null){
            board.setPhase(Phase.ACTIVATION);
            executeCommand(currentPlayer, option);

            int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
            if (nextPlayerNumber < board.getPlayersNumber()) {
                board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            } else {
                int step = board.getStep() + 1;
                if (step < Player.NO_REGISTERS) {
                    makeProgramFieldsVisible(step);
                    board.setStep(step);
                    board.setCurrentPlayer(board.getPlayer(0));
                } else {
                    startProgrammingPhase();
                }
            }
        }
    }

    /**
     * ExecuteCommand allows for the execution of commands/commandcards.
     * @param player - player to execute the command on.
     * @param command - the command which should be executes.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD:
                    this.moveForward(player, player.getHeading());
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * The moveFoward function get the player and what way they are heading and then moves them one space forward if able.
     * In the case of a wall is blocking the path the player won't move.
     * In the case of another player blocking the path, the blocking player will then be moved in the direction of the player who pushes.
     * @param player - the player to move
     * @param heading - the heading to move the player
     */
    public void moveForward(@NotNull Player player, Heading heading) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            Space target = board.getNeighbour(current, heading);

            if(target != null){
                switch(heading){
                    case NORTH:
                        if(!current.hasWallNouth && !target.hasWallSouth){
                            if(target.getPlayer() != null){
                                moveForward(target.getPlayer(), player.getHeading());
                            }
                            if(target.getPlayer() == null){
                                player.setSpace(target);
                            }
                        } break;

                    case SOUTH:
                        if(!current.hasWallSouth && !target.hasWallNouth){
                            if(target.getPlayer() != null){
                                moveForward(target.getPlayer(), player.getHeading());
                            }
                            else if(target.getPlayer() == null){
                                player.setSpace(target);
                            }
                        } break;

                    case EAST:
                        if(!current.hasWallEast && !target.hasWallWest){
                            if(target.getPlayer() != null){
                                moveForward(target.getPlayer(), player.getHeading());
                            }
                            if(target.getPlayer() == null){
                                player.setSpace(target);
                            }
                        } break;

                    case WEST:
                        if(!current.hasWallWest && !target.hasWallEast){
                            if(target.getPlayer() != null){
                                moveForward(target.getPlayer(), player.getHeading());
                            }
                            if(target.getPlayer() == null){
                                player.setSpace(target);
                            }
                        } break;
                }
            } else {
                try {
                    if (RebootToken.getPlayer() != null) {
                        pushPlayer(RebootToken.getPlayer(), RebootToken.getPlayer().getHeading());
                        moveCurrentPlayerToSpace(RebootToken);
                        ((RebootToken) RebootToken.getActions().get(0)).destoyProgrammingCards(RebootToken.getPlayer());
                    } else {
                        current.getPlayer().setSpace(RebootToken);
                        ((RebootToken) RebootToken.getActions().get(0)).destoyProgrammingCards(RebootToken.getPlayer());
                    }
                } catch(NullPointerException e){
                    System.out.println("No RebootToken in this map");
                }
            }

        }
    }
    /**
     * This function allows the player to push the guy in front of him and afterward take his spot.
     * @param player - player to turn to the right.
     * @param pusher - pusher is the direction of which the pushed player should get moved.
     */
    public void pushPlayer(Player player, Heading pusher){
        Space playerToPushSpace = player.getSpace();
        Space PushTarget = board.getNeighbour(playerToPushSpace, pusher);
        player.setSpace(PushTarget);
    }

    /**
     * Calls the moveforward function twice.
     * @param player - player to move.
     *
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player, player.getHeading());
        // Execute if player step on a field action before they do next step.
        for (FieldAction action : player.getSpace().getActions()){
            action.doAction(this, player.getSpace());
        }
        moveForward(player, player.getHeading());
    }

    /**
     * Turns the player to the right.
     * @param player - player to turn to the right.
     *
     */
    public void turnRight(@NotNull Player player) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            player.setHeading(player.getHeading().next());
        }

    }

    /**
     * Turns the player to the left.
     * @param player - player to turn to the left.
     *
     */
    public void turnLeft(@NotNull Player player) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * The moveCards function allows the player to move his commandcards from one place to another.
     * @param source - source is the field which contains the card to move.
     * @param target - target is the field to which the card should be moved.
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    public void winInitialisation(Player player){

        Alert msg = new Alert(Alert.AlertType.INFORMATION, player.getName() + "\" you won.");
        msg.showAndWait();
    }


    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }



}
