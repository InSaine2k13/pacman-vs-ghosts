package pacman.entries.pacman;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

import static pacman.game.Constants.GHOST;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */

// Improved learning pacman
public class LearningPacManThree extends Controller<MOVE>
{
    private MOVE moveToMake=MOVE.NEUTRAL;
    private boolean levelEvaluationDone = false;
    private int earnedScore = 0;
    private int lastScore = 0;
    private int pacManLastLocationIndex = 0;
    private int pacManPriorToLastLocationIndex = 0;
    private int lastLevel = 0;
    private int lastLives = 3;
    private double epsilonValue = 0.99;
    private MOVE lastMove = MOVE.NEUTRAL;

    private JFrame pacManStatsWindow = new JFrame("Pacman Stats");
    private JLabel StatusLbl = new JLabel();
    private JLabel NextMoveLbl = new JLabel();
    private JLabel LastScoreLbl = new JLabel();
    private JLabel LeftScoreLbl = new JLabel();
    private JLabel RightScoreLbl = new JLabel();
    private JLabel DownScorelbl = new JLabel();
    private JLabel UpScoreLbl = new JLabel();

    private JLabel PacManLastLocationIndexLbl = new JLabel();
    private JLabel LastLevelLbl = new JLabel();
    private JLabel LastLivesLbl = new JLabel();
    private JLabel ErrorLbl = new JLabel();

    HashMap<Integer, Double> indexValueUp = new HashMap<>();
    HashMap<Integer, Double> indexValueDown = new HashMap<>();
    HashMap<Integer, Double> indexValueRight = new HashMap<>();
    HashMap<Integer, Double> indexValueLeft = new HashMap<>();

    HashMap<Integer, Integer> indexGhostValues = new HashMap<>();
    HashMap<Integer, Boolean> indexGhostFlagValues = new HashMap<>();

    public MOVE getMove(Game game, long timeDue)
    {
        // Setup statistics window
        SetupWindow(game);

        earnedScore = game.getScore() - lastScore;

        moveToMake = decideWhereToGo(game);


        // Save stats of this turn
        lastScore = game.getScore();
        lastMove = game.getPacmanLastMoveMade();
        pacManLastLocationIndex = game.getPacmanCurrentNodeIndex();
        lastLevel = game.getCurrentLevel();
        lastLives = game.getPacmanNumberOfLivesRemaining();
        // Flag locations that had ghosts on them
        NextMoveLbl.setText("Now I should go: " + moveToMake);
        return moveToMake;
    }

    public MOVE decideWhereToGo(Game game){
        //Loop through all the pill indexes and compare them to the map
        //Save the one that has the highest score and move towards it
        StatusLbl.setText("Status: Deciding where to go");


        double leftScore = 0.0;
        double rightScore = 0.0;
        double upScore = 0.0;
        double downScore = 0.0;

        try{leftScore = indexValueLeft.get(game.getPacmanCurrentNodeIndex());} catch (Exception e) { }
        try{rightScore = indexValueRight.get(game.getPacmanCurrentNodeIndex());} catch (Exception e) { }
        try{upScore = indexValueUp.get(game.getPacmanCurrentNodeIndex());} catch (Exception e) { }
        try{downScore = indexValueDown.get(game.getPacmanCurrentNodeIndex());} catch (Exception e) { }

        //Check last move that was made and based on that and score change give last index a value for a specific direction

        switch(game.getPacmanLastMoveMade()){
            case RIGHT:
                try{
                    double rightEarnedScore = earnedScore;
                    if(earnedScore == 0.0) {rightEarnedScore = indexValueRight.get(game.getPacmanCurrentNodeIndex());}
                    double rightReplacementValue = (indexValueRight.get(pacManLastLocationIndex) + (rightEarnedScore * epsilonValue));
                    if(rightReplacementValue > indexValueRight.get(pacManLastLocationIndex)){
                        indexValueRight.replace(pacManLastLocationIndex, (indexValueRight.get(pacManLastLocationIndex) + (rightEarnedScore * epsilonValue)));
                    }
                } catch (Exception e) { indexValueRight.put(pacManLastLocationIndex, earnedScore * epsilonValue); }
                break;
            case LEFT:
                try{
                    double leftEarnedScore = earnedScore;
                    if(earnedScore == 0.0) {leftEarnedScore = indexValueLeft.get(game.getPacmanCurrentNodeIndex());}
                    double leftReplacementValue = (indexValueLeft.get(pacManLastLocationIndex) + (leftEarnedScore * epsilonValue));
                    if(leftReplacementValue > indexValueLeft.get(pacManLastLocationIndex)){
                        indexValueLeft.replace(pacManLastLocationIndex, leftReplacementValue);
                    }
                } catch (Exception e) { indexValueLeft.put(pacManLastLocationIndex, earnedScore * epsilonValue); }
                break;
            case UP:
                try{
                    double upEarnedScore = earnedScore;
                    if(earnedScore == 0.0) {upEarnedScore = indexValueUp.get(game.getPacmanCurrentNodeIndex());}
                    double upReplacementValue = (indexValueUp.get(pacManLastLocationIndex) + (upEarnedScore * epsilonValue));
                    if(upReplacementValue > indexValueUp.get(pacManLastLocationIndex)){
                        indexValueUp.replace(pacManLastLocationIndex, upReplacementValue);
                    }
                } catch (Exception e) { indexValueUp.put(pacManLastLocationIndex, earnedScore * epsilonValue); }
                break;
            case DOWN:
                try{
                    double downEarnedScore = earnedScore;
                    if(earnedScore == 0.0) {downEarnedScore = indexValueDown.get(game.getPacmanCurrentNodeIndex());}
                    double downReplacementValue = (indexValueDown.get(pacManLastLocationIndex) + (downEarnedScore * epsilonValue));
                    if(downReplacementValue > indexValueDown.get(pacManLastLocationIndex)){
                        indexValueDown.replace(pacManLastLocationIndex, downReplacementValue);
                    }
                } catch (Exception e) { indexValueDown.put(pacManLastLocationIndex, earnedScore * epsilonValue); }
                break;
            default:
                break;
        }


        Random rnd = new Random();
        if(rnd.nextInt(20) == 1 || pacManLastLocationIndex == game.getPacmanCurrentNodeIndex()) { return chooseRandomMove(); }

        if(leftScore > rightScore && leftScore > upScore && leftScore > downScore) { return MOVE.LEFT; }
        if(rightScore > leftScore && rightScore > upScore && rightScore > downScore) { return MOVE.RIGHT; }
        if(upScore > rightScore && upScore > leftScore && upScore > downScore) { return MOVE.UP; }
        if(downScore > rightScore && downScore > upScore && downScore > leftScore) { return MOVE.DOWN; }

        return game.getPacmanLastMoveMade();
    }

    public MOVE chooseRandomMove(){
        StatusLbl.setText("Status: ChoosingRandomMove");
        Random rnd = new Random();
        switch (rnd.nextInt(4)){
            case 0: return MOVE.UP;
            case 1: return MOVE.DOWN;
            case 2: return MOVE.LEFT;
            case 3: return MOVE.RIGHT;
        }
        return MOVE.NEUTRAL;
    }

    public void SetupWindow(Game game){
        pacManStatsWindow.setLayout((new GridLayout(10,1)));
        StatusLbl.setPreferredSize(new Dimension(400,25));
        NextMoveLbl.setPreferredSize(new Dimension(400,25));
        LastScoreLbl.setPreferredSize(new Dimension(400,25));
        PacManLastLocationIndexLbl.setPreferredSize(new Dimension(400,25));
        LastLevelLbl.setPreferredSize(new Dimension(400,25));
        LastLivesLbl.setPreferredSize(new Dimension(400,25));
        LeftScoreLbl.setPreferredSize(new Dimension(400,25));
        UpScoreLbl.setPreferredSize(new Dimension(400,25));
        DownScorelbl.setPreferredSize(new Dimension(400,25));
        RightScoreLbl.setPreferredSize(new Dimension(400,25));
        ErrorLbl.setPreferredSize(new Dimension(400,25));

        // Set label text
        StatusLbl.setText("Status: ");
        NextMoveLbl.setText("NextMove: ");
        LastScoreLbl.setText("LastScore: " + lastScore);
        PacManLastLocationIndexLbl.setText("Last location: " + pacManLastLocationIndex);
        LastLevelLbl.setText("Last level: " + lastLevel);
        LastLivesLbl.setText("Last lives: " + lastLives);

        try {
            LeftScoreLbl.setText("Left score: " + indexValueLeft.get(game.getPacmanCurrentNodeIndex()));
            RightScoreLbl.setText("Right score: " +  indexValueRight.get(game.getPacmanCurrentNodeIndex()));
            UpScoreLbl.setText("Up score: " +  indexValueUp.get(game.getPacmanCurrentNodeIndex()));
            DownScorelbl.setText("Down score: " +  indexValueDown.get(game.getPacmanCurrentNodeIndex()));
        } catch (Exception e) {
            LeftScoreLbl.setText(e.getMessage());
        }


        pacManStatsWindow.add(StatusLbl);
        pacManStatsWindow.add(NextMoveLbl);
        pacManStatsWindow.add(LastScoreLbl);
        pacManStatsWindow.add(PacManLastLocationIndexLbl);
        pacManStatsWindow.add(LeftScoreLbl);
        pacManStatsWindow.add(RightScoreLbl);
        pacManStatsWindow.add(UpScoreLbl);
        pacManStatsWindow.add(DownScorelbl);
        pacManStatsWindow.add(LastLevelLbl);
        pacManStatsWindow.add(LastLivesLbl);
        pacManStatsWindow.add(ErrorLbl);
        pacManStatsWindow.pack();
        pacManStatsWindow.setVisible(true);
        pacManStatsWindow.repaint();
    }



}