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
package dk.dtu.compute.se.pisd.roborally.view;


import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 * Handles GUI stuff for the Space class
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mike Patrick Nørløv Andersen, s205417@student.dtu.dk
 * @author Sebastian  Andreas Almfort s163922@student.dtu.dk
 * @author Martin Koch, s182935@student.dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;

    Image[] imageArr;

    public SpaceView(@NotNull Space space, Image[] images) {
        this.space = space;
        imageArr = images.clone();
        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: #b3b1b1;");
        } else {
            this.setStyle("-fx-background-color: #9b9a9a;");
        }
        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
        try {
            drawAll();
        } catch (IllegalArgumentException e){
            System.out.println("duplicate drawing detected");
        }
    }
    
    public void drawAll(){
        gc.drawImage(imageArr[14],0,0);

        if(space.getActions().size() > 0){
            if(space.getActions().get(0).getClass().toString().contains("ConveyorBelt")){
                drawObject(space.x, space.y, ((ConveyorBelt) space.getActions().get(0)).getHeading().toString(), "square");
            }else if(space.getActions().get(0).getClass().toString().contains(("CheckPoint"))){
                drawObject(space.x, space.y, "NORTH", "circle");
            }else if(space.getActions().get(0).getClass().toString().contains(("Gear"))){
                drawObject(space.x, space.y, ((Gear) space.getActions().get(0)).getHeading().toString(), "triangle");
            }else if (space.getActions().get(0).getClass().toString().contains(("Pit"))){
                drawObject(space.x, space.y, "NORTH", "HexagonRed");
            }else if (space.getActions().get(0).getClass().toString().contains(("RebootToken"))){
                drawObject(space.x, space.y, "NORTH", "Hexagon");
            }else if (space.getActions().get(0).getClass().toString().contains("StartField")){
                startFieldDraw(((StartField) space.getActions().get(0)).number);
            }
        }
        if (!space.getWalls().isEmpty()){
            for (Heading wallPos : space.getWalls()){
                    drawObject(space.x, space.y, wallPos.toString(), "wall");
            }
        }
        this.getChildren().add(canvas);

        // Due to the implementation we have draw the player once more when he's standing on a field with actions, otherwise player is drawn underneath.
        if(space.getPlayer() != null){
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(space.getPlayer().getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }
            arrow.setRotate((90 * space.getPlayer().getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }
    void startFieldDraw(int number){
        switch (number) {
            case 0:
                gc.drawImage(imageArr[8],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                break;
            case 1:
                gc.drawImage(imageArr[9],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                break;
            case 2:
                gc.drawImage(imageArr[10],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                break;
            case 3:
                gc.drawImage(imageArr[11],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                break;
            case 4:
                gc.drawImage(imageArr[12],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                break;
            case 5:
                gc.drawImage(imageArr[13],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                break;
        }
    }
    void drawObject(int DrawAtX, int DrawAtY, String Heading, String typeOfDrawing) {
        if (space.x == DrawAtX && space.y == DrawAtY) {
            int checkpointNum;
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);
            gc.setLineCap(StrokeLineCap.ROUND);

            switch (Heading) {
                case "NORTH":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(60, 2, 0, 0);
                            space.hasWallNorth = true;
                            break;
                        case "circle":
                            checkpointNum = ((CheckPoint2) space.getActions().get(0)).n;
                            if(checkpointNum == 1){
                                gc.drawImage(imageArr[15],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            } else if(checkpointNum == 2){
                            gc.drawImage(imageArr[16],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            } else if(checkpointNum == 3){
                                gc.drawImage(imageArr[17],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            } else if(checkpointNum == 4){
                                gc.drawImage(imageArr[18],7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            }
                            break;
                        case "square":
                            gc.drawImage(imageArr[0],10,0,SPACE_WIDTH - 15,SPACE_HEIGHT);
                            break;
                        case "HexagonRed":
                            gc.drawImage(imageArr[7], 7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            break;
                        case "Hexagon":
                            gc.drawImage(imageArr[6], 7.5,7.5,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            break;
                    }
                    break;
                case "SOUTH":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                            space.hasWallSouth = true;
                            break;
                        case "square":
                            gc.drawImage(imageArr[3],10,0,SPACE_WIDTH - 15,SPACE_HEIGHT);
                            break;
                    }

                    break;
                case "EAST":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(65, SPACE_HEIGHT - 800, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                            space.hasWallEast = true;
                            break;
                        case "square":
                            gc.drawImage(imageArr[2],0,10,SPACE_WIDTH,SPACE_HEIGHT - 15);
                            break;
                        case "triangle":
                            gc.drawImage(imageArr[5],10,10,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            break;
                    }
                    break;
                case "WEST":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 65, SPACE_HEIGHT - 800);
                            space.hasWallWest = true;
                            break;
                        case "square":
                            gc.drawImage(imageArr[1],0,10,SPACE_WIDTH,SPACE_HEIGHT - 15);
                            break;
                        case "triangle":
                            gc.drawImage(imageArr[4],10,10,SPACE_WIDTH - 15,SPACE_HEIGHT - 15);
                            break;
                    }
                    break;
            }
        }
    }

}
