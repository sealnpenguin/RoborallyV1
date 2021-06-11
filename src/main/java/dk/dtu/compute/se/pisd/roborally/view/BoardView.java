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
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ...
 * The visualisation of the entire board and different parts.
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Mike Patrick Nørløv Andersen, s205417@student.dtu.dk
 */
public class BoardView extends VBox implements ViewObserver {

    private Board board;

    private GridPane mainBoardPane;
    private SpaceView[][] spaces;

    private PlayersView playersView;

    private Label statusLabel;

    private SpaceEventHandler spaceEventHandler;
    BufferedImage conImage,conImageLeft,conImageRight,conImageDown,gearImageLeft,gearImageRight,PitImage,RebootTokenImage,
            backgroundImage,StartField0,StartField1,StartField2,StartField3,StartField4,StartField5,checkPoint1,checkPoint2,checkPoint3,checkPoint4;

    Image conveyorUp, conveyorLeft, conveyorRight, conveyorDown, gearLeft, gearRight, RebootToken, Pit, StartField0s,
            StartField1s, StartField2s, StartField3s, StartField4s, StartField5s, background,checkPointImage1,checkPointImage2,checkPointImage3,checkPointImage4;

    public BoardView(@NotNull GameController gameController) {
        board = gameController.board;

        mainBoardPane = new GridPane();
        playersView = new PlayersView(gameController);
        statusLabel = new Label("<no status>");
        loadImages();
        Image[] images = new Image[]{conveyorUp, conveyorLeft, conveyorRight, conveyorDown, gearLeft, gearRight,
                RebootToken, Pit, StartField0s, StartField1s, StartField2s, StartField3s, StartField4s, StartField5s, background, checkPointImage1, checkPointImage2, checkPointImage3,checkPointImage4};

        this.getChildren().add(mainBoardPane);
        this.getChildren().add(playersView);
        this.getChildren().add(statusLabel);

        spaces = new SpaceView[board.width][board.height];

        spaceEventHandler = new SpaceEventHandler(gameController);

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                SpaceView spaceView = new SpaceView(space, images);
                spaces[x][y] = spaceView;
                mainBoardPane.add(spaceView, x, y);
                spaceView.setOnMouseClicked(spaceEventHandler);
            }
        }

        board.attach(this);
        update(board);
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == board) {
            Phase phase = board.getPhase();
            statusLabel.setText(board.getStatusMessage());

        }
    }

    // XXX this handler and its uses should eventually be deleted! This is just to help test the
    //     behaviour of the game by being able to explicitly move the players on the board!
    private class SpaceEventHandler implements EventHandler<MouseEvent> {

        final public GameController gameController;

        public SpaceEventHandler(@NotNull GameController gameController) {
            this.gameController = gameController;
        }

        @Override
        public void handle(MouseEvent event) {
            Object source = event.getSource();
            if (source instanceof SpaceView) {
                SpaceView spaceView = (SpaceView) source;
                Space space = spaceView.space;
                Board board = space.board;
                Player player = space.getPlayer();
                if (player != null && board == gameController.board) {
                    if (event.isShiftDown()) {
                        gameController.turnRight(player);
                    } else if (event.isControlDown()) {
                        gameController.turnLeft(player);
                    } else {
                        gameController.moveForward(player, player.getHeading());
                    }
                    event.consume();
                }
            }
        }

    }
    private void loadImages(){
        try {
            conImage = ImageIO.read(this.getClass().getResource("/images/concon.png"));
            conImageLeft = ImageIO.read(this.getClass().getResource("/images/conconLeft.png"));
            conImageRight = ImageIO.read(this.getClass().getResource("/images/conconRight.png"));
            conImageDown = ImageIO.read(this.getClass().getResource("/images/conconDown.png"));
            gearImageLeft = ImageIO.read(this.getClass().getResource("/images/gearLeft.png"));
            gearImageRight = ImageIO.read(this.getClass().getResource("/images/gearRight.png"));
            PitImage = ImageIO.read(this.getClass().getResource("/images/pit.png"));
            RebootTokenImage = ImageIO.read(this.getClass().getResource("/images/Reboot.png"));
            StartField0 = ImageIO.read(this.getClass().getResource("/images/StartField0.png"));
            StartField1 = ImageIO.read(this.getClass().getResource("/images/StartField1.png"));
            StartField2 = ImageIO.read(this.getClass().getResource("/images/StartField2.png"));
            StartField3 = ImageIO.read(this.getClass().getResource("/images/StartField3.png"));
            StartField4 = ImageIO.read(this.getClass().getResource("/images/StartField4.png"));
            StartField5 = ImageIO.read(this.getClass().getResource("/images/StartField5.png"));
            backgroundImage = ImageIO.read(this.getClass().getResource("/images/background.png"));
            checkPoint1 = ImageIO.read(this.getClass().getResource("/images/checkpoint1.png"));
            checkPoint2 = ImageIO.read(this.getClass().getResource("/images/checkpoint2.png"));
            checkPoint3 = ImageIO.read(this.getClass().getResource("/images/checkpoint3.png"));
            checkPoint4 = ImageIO.read(this.getClass().getResource("/images/checkpoint4.png"));



            conveyorUp = SwingFXUtils.toFXImage(conImage, null);
            conveyorLeft = SwingFXUtils.toFXImage(conImageLeft, null);
            conveyorRight = SwingFXUtils.toFXImage(conImageRight, null);
            conveyorDown = SwingFXUtils.toFXImage(conImageDown, null);
            gearLeft = SwingFXUtils.toFXImage(gearImageLeft, null);
            gearRight = SwingFXUtils.toFXImage(gearImageRight, null);
            RebootToken = SwingFXUtils.toFXImage(RebootTokenImage, null);
            Pit = SwingFXUtils.toFXImage(PitImage, null);
            StartField0s = SwingFXUtils.toFXImage(StartField0, null);
            StartField1s = SwingFXUtils.toFXImage(StartField1, null);
            StartField2s = SwingFXUtils.toFXImage(StartField2, null);
            StartField3s = SwingFXUtils.toFXImage(StartField3, null);
            StartField4s = SwingFXUtils.toFXImage(StartField4, null);
            StartField5s = SwingFXUtils.toFXImage(StartField5, null);
            background = SwingFXUtils.toFXImage(backgroundImage, null);
            checkPointImage1 = SwingFXUtils.toFXImage(checkPoint1, null);
            checkPointImage2 = SwingFXUtils.toFXImage(checkPoint2, null);
            checkPointImage3 = SwingFXUtils.toFXImage(checkPoint3, null);
            checkPointImage4 = SwingFXUtils.toFXImage(checkPoint4, null);

        } catch (IOException e) {
            System.out.println("Unable to load images");
            e.printStackTrace();
        }
    }
}
