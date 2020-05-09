package pacman.entries.pacman;

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
public class LearningPacMan extends Controller<MOVE>
{
    private MOVE moveToMake=MOVE.NEUTRAL;
    private int leftScore = 0;
    private int upScore = 0;
    private int downScore = 0;
    private int rightScore = 0;
    private int lastLives = 3;
    private int lastScore = 0;
    private int earnedScore = 0;
    private JFrame pacManStatsWindow = new JFrame("Pacman Stats");
    private JLabel LeftScoreLbl = new JLabel();
    private JLabel UpScoreLbl = new JLabel();
    private JLabel DownScoreLbl = new JLabel();
    private JLabel RightScoreLbl = new JLabel();

    public MOVE getMove(Game game, long timeDue)
    {
        SetupWindow();
        earnedScore = game.getScore() - lastScore;
        lastScore = game.getScore();

        //Process the reward or punishment
        if(game.getScore() > lastScore){
            switch (game.getPacmanLastMoveMade()){
                case LEFT: leftScore += earnedScore;
                case RIGHT: rightScore += earnedScore;
                case UP: upScore += earnedScore;
                case DOWN: downScore += earnedScore;
            }
        } else if(lastLives > game.getPacmanNumberOfLivesRemaining()){
            switch(game.getPacmanLastMoveMade()){
                case LEFT: leftScore -= 50;
                case RIGHT: rightScore -= 50;
                case UP: upScore -= 50;
                case DOWN: downScore -= 50;
            }
        } else if(game.getScore() == lastScore){
            moveToMake = chooseRandomMove();
        }

        if(leftScore == rightScore && upScore == downScore && leftScore == downScore) {
            moveToMake = chooseRandomMove();
        } else {
            moveToMake = chooseBestMove();
        }

        return moveToMake;
    }

    public MOVE chooseRandomMove(){
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
        if(leftScore > rightScore && leftScore > upScore && leftScore > downScore) { return MOVE.LEFT; }
        if(rightScore > leftScore && rightScore > upScore && rightScore > downScore) { return MOVE.RIGHT; }
        if(upScore > rightScore && upScore > leftScore && upScore > downScore) { return MOVE.UP; }
        if(downScore > rightScore && downScore > upScore && downScore > leftScore) { return MOVE.DOWN; }
        return MOVE.NEUTRAL;
    }

    public void SetupWindow(){
        pacManStatsWindow.setLayout((new GridLayout(10,1)));
        LeftScoreLbl.setPreferredSize(new Dimension(400,25));
        RightScoreLbl.setPreferredSize(new Dimension(400,25));
        DownScoreLbl.setPreferredSize(new Dimension(400,25));
        UpScoreLbl.setPreferredSize(new Dimension(400,25));
        LeftScoreLbl.setText("Left score: ");
        RightScoreLbl.setText("Right score: ");
        DownScoreLbl.setText("Down score: ");
        UpScoreLbl.setText("Up score: ");
        pacManStatsWindow.pack();
        pacManStatsWindow.setVisible(true);
        pacManStatsWindow.repaint();
    }

}