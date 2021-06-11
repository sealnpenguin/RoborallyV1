package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class StartField extends FieldAction {
    public final int number;
    public StartField(int number){this.number = number;}

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return true;
    }

}
