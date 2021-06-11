package dk.dtu.compute.se.pisd.roborally.controller;

import javafx.application.Application;
import javafx.stage.Stage;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;
    //final Stage primaryStage = new Stage();



    private GameController Appcontroller;

    @Test
    void newGame() {
        Application.launch();

    }
}