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
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Gear;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;


import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

        }
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
            }else if (space.getActions().get(0).getClass().toString().contains("StartField")){
                System.out.println(((StartField) space.getActions().get(0)).number);
                    startFieldDraw(((StartField) space.getActions().get(0)).number);
            }
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

        if (!space.getWalls().isEmpty()){
            for (Heading wallPos : space.getWalls()){
                    drawObject(space.x, space.y, wallPos.toString(), "wall");
            }
        }
    }
    void startFieldDraw(int number){
        BufferedImage StartField0 = null;
        BufferedImage StartField1 = null;
        BufferedImage StartField2 = null;
        BufferedImage StartField3 = null;
        BufferedImage StartField4 = null;
        BufferedImage StartField5 = null;

        try {
            StartField0 = ImageIO.read(this.getClass().getResource("/images/StartField0.png"));
            StartField1 = ImageIO.read(this.getClass().getResource("/images/StartField1.png"));
            StartField2 = ImageIO.read(this.getClass().getResource("/images/StartField2.png"));
            StartField3 = ImageIO.read(this.getClass().getResource("/images/StartField3.png"));
            StartField4 = ImageIO.read(this.getClass().getResource("/images/StartField4.png"));
            StartField5 = ImageIO.read(this.getClass().getResource("/images/StartField5.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image StartField0s = SwingFXUtils.toFXImage(StartField0, null);
        Image StartField1s = SwingFXUtils.toFXImage(StartField1, null);
        Image StartField2s = SwingFXUtils.toFXImage(StartField2, null);
        Image StartField3s = SwingFXUtils.toFXImage(StartField3, null);
        Image StartField4s = SwingFXUtils.toFXImage(StartField4, null);
        Image StartField5s = SwingFXUtils.toFXImage(StartField5, null);
        switch (number) {
            case 0:
                gc.drawImage(StartField0s,7.5,7.5,60,60);
                break;
            case 1:
                gc.drawImage(StartField1s,7.5,7.5,60,60 );
                break;
            case 2:
                gc.drawImage(StartField2s,7.5,7.5,60,60 );
                break;
            case 3:
                gc.drawImage(StartField3s,7.5,7.5,60,60 );
                break;
            case 4:
                gc.drawImage(StartField4s,7.5,7.5,60,60 );
                break;
            case 5:
                gc.drawImage(StartField5s,7.5,7.5,60,60 );
                break;
        }
        this.getChildren().add(canvas);
    }
//*******************************WALLS**********************************//
    void drawObject(int DrawAtX, int DrawAtY, String Heading, String typeOfDrawing) {
        if (space.x == DrawAtX && space.y == DrawAtY) {

            BufferedImage conImage = null;
            BufferedImage conImageLeft = null;
            BufferedImage conImageRight = null;
            BufferedImage conImageDown = null;
            BufferedImage gearImageLeft = null;
            BufferedImage gearImageRight = null;
            BufferedImage PitImage = null;
            BufferedImage RebootTokenImage = null;


            try {
                conImage = ImageIO.read(this.getClass().getResource("/images/concon.png"));
                conImageLeft = ImageIO.read(this.getClass().getResource("/images/conconLeft.png"));
                conImageRight = ImageIO.read(this.getClass().getResource("/images/conconRight.png"));
                conImageDown = ImageIO.read(this.getClass().getResource("/images/conconDown.png"));
                gearImageLeft = ImageIO.read(this.getClass().getResource("/images/gearLeft.png"));
                gearImageRight = ImageIO.read(this.getClass().getResource("/images/gearRight.png"));
                PitImage = ImageIO.read(this.getClass().getResource("/images/pit.png"));
                RebootTokenImage = ImageIO.read(this.getClass().getResource("/images/Reboot.png"));

            } catch (IOException e) {
                e.printStackTrace();
            }

            Image conveyorUp = SwingFXUtils.toFXImage(conImage, null);
            Image conveyorLeft = SwingFXUtils.toFXImage(conImageLeft, null);
            Image conveyorRight = SwingFXUtils.toFXImage(conImageRight, null);
            Image conveyorDown = SwingFXUtils.toFXImage(conImageDown, null);
            Image gearLeft = SwingFXUtils.toFXImage(gearImageLeft, null);
            Image gearRight = SwingFXUtils.toFXImage(gearImageRight, null);
            Image RebootToken = SwingFXUtils.toFXImage(RebootTokenImage, null);
            Image Pit = SwingFXUtils.toFXImage(PitImage, null);



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
                            gc.drawImage(conveyorUp,7.5,7.5,60,60);
                            break;
                        case "HexagonRed":
                            gc.drawImage(Pit, 7.5,7.5,60,60);
                            break;
                        case "Hexagon":
                            gc.drawImage(RebootToken, 7.5,7.5,60,60);
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
                            gc.drawImage(conveyorDown,7.5,7.5,60,60);
                            break;
                        case "Hexagon":
                            gc.drawImage(RebootToken, 7.5,7.5,60,60);
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
                            gc.drawImage(conveyorRight,7.5,7.5,60,60);
                            break;
                        case "Hexagon":
                            gc.drawImage(RebootToken, 7.5,7.5,60,60);
                            break;
                        case "HexagonRed":
                            gc.drawImage(Pit, 7.5,7.5,60,60);
                            break;
                        case "triangle":
                            gc.drawImage(gearRight,10,10,60,60);
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
                            gc.drawImage(conveyorLeft,7.5,7.5,60,60);
                            break;
                        case "Hexagon":
                            gc.drawImage(RebootToken, 7.5,7.5,60,60);
                            break;
                        case "HexagonRed":
                            gc.drawImage(Pit, 7.5,7.5,60,60);
                            break;
                        case "triangle":
                            gc.drawImage(gearLeft,10,10,60,60);
                            break;
                    }
                    break;
            }
            this.getChildren().add(canvas);
        }
    }


}
