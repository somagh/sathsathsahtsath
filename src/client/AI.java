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
public class AI
{

    public void doTurn(World game)
    {
        // fill this method, we've presented a stupid AI for example!
        Random rand = new Random();

        Map map = game.getMap();
        Cell[] cells = map.getMyCells();
        List<Beetle> beetles=new ArrayList<>();
        int max_power=0;
        for (Cell cell : cells) {
            beetles.add((Beetle) cell.getBeetle());
            max_power = Math.max(max_power, beetles.get(beetles.size() - 1).getPower());
        }
        for(Beetle beetle:beetles)
        {
            if(beetle.getPower()<max_power/2&&beetle.getBeetleType().equals(BeetleType.HIGH))
                game.changeType(beetle,BeetleType.LOW);
            if(beetle.getPower()>=max_power/2&&beetle.getBeetleType().equals(BeetleType.LOW))
                game.changeType(beetle,BeetleType.HIGH);
        }

        if (game.getCurrentTurn() == 0)
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 2; j++)
                {
                    for (int k = 0; k < 3; k++)
                    {
                        game.changeStrategy(BeetleType.LOW, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[1]);
                        game.changeStrategy(BeetleType.HIGH, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[1]);
                    }
                }
            }
        } else
        {
            // no strategy change
        }

    }

}
