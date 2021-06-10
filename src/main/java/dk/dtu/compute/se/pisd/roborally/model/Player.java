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

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {
    /**
     * Player instantiation method, which creates a player with a specific name and color
     * as well as direction and placing on the board
     * and a set of cards and registers?
     */
    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;
    private int recentCheckpoint;
    Boolean hasFinished;

    private String CardsNumber;
    private Space space;
    private Heading heading = SOUTH;

    private CommandCardField[] program;
    private CommandCardField[] cards;

    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;
        this.hasFinished = false;
        int checkPointStatus = 0;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    public Space getSpace() {
        return space;
    }


    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }
    //this gets updated whenever a player reaches a checkpoint one higher than what the player already has
    public void RecentCheckpoint(int recentCheckpoint){
        if (recentCheckpoint == (this.recentCheckpoint + 1)){
            this.recentCheckpoint = recentCheckpoint;
            notifyChange();
        }
    }
    public int getRecentCheckpoint(){
        return recentCheckpoint;
    }

    public void setRecentCheckpoint(int setValue){
        recentCheckpoint = setValue;
    }

    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public void sethasFinished(){
        this.hasFinished = true;
    }

    public Boolean gethasFinished(){
        return this.hasFinished;

    }

    public String CreateCardsNumber(){
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            if(getCardField(i).getCard() == null){
                values.add(0);
            }
            else if(getCardField(i).getCard().getName() == "Fwd"){
                values.add(1);
            }
            else if(getCardField(i).getCard().getName() == "Turn Right"){
                values.add(2);
            }
            else if(getCardField(i).getCard().getName() == "Turn Left"){
                values.add(3);
            }
            else if(getCardField(i).getCard().getName() == "Fast Fwd"){
                values.add(4);
            }
            else if(getCardField(i).getCard().getName() == "Left OR Right"){
                values.add(5);
            }
        }
        CardsNumber = values.toString();
        CardsNumber = CardsNumber.replace("[", "");
        CardsNumber = CardsNumber.replace("]", "");
        CardsNumber = CardsNumber.replace(", ", "");
        //System.out.println(CardsNumber + " Gemmer dette ");
        return CardsNumber;
    }

    public void setCardNumber(String newString){
        CardsNumber = newString;
    }

    public void loadCards(){
        for (int i = 0; i < CardsNumber.length(); i++) {
            switch (CardsNumber.charAt(i)) {
                case '0':
                    break;
                case '1':
                    getCardField(i).setCard(new CommandCard(Command.FORWARD));
                    break;
                case '2':
                    getCardField(i).setCard(new CommandCard(Command.RIGHT));
                    break;
                case '3':
                    getCardField(i).setCard(new CommandCard(Command.LEFT));
                    break;
                case '4':
                    getCardField(i).setCard(new CommandCard(Command.FAST_FORWARD));
                    break;
                case '5':
                    getCardField(i).setCard(new CommandCard(Command.OPTION_LEFT_RIGHT));
                    break;
            }

        }
    }

}
