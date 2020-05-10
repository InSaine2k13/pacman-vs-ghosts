package pacman.entries.pacman;

import com.sun.xml.internal.ws.api.pipe.NextAction;
import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */

// Kind of learning pacman...
public class LearningPacManTwo extends Controller<MOVE>
{
    private MOVE moveToMake=MOVE.NEUTRAL;
    private int lastLives = 3;
    private int earnedScore = 0;
    private JFrame pacManStatsWindow = new JFrame("Pacman Stats");
    private JLabel NextAction = new JLabel();
    HashMap<Integer, Integer> IndexValues = new HashMap<>();

    public MOVE getMove(Game game, long timeDue)
    {
        SetupWindow();
        // Give each index on the map a value
        EvaluateMap(game);

        //Loop through all the pill indexes and compare them to the map
        //Save the one that has the highest score and move towards it

        return moveToMake;
    }

    public void EvaluateMap(Game game){
        // Save all the pillIndices in a hashmap a
        for (int pillIndex: game.getPillIndices()) {

        }
    }

    public MOVE chooseRandomMove(){
        NextAction.setText("ChoosingRandomMove");
        Random rnd = new Random();
        switch (rnd.nextInt(4)){
            case 0: return MOVE.UP;
            case 1: return MOVE.DOWN;
            case 2: return MOVE.LEFT;
            case 3: return MOVE.RIGHT;
        }
        return MOVE.NEUTRAL;
    }

    public MOVE chooseBestMove(){
        NextAction.setText("ChoosingBestMove");
        if(leftScore > rightScore && leftScore > upScore && leftScore > downScore) { return MOVE.LEFT; }
        if(rightScore > leftScore && rightScore > upScore && rightScore > downScore) { return MOVE.RIGHT; }
        if(upScore > rightScore && upScore > leftScore && upScore > downScore) { return MOVE.UP; }
        if(downScore > rightScore && downScore > upScore && downScore > leftScore) { return MOVE.DOWN; }
        return MOVE.NEUTRAL;
    }

    public void DefaultMapValues(Game game){
        //for (int i = 1; i <= x; i++){
       //     for (int j = 1; j <= y; j++){

        //    }
       // }
    }

    public void SetupWindow(){
        pacManStatsWindow.setLayout((new GridLayout(10,1)));
        LeftScoreLbl.setPreferredSize(new Dimension(400,25));
        RightScoreLbl.setPreferredSize(new Dimension(400,25));
        DownScoreLbl.setPreferredSize(new Dimension(400,25));
        UpScoreLbl.setPreferredSize(new Dimension(400,25));
        NextAction.setPreferredSize(new Dimension(400,25));

        // Set label text
        EarnedScoreLbl.setText("Earned score: " + earnedScore);
        LeftScoreLbl.setText("Left score: " + leftScore);
        RightScoreLbl.setText("Right score: " + rightScore);
        DownScoreLbl.setText("Down score: " + downScore);
        UpScoreLbl.setText("Up score: " + upScore);
        NextAction.setText("");

        pacManStatsWindow.add(NextAction);
        pacManStatsWindow.add(EarnedScoreLbl);
        pacManStatsWindow.add(LeftScoreLbl);
        pacManStatsWindow.add(RightScoreLbl);
        pacManStatsWindow.add(DownScoreLbl);
        pacManStatsWindow.add(UpScoreLbl);
        pacManStatsWindow.pack();
        pacManStatsWindow.setVisible(true);
        pacManStatsWindow.repaint();
    }



}