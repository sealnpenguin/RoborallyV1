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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 * Creates board with spaces,and players. It extends Subject which is the observer that notifies the GUI when something is changed.
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */

public class Board extends Subject {
    /**
     * Board 
     */
    private int counter = 0;

    public final int width;

    public final int height;

    public String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private List<Boolean> checkpoints = new ArrayList<Boolean>();

    private List<Boolean> Gear = new ArrayList<Boolean>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    /**
     * Board constructor. Creates the board object with paremeters width and height.
     *
     * spaces: two dimensional array which contains the positions of the fields of the board.
     * @param width is how many fields/spaces there is on the on the x-axis of the board
     * @param height is how many fields/spaces there is on the the y-axis of the board
     * @param boardName the name of the board.
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
    }


    /**
     * Itterating through all the spaces to find the reboot token
     * @return the space with the reboot token otherwise returns null
     */
    public Space findToken(){
        for (int i = 0; i < this.width; i++){
            for (int j = 0; j < this.height; j++) {
                try {
                    if (getSpace(i, j).getActions().get(0).getClass().toString().contains("RebootToken")) {
                        return getSpace(i, j);
                    }
                }
                catch (IndexOutOfBoundsException e)  {

                }
            }
        }
        return null;
    }
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    /**
     * Function to get a space on the board.
     * @param x specifies the space on the horizontal axis
     * @param y specifies the space on the vertical axis
     * @return a specific space from the board
     */
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public int getPlayersNumber() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    /**
     * This function is used when you load a board from a file.
     * Whenever it encounters a checkpoint this function should get called and it adds a checkpoint to an array so the game knows
     * how many checkpoints there are and thefore able to find a winner
     */
    public void countupcheckpoint(){
        checkpoints.add(true);
    }
    public int getTotalCheckpoints(){
        return checkpoints.size();
    }


public void addGear(boolean Gear){
        this.Gear.add(Gear);
}

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                //y = (y + 1) % height;
                y = (y + 1);
                break;
            case WEST:
                //x = (x + width - 1) % width;
                x = (x - 1);
                break;
            case NORTH:
                //y = (y + height - 1) % height;
                y = (y - 1);
                break;
            case EAST:
                //x = (x + 1) % width;
                x = (x + 1);
                break;
        }

        return getSpace(x, y);
    }

    public int getCounter() {
        return counter;
    }

    public void increaseCounter(){
        this.counter++;
    }

    public String getStatusMessage() {
        // This is actually a view aspect, but for making the first task easy for
        // the students, this method gives a string representation of the current
        // status of the game

        String holdingstring = "";

        for (int i = 0; i < getPlayersNumber(); i++){
            holdingstring += getPlayer(i).getName()+ ": " + getPlayer(i).getRecentCheckpoint() + " ";
        }
        return holdingstring;
    }
    private int count;

    public int getCount() {
        return count;
    }

    public String getBoardName(){return boardName;}
    public void setCount(int count) {
        //only updates when it is necessary
        if (this.count != count) {
            this.count = count;
            notifyChange();
        }
    }



}
