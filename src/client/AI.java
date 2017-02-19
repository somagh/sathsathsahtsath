package client;

import client.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * AI class.
 * You should fill body of the method {@link #doTurn}.
 * Do not change name or modifiers of the methods or fields
 * and do not add constructor for this class.
 * You can add as many methods or fields as you want!
 * Use world parameter to access and modify game's
 * world!
 * See World interface for more details.
 */
public class AI {

    public void doTurn(World game) {
        // fill this method, we've presented a stupid AI for example!
        Random rand = new Random();


        Cell[][] cells = game.getMap().getCells();
        ArrayList<Beetle> beetles = new ArrayList<>();
        for(int i = 0; i < cells.length; i++)
            for(int j = 0; j < cells[i].length; j++)
                if(cells[i][j].getBeetleEntity() != null)
                    beetles.add((Beetle) cells[i][j].getBeetleEntity());

        if (game.getCurrentTurn() == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 3; k++) {
                        game.changeStrategy(BeetleType.LOW, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[1]);
                        game.changeStrategy(BeetleType.HIGH, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[1]);
                    }
                }
            }
        } else {
            // no strategy change
        }

    }

}
