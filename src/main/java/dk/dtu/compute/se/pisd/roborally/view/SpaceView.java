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
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.model.Gear;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;


import java.awt.*;

/**
 * ...
 * Handles GUI stuff for the Space class
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    CheckPoint check = new CheckPoint();

    final public static int SPACE_HEIGHT = 75; // 60; // 75;
    final public static int SPACE_WIDTH = 75;  // 60; // 75;

    public final Space space;

    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
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
        drawAll();
        /*for (int i = 0; i < 8; i++) {
            DrawWall(i,0,"NORTH","wall");
            DrawWall(i,7,"SOUTH", "wall");
        }
        for (int i = 0; i < 8; i++) {
            DrawWall(0,i,"WEST", "wall");
            DrawWall(7,i,"EAST", "wall");
        }
        //DrawWall(0,0,"North");
        DrawWall(3,3,"SOUTH", "wall");*/


        // Hardcoded which spaces contain CheckPoints
        /*if ((space.x == 1 && space.y == 1) || (space.x == 4 && space.y == 7) || (space.x == 5 && space.y == 3)){
            check.createCheckPoint(gc);
            this.getChildren().add(canvas);
        }*/
    }
    
    public void drawAll(){
        if(space.getActions().size() > 0){
            if(space.getActions().get(0).getClass().toString().contains("ConveyorBelt")){
                drawObject(space.x, space.y, ((ConveyorBelt) space.getActions().get(0)).getHeading().toString(), "square");
            } else if(space.getActions().get(0).getClass().toString().contains(("CheckPoint"))){
                drawObject(space.x, space.y, "NORTH", "circle");
            } else if(space.getActions().get(0).getClass().toString().contains(("Gear"))){
                drawObject(space.x, space.y, ((Gear) space.getActions().get(0)).getHeading().toString(), "triangle");
            }else if (space.getActions().get(0).getClass().toString().contains(("Pit"))){
                drawObject(space.x, space.y, "NORTH", "HexagonRed");
            }else if (space.getActions().get(0).getClass().toString().contains(("RebootToken"))){
                drawObject(space.x, space.y, "NORTH", "Hexagon");
            }
        }
        if (!space.getWalls().isEmpty()){
            for (Heading wallPos : space.getWalls()){
                    drawObject(space.x, space.y, wallPos.toString(), "wall");
            }
        }
    }

//*******************************WALLS**********************************//
    void drawObject(int DrawAtX, int DrawAtY, String Heading, String typeOfDrawing) {
        if (space.x == DrawAtX && space.y == DrawAtY) {
            Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.RED);
            gc.setLineWidth(5);
            gc.setLineCap(StrokeLineCap.ROUND);


            switch (Heading) {
                case "NORTH":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(2, SPACE_HEIGHT - 74, SPACE_WIDTH - 2, SPACE_HEIGHT - 74);
                            space.hasWallNouth = true;
                            break;
                        case "circle":
                            gc.setStroke(Color.GRAY);
                            gc.strokeOval(3, 3, SPACE_WIDTH - 5, SPACE_HEIGHT - 5);
                            break;
                        case "square":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRect(20, 2, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "triangle":
                            gc.setFill(Color.CRIMSON);
                            // gc.fillPolygon(new double[]{0, 75, 37.5}, new double[]{75, 75, 0}, 3);}
                            gc.strokeRect(20, 2, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "HexagonRed":
                            gc.setStroke(Color.RED);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "Hexagon":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                    }
                    break;
                case "SOUTH":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                            space.hasWallSouth = true;
                            break;
                        case "circle":
                            gc.setStroke(Color.GRAY);
                            gc.strokeOval(3, 3, SPACE_WIDTH - 5, SPACE_HEIGHT - 5);
                            break;
                        case "square":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRect(20, 2, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "Hexagon":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                    }

                    break;
                case "EAST":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(65, SPACE_HEIGHT - 800, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                            space.hasWallEast = true;
                            break;
                        case "circle":
                            gc.setStroke(Color.GRAY);
                            gc.strokeOval(3, 3, SPACE_WIDTH - 5, SPACE_HEIGHT - 5);
                            break;
                        case "square":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRect(2, 20, SPACE_WIDTH - 5, SPACE_HEIGHT - 40);
                            break;
                        case "Hexagon":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "HexagonRed":
                            gc.setStroke(Color.RED);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "triangle":
                            gc.setStroke(Color.CRIMSON);
                            gc.strokePolygon(new double[]{0, 0, 75}, new double[]{0, 75, 37.5}, 3);
                            break;
                    }
                    break;
                case "WEST":
                    switch (typeOfDrawing) {
                        case "wall":
                            gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 65, SPACE_HEIGHT - 800);
                            space.hasWallWest = true;
                            break;
                        case "circle":
                            gc.setStroke(Color.GRAY);
                            gc.strokeOval(3, 3, SPACE_WIDTH - 5, SPACE_HEIGHT - 5);
                            break;
                        case "square":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRect(2, 20, SPACE_WIDTH - 5, SPACE_HEIGHT - 40);
                            break;
                        case "Hexagon":
                            gc.setStroke(Color.BLUE);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "HexagonRed":
                            gc.setStroke(Color.RED);
                            gc.strokeRoundRect(7, 7, 60, 60, SPACE_WIDTH - 40, SPACE_HEIGHT - 5);
                            break;
                        case "triangle":

                            gc.setStroke(Color.CRIMSON);
                            gc.strokePolygon(new double[]{75, 75, 0}, new double[]{75, 0, 37.5}, 3);


                            break;
                    }
                    break;
            }
            this.getChildren().add(canvas);
        }
    }


}
