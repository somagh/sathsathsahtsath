package client;

import client.model.*;

import java.util.ArrayList;
import java.util.List;

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

    public AI()
    {
        for(int i=0;i<2;i++)
            for(int j=0;j<3;j++)
                for(int k=0;k<2;k++)
                    for(int l=0;l<3;l++)
                        strategy[i][j][k][l]=-1;
    }
    class State{
        CellState front, right, left;
    }

    private void defenseFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();

        for(int i = -1; i <= 1; i += 2)
            if(getState(x + dirX + i * dirY,y + dirY + i * dirX, map.getMyCells()).equals(CellState.Enemy))
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, -30);

        if(getState(x + dirX, y + dirY, map.getMyCells()).equals(CellState.Enemy))
            StateToScore(beetle.getBeetleType(),state, Move.stepForward, -60);

        if(getState(x + 2 * dirX, y + 2 * dirY, map.getMyCells()).equals(CellState.Enemy))
            StateToScore(beetle.getBeetleType(),state, Move.stepForward, -30);


        if(getState(x + dirY, y - dirX, map.getMyCells()).equals(CellState.Enemy)) {
            StateToScore(beetle.getBeetleType(),state, Move.turnRight, -30);
            StateToScore(beetle.getBeetleType(),state, Move.turnLeft, +30);
        }


        if(getState(x + dirY, y - dirX, map.getMyCells()).equals(CellState.Enemy)){
            StateToScore(beetle.getBeetleType(),state, Move.turnLeft, -30);
            StateToScore(beetle.getBeetleType(),state, Move.turnRight, +30);
        }

    }

    private void attackFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();

        for(int i = -1; i <= 1; i += 2)
            if(getState(x + dirX - i * dirY,y + dirY - i * dirX, map.getMyCells()).equals(CellState.Enemy))
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, 30);

        if(getState(x + dirX, y + dirY, map.getMyCells()).equals(CellState.Enemy))
            StateToScore(beetle.getBeetleType(),state, Move.stepForward, 60);

        if(getState(x + 2 * dirX, y + 2 * dirY, map.getMyCells()).equals(CellState.Enemy))
            StateToScore(beetle.getBeetleType(),state, Move.stepForward, 30);


        if(getState(x - dirY, y + dirX, map.getMyCells()).equals(CellState.Enemy)) {
            StateToScore(beetle.getBeetleType(),state, Move.turnRight, 30);
            StateToScore(beetle.getBeetleType(),state, Move.turnLeft, -30);
        }


        if(getState(x - dirY, y + dirX, map.getMyCells()).equals(CellState.Enemy)){
            StateToScore(beetle.getBeetleType(),state, Move.turnLeft, 30);
            StateToScore(beetle.getBeetleType(),state, Move.turnRight, -30);
        }

    }

    private int SlipperHits(int x, int y, int dirX, int dirY, Cell[][] mapCells){
        x = (x + width * height) % height;
        y = (y + width * height) % width;
        if(mapCells[x][y].getSlipper() != null) {
                return -(((Slipper) mapCells[x][y].getSlipper()).getRemainingTurns())*40;
        }
        if(mapCells[(y + dirY + 10 * height) % height][(x + dirX + 10 * width) % width].getSlipper() != null) {
            return -(((Slipper) mapCells[(y + dirY + 10 * height) % height][(x + dirX + 10 * width) % width].getSlipper()).getRemainingTurns())*40;
        }
        if(mapCells[(y + 2 * dirY + 10 * height) % height][(x + 2 * dirX + 10 * width) % width].getSlipper() != null) {
            return -(((Slipper) mapCells[(y + 2 * dirY + 10 * height) % height][(x + 2 * dirX + 10 * width) % width].getSlipper()).getRemainingTurns())*20;
        }
        return 0;
    }
    private void abscondFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();
        
        StateToScore(beetle.getBeetleType(),state, Move.stepForward, SlipperHits(x + dirX, y + dirY, dirX, dirY, map.getCells()));
        StateToScore(beetle.getBeetleType(),state, Move.turnLeft, SlipperHits(x, y, -dirY, dirX, map.getCells()));
        StateToScore(beetle.getBeetleType(),state, Move.turnRight, SlipperHits(x, y, dirY, -dirX, map.getCells()));
    }

    private void sacrificeFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();

        StateToScore(beetle.getBeetleType(),state, Move.stepForward, -SlipperHits(x + dirX, y + dirY, dirX, dirY, map.getCells()));
        StateToScore(beetle.getBeetleType(),state, Move.turnLeft, -SlipperHits(x, y, -dirY, dirX, map.getCells()));
        StateToScore(beetle.getBeetleType(),state, Move.turnRight, -SlipperHits(x, y, dirY, -dirX, map.getCells()));
    }


    private void reproduceFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();
        Cell[][] cells = map.getCells();

        if(cells[(y + dirY + 10 * height) % height][(x + dirX + 10 * width) % width].getItem() != null)
            if(cells[(y + dirY + 10 * height) % height][(x + dirX + 10 * width) % width].getItem() instanceof Food)
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, +30);


        if(cells[(y + 2 * dirY + 10 * height) % height][(x + 2 * dirX + 10 * width) % width].getItem() != null)
            if(cells[(y + 2 * dirY + 10 * height) % height][(x + 2 * dirX + 10 * width) % width].getItem() instanceof Food)
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, +17);

        if(cells[(y + dirX + height * width) % height][(x - dirY + height * width) % width].getItem() != null)
            if(cells[(y + dirX + height * width) % height][(x-dirY + height * width) % width].getItem() instanceof Food)
                StateToScore(beetle.getBeetleType(),state, Move.turnRight, +17);

        if(cells[(y-dirX + height * width) % height][(x+dirY + height * width) % width].getItem() != null)
            if(cells[(y-dirX + height * width) % height][(x+dirY + height * width) % width].getItem() instanceof Food)
                StateToScore(beetle.getBeetleType(),state, Move.turnLeft, +17);
    }

    private void infertilityFormation(Beetle beetle, State state, int dirX, int dirY){

        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();
        Cell[][] cells = map.getCells();

        if(cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem() != null)
            if(cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem() instanceof Food)
                if(((Food)cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem()).getRemainingTurns() > 0)
                    StateToScore(beetle.getBeetleType(),state, Move.stepForward, -30);
    }


    private void sickenFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();
        Cell[][] cells = map.getCells();

        if(cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem() != null)
            if(cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem() instanceof Trash)
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, +20);


        if(cells[(y + 2 * dirY + 10 * height) % height][(x + 2 * dirX + 10 * width) % width].getItem() != null)
            if(cells[(y + 2 * dirY + 10 * height) % height][(x + 2 * dirX + 10 * width) % width].getItem() instanceof Trash)
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, +8);

        if(cells[(y+dirX + height * width) % height][(x-dirY + height * width) % width].getItem() != null)
            if(cells[(y+dirX + height * width) % height][(x-dirY + height * width) % width].getItem() instanceof Trash)
                StateToScore(beetle.getBeetleType(),state, Move.turnRight, +8);

        if(cells[(y-dirX + height * width) % height][(x+dirY + height * width) % width].getItem() != null)
            if(cells[(y-dirX + height * width) % height][(x+dirY + height * width) % width].getItem() instanceof Trash)
                    StateToScore(beetle.getBeetleType(),state, Move.turnLeft, +8);
                }

    private void avoidIllnessFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();
        Cell[][] cells = map.getCells();
        if(cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem() != null)
            if(cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem() instanceof Trash)
                if(((Trash)cells[(y + dirY + height) % height][(x + dirX + width) % width].getItem()).getRemainingTurns() > 0) {
                   // System.out.println(beetle.getRow() + " " + beetle.getColumn() +" "+ beetle.getBeetleType().getValue() + " " + state.right.getValue() + " " + state.front.getValue() + " " + state.left.getValue());
                    StateToScore(beetle.getBeetleType(), state, Move.stepForward, -30);
                }
    }

    private void rogueFormation(Beetle beetle, State state, int dirX, int dirY){
        int x = beetle.getPosition().getY(), y = beetle.getPosition().getX();
        Cell[][] cells = map.getCells();

        for(int i = -1; i <= 1; i++)
            if(getState(x - dirX + i * dirY, y - dirY + i * dirX, map.getMyCells()).equals(CellState.Ally)) {
                int val = -10;
                if(((Beetle) cells[(x - dirX + i * dirY + width * height) % height][(y - dirY + i * dirX + height * width) % width].getBeetle()).has_winge())
                    val = -20;
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, val);
            }

        for(int i = -1; i <= 1; i++)
            if(getState(x + 2 * dirX + i * dirY, y + 2* dirY + i * dirX, map.getMyCells()).equals(CellState.Ally)) {
                int val = -10;
                if(((Beetle) cells[(x + 2 * dirX + i * dirY + width * height) % height][(y + 2 * dirY + i * dirX + height * width) % width].getBeetle()).has_winge())
                    val = -20;
                StateToScore(beetle.getBeetleType(),state, Move.stepForward, val);
            }
    }

    private int[][][][][] scores=new int[2][3][2][3][3];

    private void StateToScore(BeetleType bt, State state, Move move, int value)
    {
        scores[bt.getValue()][state.right.getValue()][state.front.getValue()][state.left.getValue()][move.getValue()]+=value;
    }

    private CellState getState(int x, int y, Cell[] myCells){
        x = (x + width * height) % height;
        y = (y + width * width) % width;
        Entity e = map.getCell(x, y).getBeetle();
        if (e == null)
            return CellState.Blank;
        Beetle b = (Beetle) e;
        Cell c = b.getPosition();
        for (Cell cell: myCells)
            if (cell.getX() == c.getX() && cell.getY() == c.getY())
                return CellState.Ally;
        return CellState.Enemy;
    }
    private Map map;

    private int[][][][] strategy=new int[2][3][2][3];
    private int height, width;
    public void doTurn(World game)
    {
       // System.out.println();
        map = game.getMap();
        Cell[] cells = map.getMyCells();
        List<Beetle> beetles=new ArrayList<>();
        List<State> states = new ArrayList<>();
        for(int i=0;i<2;i++)
            for(int j=0;j<3;j++)
                for(int k=0;k<2;k++)
                    for(int l=0;l<3;l++)
                        for(int m=0;m<3;m++)
                            scores[i][j][k][l][m]=0;
        int max_power=0;
        for (Cell cell : cells) {


            beetles.add((Beetle) cell.getBeetle());
            max_power = Math.max(max_power, beetles.get(beetles.size() - 1).getPower());
        }
        for(int i=0;i<beetles.size();i++) {
            Beetle beetle=beetles.get(i);
            if (beetle.getPower() < max_power *3 / 7 && beetle.getBeetleType().equals(BeetleType.LOW))
                game.changeType(beetle, BeetleType.HIGH);
            if (beetle.getPower() >= max_power*4 / 7 && beetle.getBeetleType().equals(BeetleType.HIGH))
                game.changeType(beetle, BeetleType.LOW);

            State state = new State();
            int x = beetle.getRow(), y = beetle.getColumn();
            int rightX = x, rightY = y, leftX = x, leftY = y, frontDirX = 0, frontDirY = 0;
            int n = height = map.getHeight(), m = width = map.getWidth();
            switch (beetle.getDirection()) {
                case Down:
                    rightX = (x + 1) % n;
                    leftY = (y + 1) % m;
                    rightY = (y + m - 1) % m;
                    leftX = rightX;
                    frontDirX = 1;
                    break;
                case Up:
                    rightX = (x + n - 1) % n;
                    rightY = (y + 1) % m;
                    leftY = (y + m - 1) % m;
                    leftX = rightX;
                    frontDirX = -1;
                    break;
                case Right:

                    rightX = (x + 1) % n;
                    leftY = (y + 1) % m;
                    rightY = leftY;
                    leftX = (x + n - 1) % n;
                    frontDirY = 1;
                    break;
                case Left:

                    leftX = (x + 1) % n;
                    leftY = (y + m - 1) % m;
                    rightY = leftY;
                    rightX = (x + n - 1) % n;
                    frontDirY = -1;
                    break;
            }

            state.right = this.getState(rightX, rightY, cells);
            state.left = this.getState(leftX, leftY, cells);
            do {
                x = (x + frontDirX + n) % n;
                y = (y + frontDirY + m) % m;
                CellState c = getState(x, y, cells);
                if (c == CellState.Blank)
                    continue;
                state.front = c;
                break;
            } while (true);
            states.add(state);
        }


        for(int i=0;i<2;i++)
            for(int j=0;j<3;j++)
                for(int k=0;k<2;k++)
                    for(int l=0;l<3;l++)
                    {
                        scores[i][j][k][l][1] = 30; //moving forward is good
                        scores[i][j][k][l][0] = 0;
                        scores[i][j][k][l][2] = 0;
                        if(game.getCurrentTurn()>0)
                            scores[i][j][k][l][strategy[i][j][k][l]] += 25; //don't changing strategy is good
                    }

        for(int i = 0; i < beetles.size(); i++){
            Beetle beetle = beetles.get(i);
            State state = states.get(i);
            int dirX, dirY = dirX = 0;
            switch (beetle.getDirection()) {
                case Down:
                    dirY = 1;
                    break;
                case Up:
                    dirY = -1;
                    break;
                case Left:
                    dirX = -1;
                    break;
                case Right:
                    dirX = 1;
                    break;
            }
            if(beetle.is_sick()) {
                rogueFormation(beetle, state, dirX, dirY);
                attackFormation(beetle, state, dirX, dirY);
            }
            if(!beetle.has_winge()) {
//              infertilityFormation(beetle, state, dirX, dirY);
                reproduceFormation(beetle, state, dirX, dirY);
                if (beetle.getBeetleType().equals(BeetleType.LOW)) {
                    abscondFormation(beetle, state, dirX, dirY);
                    if(!beetle.is_sick())
                        avoidIllnessFormation(beetle, state, dirX, dirY);
                    //for(int _1 = 0; _1 < 10; _1++)
                    attackFormation(beetle, state, dirX, dirY);
                    rogueFormation(beetle, state, dirX, dirY);
                }
                else {
                    defenseFormation(beetle, state, dirX, dirY);
                }
            }
            else{
                for(int _1 = 0; _1 < 7; _1++) {
                    abscondFormation(beetle, state, dirX, dirY);
                    abscondFormation(beetle, state, dirX, dirY);
                    abscondFormation(beetle, state, dirX, dirY);
                    reproduceFormation(beetle, state, dirX, dirY);
                    reproduceFormation(beetle, state, dirX, dirY);
                    if (!beetle.is_sick())
                    {
                        avoidIllnessFormation(beetle, state, dirX, dirY);
                        avoidIllnessFormation(beetle, state, dirX, dirY);
                        avoidIllnessFormation(beetle, state, dirX, dirY);
                        if(beetle.getBeetleType()==BeetleType.LOW) {
                            avoidIllnessFormation(beetle, state, dirX, dirY);
                            avoidIllnessFormation(beetle, state, dirX, dirY);
                        }
                    }
                }
                defenseFormation(beetle, state, dirX, dirY);
            }
        }
        //System.out.println(game.getCurrentTurn());
        //System.out.println(scores[1][2][0][2][1]);
        //System.out.println(scores[1][0][0][2][2]);
        //System.out.println(scores[1][0][0][2][0]);
        //System.out.println();
        System.out.println(strategy[0][2][0][2]);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 3; k++) {
                    int a = 1;
                    if (scores[BeetleType.LOW.getValue()][i][j][k][0] > scores[BeetleType.LOW.getValue()][i][j][k][1] && scores[BeetleType.LOW.getValue()][i][j][k][0] >= scores[BeetleType.LOW.getValue()][i][j][k][2])
                        a = 0;
                    if (scores[BeetleType.LOW.getValue()][i][j][k][2] > scores[BeetleType.LOW.getValue()][i][j][k][1] && scores[BeetleType.LOW.getValue()][i][j][k][2] >= scores[BeetleType.LOW.getValue()][i][j][k][0])
                        a = 2;
                    if (a != strategy[BeetleType.LOW.getValue()][i][j][k]) {
                        //if(game.getCurrentTurn()>1)
                        //System.out.println(i+" "+j+" "+k+" "+a);
                        game.changeStrategy(BeetleType.LOW, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[a]);
                        strategy[BeetleType.LOW.getValue()][i][j][k] = a;
                    }
                    a = 1;
                    if (scores[BeetleType.HIGH.getValue()][i][j][k][0] > scores[BeetleType.HIGH.getValue()][i][j][k][1] && scores[BeetleType.HIGH.getValue()][i][j][k][0] >= scores[BeetleType.HIGH.getValue()][i][j][k][2])
                        a = 0;
                    if (scores[BeetleType.HIGH.getValue()][i][j][k][2] > scores[BeetleType.HIGH.getValue()][i][j][k][1] && scores[BeetleType.HIGH.getValue()][i][j][k][2] >= scores[BeetleType.HIGH.getValue()][i][j][k][0])
                        a = 2;
                    if (a != strategy[BeetleType.HIGH.getValue()][i][j][k]) {
                        //if(game.getCurrentTurn()>1)
                        //    System.out.println(i+" "+j+" "+k+" "+a);
                        game.changeStrategy(BeetleType.HIGH, CellState.values()[i], CellState.values()[j], CellState.values()[k], Move.values()[a]);
                        strategy[BeetleType.HIGH.getValue()][i][j][k] = a;
                    }

                }
    }

}
