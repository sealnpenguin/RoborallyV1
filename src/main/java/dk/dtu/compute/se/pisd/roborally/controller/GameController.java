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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 * The GameController interacts when a player do an action
 * int the GUI. It's the controlling part of the game.
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;
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
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved


        Player player = this.board.getCurrentPlayer();
        System.out.println(space.getPlayer());
        if(space.getPlayer() == null) {
            player.setSpace(space);
            board.increaseCounter();
            this.board.setCurrentPlayer(this.board.getPlayer(board.getCounter() % board.getPlayersNumber()));
            board.setCount(board.getCount() + 1);
        }
    }

    // XXX: V2
    /**
     * ...
     * This is where we initilize all the elements on the board. This
     * is just tempoary because we should be able to load the elements from a file at some point.
     */
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        /**
         * Definer where on the board, the specific fields shall be placed
         */
        // SKAL IKKE VÆRE HER! BARE MIDLERTIDIG.

        /*if (board.getTotalCheckpoints() < 1) {
        board.getSpace(1,1).getActions().add(new ConveyorBelt());
        board.getSpace(3,1).getActions().add(new ConveyorBelt());

        board.getSpace(4, 2).addWall(Heading.SOUTH);
        board.getSpace(5,2).addWall(Heading.NORTH);

        board.addGear(board.getSpace(4, 5).getActions().add(new Gear(Heading.WEST)));
        board.addGear(board.getSpace(4, 6).getActions().add(new Gear(Heading.EAST)));


        board.getSpace(6,6).getActions().add(new RebootToken());

        board.getSpace(1,0).getActions().add(new Pit());
            RebootToken = board.findToken();

            board.addCheckpoint(board.getSpace(0, 1).getActions().add(new CheckPoint2(1)));
            board.addCheckpoint(board.getSpace(0, 2).getActions().add(new CheckPoint2(2)));

        }*/

        RebootToken = board.findToken();
        // SKAL IKKE VÆRE HER! BARE MIDLERTIDIG.

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

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);

    }

    // XXX: V2
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

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2
    /**
     * Makes the move the player request.
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }


    // XXX: V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        Space current = currentPlayer.getSpace();

         // SKAL IKKE VÆRE HER! BARE MIDLERTIDIG.

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
                // SKAL IKKE VÆRE HER! BARE MIDLERTIDIG. Fires if activiation field
                current = currentPlayer.getSpace();
                if(current.getActions().size() != 0){
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

    // XXX: V2
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

    // TODO Assignment V2
    /**
     * The moveFoward function get the player and what way they are heading and then moves them one space forward.
     */
    public void moveForward(@NotNull Player player, Heading heading) {
        //heading = Heading.NORTH;


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
                //if(target.getPlayer() != null) moveForward(target.getPlayer(), player.getHeading());
                try {
                    if (RebootToken.getPlayer() != null) {
                        pushPlayer(RebootToken.getPlayer(), RebootToken.getPlayer().getHeading());
                        moveCurrentPlayerToSpace(RebootToken);
                        ((RebootToken) RebootToken.getActions().get(0)).destoyProgrammingCards(RebootToken.getPlayer());
                    } else {
                        //Space space = board.getSpace(target.x, target.y);
                        current.getPlayer().setSpace(RebootToken);
                        //pushPlayer(current.getPlayer(), current.getPlayer().getHeading());
                        //moveCurrentPlayerToSpace(RebootToken);
                        ((RebootToken) RebootToken.getActions().get(0)).destoyProgrammingCards(RebootToken.getPlayer());
                    }
                } catch(NullPointerException e){
                    System.out.println("No RebootToken in this map");
                }
            }

        }
    }
    public void pushPlayer(Player player, Heading pusher){
        Space playerToPushSpace = player.getSpace();
        Space PushTarget = board.getNeighbour(playerToPushSpace, pusher);
        player.setSpace(PushTarget);
    }

    // TODO Assignment V2
    /**
     * Calls the moveforward function twice.
     */
    public void fastForward(@NotNull Player player) {
        moveForward(player, player.getHeading());
        moveForward(player, player.getHeading());
    }

    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            player.setHeading(player.getHeading().next());
        }

    }

    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        Space current = player.getSpace();
        if (current != null && player.board == current.board) {
            player.setHeading(player.getHeading().prev());
        }
    }

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

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
