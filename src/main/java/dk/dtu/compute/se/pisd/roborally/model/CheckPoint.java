package dk.dtu.compute.se.pisd.roborally.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import static dk.dtu.compute.se.pisd.roborally.view.SpaceView.SPACE_HEIGHT;
import static dk.dtu.compute.se.pisd.roborally.view.SpaceView.SPACE_WIDTH;

public class CheckPoint extends StackPane {

    /**
     * createCheckPoint draws a new checkpoint graphic each time it is called from SpaceView
     * @param gc;
     */
    public void createCheckPoint(GraphicsContext gc) {
        gc.setStroke(Color.GREY);
        gc.setLineWidth(3);
        gc.strokeOval(3, 3, SPACE_WIDTH - 5, SPACE_HEIGHT - 5);
    }

    public void checkPointID() {

    }
}
