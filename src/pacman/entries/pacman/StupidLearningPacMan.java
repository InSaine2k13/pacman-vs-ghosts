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
public class StupidLearningPacMan extends Controller<MOVE>
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
    private JLabel EarnedScoreLbl = new JLabel();
    private JLabel NextAction = new JLabel();
    private JLabel NextMove = new JLabel();
    private JLabel LastMove = new JLabel();

    public MOVE getMove(Game game, long timeDue)
    {
        SetupWindow(game);

        earnedScore = game.getScore() - lastScore;
        game.copy();
        //Process the reward or punishment
        if(game.getScore() > lastScore){
            switch (game.getPacmanLastMoveMade()){
                case LEFT: leftScore += earnedScore;
                    break;
                case RIGHT: rightScore += earnedScore;
                    break;
                case UP: upScore += earnedScore;
                    break;
                case DOWN: downScore += earnedScore;
                    break;
                default: break;
            }
        } else if(lastLives > game.getPacmanNumberOfLivesRemaining()){
            switch(game.getPacmanLastMoveMade()){
                case LEFT: leftScore -= 10;
                    break;
                case RIGHT: rightScore -= 10;
                    break;
                case UP: upScore -= 10;
                    break;
                case DOWN: downScore -= 10;
                    break;
            }
        } else if(game.getScore() == lastScore){
            // Exploration versus Exploitation
            Random rnd = new Random();
            if(rnd.nextInt(100) == 0) {moveToMake = chooseRandomMove();} else { moveToMake = chooseBestMove(); }
        }

        lastScore = game.getScore();
        lastLives = game.getPacmanNumberOfLivesRemaining();
        NextMove.setText("Next move: " + moveToMake);
        return moveToMake;
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
        NextAction.setText("ChoosingBestMove NEUTRAL");
        if(leftScore > rightScore && leftScore > upScore && leftScore > downScore) { NextAction.setText("ChoosingBestMove I chose Left"); return MOVE.LEFT; }
        if(rightScore > leftScore && rightScore > upScore && rightScore > downScore) { NextAction.setText("ChoosingBestMove I chose Right"); return MOVE.RIGHT; }
        if(upScore > rightScore && upScore > leftScore && upScore > downScore) { NextAction.setText("ChoosingBestMove I chose Up"); return MOVE.UP; }
        if(downScore > rightScore && downScore > upScore && downScore > leftScore) { NextAction.setText("ChoosingBestMove I chose Down"); return MOVE.DOWN; }
        return MOVE.NEUTRAL;
    }

    public void SetupWindow(Game game){
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
        NextMove.setText("");
        LastMove.setText("Last move was:" + game.getPacmanLastMoveMade());

        pacManStatsWindow.add(LastMove);
        pacManStatsWindow.add(NextMove);
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