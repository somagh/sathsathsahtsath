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
    class Vaziat{
        public CellState front, right, left;
    }

    CellState getState(int x, int y, Cell[] mycells){
        Entity e = map.getCell(x, y).getBeetle();
        if (e == null)
            return CellState.Blank;
        Beetle b = (Beetle) e;
        Cell c = b.getPosition();
        for (Cell cell: mycells)
            if (cell.getX() == c.getX() && cell.getY() == c.getY())
                return CellState.Ally;
        return CellState.Enemy;
    }
    Map map;

    int[][][][] strategy=new int[2][3][2][3];
    public void doTurn(World game)
    {
        // fill this method, we've presented a stupid AI for example!
        Random rand = new Random();

        map = game.getMap();
        Cell[] cells = map.getMyCells();
        List<Beetle> beetles=new ArrayList<>();
        List<Vaziat> vaziats = new ArrayList<>();

        int max_power=0;
        for (Cell cell : cells) {
            beetles.add((Beetle) cell.getBeetle());
            max_power = Math.max(max_power, beetles.get(beetles.size() - 1).getPower());
        }
        for(Beetle beetle:beetles) {
            if (beetle.getPower() < max_power / 2 && beetle.getBeetleType().equals(BeetleType.HIGH))
                game.changeType(beetle, BeetleType.LOW);
            if (beetle.getPower() >= max_power / 2 && beetle.getBeetleType().equals(BeetleType.LOW))
                game.changeType(beetle, BeetleType.HIGH);

            Vaziat vaziat = new Vaziat();
            int x = beetle.getRow(), y = beetle.getColumn();
            int rightX = x, rightY = y, leftX = x, leftY = y, frontDirX = 0, frontDirY = 0;
            int n = map.getHeight(), m = map.getWidth();
            switch (beetle.getDirection()) {
                case Down:
                    rightX = (x + 1) % m;
                    leftY = (y + 1) % m;
                    rightY = (y + m - 1) % m;
                    leftX = rightX;
                    frontDirX = 1;
                    break;
                case Up:
                    rightX = (x + n - 1) % m;
                    rightY = (y + 1) % m;
                    leftY = (y + m - 1) % m;
                    leftX = rightX;
                    frontDirX = -1;
                    break;
                case Right:

                    rightX = (x + 1) % m;
                    leftY = (y + 1) % m;
                    rightY = leftY;
                    leftX = (x + n - 1) % m;
                    frontDirY = 1;
                    break;
                case Left:

                    leftX = (x + 1) % m;
                    leftY = (y + m - 1) % m;
                    rightY = leftY;
                    rightX = (x + n - 1) % m;
                    frontDirY = -1;
                    break;
            }

            vaziat.right = this.getState(rightX, rightY, cells);
            vaziat.left = this.getState(leftX, leftY, cells);
            do {
                x = (x + frontDirX + n) % n;
                y = (y + frontDirY + m) % m;
                CellState c = getState(x, y, cells);
                if (c == CellState.Blank)
                    continue;
                vaziat.front = c;
                break;
            } while (true);
            vaziats.add(vaziat);
        }



        int[][][][][] scores=new int[2][3][2][3][3];
        for(int i=0;i<2;i++)
            for(int j=0;j<3;j++)
                for(int k=0;k<2;k++)
                    for(int l=0;l<3;l++)
                    {
                        scores[i][j][k][l][1] = 10; //moving forward is good
                        scores[i][j][k][l][0] = 0;
                        scores[i][j][k][l][2] = 0;
                        scores[i][j][k][l][strategy[i][j][k][l]]+=10; //don't changing strategy is good
                    }

        for(Beetle bettle:beetles)
        {
            //set score for each move here
        }
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 3; k++) {
                    int a = 1;
                    if (scores[0][i][j][k][0] > scores[0][i][j][k][1] && scores[0][i][j][k][0] > scores[0][i][j][k][2])
                        a = 0;
                    if (scores[0][i][j][k][2] > scores[0][i][j][k][1] && scores[0][i][j][k][2] > scores[0][i][j][k][0])
                        a = 2;
                    if (a != strategy[0][i][j][k]) {
                        game.changeStrategy(BeetleType.LOW, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[a]);
                        strategy[0][i][j][k] = a;
                    }
                    a = 1;
                    if (scores[1][i][j][k][0] > scores[1][i][j][k][1] && scores[1][i][j][k][0] > scores[1][i][j][k][2])
                        a = 0;
                    if (scores[1][i][j][k][2] > scores[1][i][j][k][1] && scores[1][i][j][k][2] > scores[1][i][j][k][0])
                        a = 2;
                    if (a != strategy[1][i][j][k]) {
                        game.changeStrategy(BeetleType.HIGH, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[a]);
                        strategy[1][i][j][k] = a;
                    }
                }
    }

}
