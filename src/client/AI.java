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

    void defenseFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        for(int i = -1; i <= 1; i += 2)
            if(getState(x + dirX + i * dirY,y + dirY + i * dirX, map.getMyCells()).equals(CellState.Enemy))
                VazToScore(vaziat, Move.stepForward, -30);

        if(getState(x + dirX, y + dirY, map.getMyCells()).equals(CellState.Enemy))
            VazToScore(vaziat, Move.stepForward, -60);

        if(getState(x + 2 * dirX, y + 2 * dirY, map.getMyCells()).equals(CellState.Enemy))
            VazToScore(vaziat, Move.stepForward, -30);


        if(getState(x + dirY, y - dirX, map.getMyCells()).equals(CellState.Enemy)) {
            VazToScore(vaziat, Move.turnRight, -30);
            VazToScore(vaziat, Move.turnLeft, +30);
        }


        if(getState(x + dirY, y - dirX, map.getMyCells()).equals(CellState.Enemy)){
            VazToScore(vaziat, Move.turnLeft, -30);
            VazToScore(vaziat, Move.turnRight, +30);
        }

    }

    void attackFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        for(int i = -1; i <= 1; i += 2)
            if(getState(x + dirX + i * dirY,y + dirY + i * dirX, map.getMyCells()).equals(CellState.Enemy))
                VazToScore(vaziat, Move.stepForward, 30);

        if(getState(x + dirX, y + dirY, map.getMyCells()).equals(CellState.Enemy))
            VazToScore(vaziat, Move.stepForward, 60);

        if(getState(x + 2 * dirX, y + 2 * dirY, map.getMyCells()).equals(CellState.Enemy))
            VazToScore(vaziat, Move.stepForward, 30);


        if(getState(x + dirY, y - dirX, map.getMyCells()).equals(CellState.Enemy)) {
            VazToScore(vaziat, Move.turnRight, 30);
            VazToScore(vaziat, Move.turnLeft, -30);
        }


        if(getState(x + dirY, y - dirX, map.getMyCells()).equals(CellState.Enemy)){
            VazToScore(vaziat, Move.turnLeft, 30);
            VazToScore(vaziat, Move.turnRight, -30);
        }

    }

    int SlipperHits(int x, int y, int dirX, int dirY, Cell[][] mapcells){
        if(mapcells[x][y].getSlipper() != null) {
                return -((Slipper) mapcells[x][y].getSlipper()).getRemainingTurns()*20;
        }
        if(mapcells[x + dirX][y + dirY].getSlipper() != null) {
            return -((Slipper) mapcells[x][y].getSlipper()).getRemainingTurns()*20;
        }
        if(mapcells[x + 2 * dirX][y + 2 * dirY].getSlipper() != null) {
            return -((Slipper) mapcells[x][y].getSlipper()).getRemainingTurns()*10;
        }
        return 0;
    }
    void abscondFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        VazToScore(vaziat, Move.stepForward, SlipperHits(x + dirX, y + dirY, dirX, dirY, map.getCells()));
        VazToScore(vaziat, Move.turnLeft, SlipperHits(x, y, dirY, -dirX, map.getCells()));
        VazToScore(vaziat, Move.turnRight, SlipperHits(x, y, -dirY, dirX, map.getCells()));
    }

    void sacrificeFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        VazToScore(vaziat, Move.stepForward, -SlipperHits(x + dirX, y + dirY, dirX, dirY, map.getCells()));
        VazToScore(vaziat, Move.turnLeft, -SlipperHits(x, y, dirY, -dirX, map.getCells()));
        VazToScore(vaziat, Move.turnRight, -SlipperHits(x, y, -dirY, dirX, map.getCells()));
    }


    void reproduceFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        if(cells[x + dirX][y + dirY].getItem() != null)
            if(cells[x + dirX][y + dirY].getItem() instanceof Food)
                VazToScore(vaziat, Move.stepForward, +20);


        if(cells[x + 2 * dirX][y + 2 * dirY].getItem() != null)
            if(cells[x + 2 * dirX][y + 2 * dirY].getItem() instanceof Food)
                VazToScore(vaziat, Move.stepForward, +8);

        if(cells[x + dirY][y - dirX].getItem() != null)
            if(cells[x + dirY][y - dirX].getItem() instanceof Food)
                VazToScore(vaziat, Move.turnRight, +8);

        if(cells[x - dirY][y + dirX].getItem() != null)
            if(cells[x - dirY][y + dirX].getItem() instanceof Food)
                VazToScore(vaziat, Move.turnLeft, +8);
    }

    void infertilizationFormation(Beetle beetle, Vaziat vaziat){

        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        if(cells[x + dirX][y + dirY].getItem() != null)
            if(cells[x + dirX][y + dirY].getItem() instanceof Food)
                if(((Food)cells[x + dirX][y + dirY].getItem()).getRemainingTurns() > 0)
                    VazToScore(vaziat, Move.stepForward, -30);
    }


    void sickenFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        if(cells[x + dirX][y + dirY].getItem() != null)
            if(cells[x + dirX][y + dirY].getItem() instanceof Trash)
                VazToScore(vaziat, Move.stepForward, +20);


        if(cells[x + 2 * dirX][y + 2 * dirY].getItem() != null)
            if(cells[x + 2 * dirX][y + 2 * dirY].getItem() instanceof Trash)
                VazToScore(vaziat, Move.stepForward, +8);

        if(cells[x + dirY][y - dirX].getItem() != null)
            if(cells[x + dirY][y - dirX].getItem() instanceof Trash)
                VazToScore(vaziat, Move.turnRight, +8);

        if(cells[x - dirY][y + dirX].getItem() != null)
            if(cells[x - dirY][y + dirX].getItem() instanceof Trash)
                VazToScore(vaziat, Move.turnLeft, +8);
    }

    void avoidIllnessFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        if(cells[x + dirX][y + dirY].getItem() != null)
            if(cells[x + dirX][y + dirY].getItem() instanceof Trash)
                if(((Trash)cells[x + dirX][y + dirY].getItem()).getRemainingTurns() > 0)
                    VazToScore(vaziat, Move.stepForward, -30);
    }

    void rougeFormation(Beetle beetle, Vaziat vaziat){
        int x = beetle.getPosition().getX(), y = beetle.getPosition().getY();
        Cell[][] cells = map.getCells();
        int dirX, dirY = dirX = 0;
        switch (beetle.getDirection()) {
            case Down:
                dirY = -1;
                break;
            case Up:
                dirY = 1;
                break;
            case Left:
                dirX = -1;
                break;
            case Right:
                dirX = 1;
                break;
        }

        for(int i = -1; i <= 1; i++)
            if(getState(x - dirX - i * dirY, y - dirY - i * dirY, map.getMyCells()).equals(CellState.Ally)) {
                int val = -10;
                if(((Beetle) cells[x - dirX - i * dirY][y - dirY - i * dirY].getBeetle()).has_winge())
                    val = -20;
                VazToScore(vaziat, Move.stepForward, val);
            }

        for(int i = -1; i <= 1; i++)
            if(getState(x + 2 * dirX - i * dirY, y + 2* dirY - i * dirY, map.getMyCells()).equals(CellState.Ally)) {
                int val = -10;
                if(((Beetle) cells[x + 2 * dirX - i * dirY][y + 2 * dirY - i * dirY].getBeetle()).has_winge())
                    val = -20;
                VazToScore(vaziat, Move.stepForward, val);
            }
    }

    int[][][][][] scores=new int[2][3][2][3][3];

    void VazToScore(Vaziat vaz, Move move, int value){

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


        for(int i=0;i<2;i++)
            for(int j=0;j<3;j++)
                for(int k=0;k<2;k++)
                    for(int l=0;l<3;l++)
                    {
                        scores[i][j][k][l][1] = 10; //moving forward is good
                        scores[i][j][k][l][0] = 0;
                        scores[i][j][k][l][2] = 0;
                        scores[i][j][k][l][strategy[i][j][k][l]] += 10; //don't changing strategy is good
                    }
        for(int i = 0; i < beetles.size(); i++){
            Beetle beetle = beetles.get(i);
            Vaziat vaziat = vaziats.get(i);
            if(beetle.is_sick()) {
                rougeFormation(beetle, vaziat);
                attackFormation(beetle, vaziat);
            }
            if(!beetle.has_winge()) {
                infertilizationFormation(beetle, vaziat);
                if (beetle.getBeetleType().equals(BeetleType.HIGH)) {
                    abscondFormation(beetle, vaziat);
                    if(!beetle.is_sick())
                        avoidIllnessFormation(beetle, vaziat);
                }
                else {
                    sickenFormation(beetle, vaziat);
                    sacrificeFormation(beetle, vaziat);
                    defenseFormation(beetle, vaziat);
                }
            }
            else{
                abscondFormation(beetle, vaziat);
                reproduceFormation(beetle, vaziat);
                if(!beetle.is_sick())
                    avoidIllnessFormation(beetle, vaziat);
                if(beetle.getBeetleType().equals(BeetleType.HIGH)) {
                    attackFormation(beetle, vaziat);
                }
                else defenseFormation(beetle, vaziat);
            }
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
