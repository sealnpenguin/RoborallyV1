package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * @author Mike Patrick Nørlev Andersen, s205417@student.dtu.dk
 * @author Sebastian  Andreas Almfort s163922@student.dtu.dk
 */
public class RebootToken extends FieldAction{
    public RebootToken(){
    }

    @Override
    public boolean doAction(GameController gameController, Space space){
        return true;
    }

    public void destoyProgrammingCards(Player player){
        for (int i = 0; i < 5; i++) {
           player.getProgramField(i).setCard(null);
        }
    }
}
