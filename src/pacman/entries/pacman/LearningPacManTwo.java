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
public class LearningPacManTwo extends Controller<MOVE>
{
    private MOVE moveToMake=MOVE.NEUTRAL;
    private boolean levelEvaluationDone = false;
    private int earnedScore = 0;
    private int lastScore = 0;
    private int pacManLastLocationIndex = 0;
    private int lastLevel = 0;
    private int lastLives = 3;
    private JFrame pacManStatsWindow = new JFrame("Pacman Stats");
    private JLabel StatusLbl = new JLabel();
    private JLabel NextMoveLbl = new JLabel();
    private JLabel EarnedScoreLbl = new JLabel();
    private JLabel LastScoreLbl = new JLabel();
    private JLabel PacManLastLocationIndexLbl = new JLabel();
    private JLabel LastLevelLbl = new JLabel();
    private JLabel LastLivesLbl = new JLabel();

    HashMap<Integer, Integer> indexValues = new HashMap<>();
    HashMap<Integer, Integer> indexGhostValues = new HashMap<>();
    HashMap<Integer, Boolean> indexGhostFlagValues = new HashMap<>();

    public MOVE getMove(Game game, long timeDue)
    {
        // Setup statistics window
        SetupWindow();

        // Check if level has changed
        // If the level has changed make sure to reevaluate the map.
        if(lastLevel != game.getCurrentLevel()){ levelEvaluationDone = false;}

        // Give each index on the map a value
        EvaluateMap(game);

        //decideWhereToGo(game);

        try {
            int[] indicesToTravel = game.getShortestPath(game.getPacmanCurrentNodeIndex(), decideWhereToGo(game));
            int pacX = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
            int pacY = game.getNodeYCood(game.getPacmanCurrentNodeIndex());
            int nodeX = game.getNodeXCood(indicesToTravel[0]);
            int nodeY = game.getNodeYCood(indicesToTravel[0]);
            // Decide direction to move
            if(pacX < nodeX) {
                moveToMake = MOVE.RIGHT;
            } else if(pacX > nodeX) {
                moveToMake = MOVE.LEFT;
            }

            if(pacY < nodeY) {
                moveToMake = MOVE.DOWN;
            } else if(pacY > nodeY) {
                moveToMake = MOVE.UP;
            }
        } catch(Exception e) {
            moveToMake = MOVE.NEUTRAL;
        }


        // Save stats of this turn
        lastScore = game.getScore();
        pacManLastLocationIndex = game.getPacmanCurrentNodeIndex();
        lastLevel = game.getCurrentLevel();
        lastLives = game.getPacmanNumberOfLivesRemaining();
        // Flag locations that had ghosts on them

        return moveToMake;
    }

    public int decideWhereToGo(Game game){
        //Loop through all the pill indexes and compare them to the map
        //Save the one that has the highest score and move towards it
        StatusLbl.setText("Status: Deciding where to go");
        int bestIndexValue = 0;
        int bestIndex = 0;
        for (int pillIndex: game.getPillIndices()) {
            // Check if there is a ghost on the index
            if(!checkForGhosts(pillIndex, game)){
                if(bestIndexValue < indexValues.get(pillIndex)){
                    bestIndexValue = indexValues.get(pillIndex);
                    bestIndex = pillIndex;
                }
            } else {
                // If a ghost was found on the location check the GhostIndexValue instead
                if(bestIndexValue < indexGhostValues.get(pillIndex)){
                    bestIndexValue = indexGhostValues.get(pillIndex);
                    bestIndex = pillIndex;
                }
            }
        }
 //1% chance to choose a completely random index
//        Random rnd = new Random();
//        Random rndL = new Random();
//        if(rnd.nextInt(100) == 1 || bestIndex == 0){
//            for (int pillIndex: game.getPillIndices()) {
//                if(rndL.nextInt(10) == rnd.nextInt(10) || bestIndex == 0){
//                    bestIndex = pillIndex;
//                }
//            }
//        }
        StatusLbl.setText("Status: Best index to go is: " + bestIndex);
        return bestIndex;
    }

    public void EvaluateMap(Game game){
        StatusLbl.setText("Status: Evaluating the map");
        // If the level has not been evaluated yet
        if(!levelEvaluationDone)
        {
            // Save all the pillIndices in a hashmap a
            for (int pillIndex: game.getPillIndices()) {
                // The normal index value
                indexValues.put(pillIndex, 1);

                // The index value when a ghost is on it
                indexGhostValues.put(pillIndex, 0);

                // Flag every point to not have a ghost on it
                indexGhostFlagValues.put(pillIndex, false);
            }

            levelEvaluationDone = true;
        } else {
            // Check what happened last turn and change values accordingly
            earnedScore = game.getScore() - lastScore;
            // First check if the last index was a pill index
            if(checkIfIndexIsPillIndex(game, pacManLastLocationIndex)){
                // Check if last turns index had a ghost on it
                if(indexGhostFlagValues.get(pacManLastLocationIndex)){
                    // If it did change the indexGhostValue
                    // Check if we died on the location, if we did punish the index
                    if(lastLives > game.getPacmanNumberOfLivesRemaining()){
                        indexGhostValues.replace(pacManLastLocationIndex, (indexGhostValues.get(pacManLastLocationIndex) - 10));
                    }
                } else {
                    // If it did not change the normal value
                    indexValues.replace(pacManLastLocationIndex, (indexValues.get(pacManLastLocationIndex) + earnedScore));
                }

                // Reevaluate the map
                for (int pillIndex: game.getPillIndices()) {
                    // Check if there is a ghost on the location, if there is flag it.
                    if(checkForGhosts(pillIndex, game)){
                        indexGhostFlagValues.replace(pillIndex, true);
                    }
                }
            }
        }
    }

    public boolean checkIfIndexIsPillIndex(Game game, int index){
        for (int pillIndex: game.getPillIndices()) {
            if(pillIndex == index){
                return true;
            }
        }
        return false;
    }

    public boolean checkForGhosts(int index, Game game){
        StatusLbl.setText("Status: Checking for ghosts");
        if(index == game.getGhostCurrentNodeIndex(GHOST.PINKY)){return true;}
        if(index == game.getGhostCurrentNodeIndex(GHOST.INKY)){return true;}
        if(index == game.getGhostCurrentNodeIndex(GHOST.BLINKY)){return true;}
        if(index == game.getGhostCurrentNodeIndex(GHOST.SUE)){return true;}
        return false;
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

    public void SetupWindow(){
        pacManStatsWindow.setLayout((new GridLayout(10,1)));
        StatusLbl.setPreferredSize(new Dimension(400,25));
        NextMoveLbl.setPreferredSize(new Dimension(400,25));
        EarnedScoreLbl.setPreferredSize(new Dimension(400,25));
        LastScoreLbl.setPreferredSize(new Dimension(400,25));
        PacManLastLocationIndexLbl.setPreferredSize(new Dimension(400,25));
        LastLevelLbl.setPreferredSize(new Dimension(400,25));
        LastLivesLbl.setPreferredSize(new Dimension(400,25));

        // Set label text
        StatusLbl.setText("Status: ");
        NextMoveLbl.setText("NextMove: ");
        EarnedScoreLbl.setText("EarnedScore: " + earnedScore);
        LastScoreLbl.setText("LastScore: " + lastScore);
        PacManLastLocationIndexLbl.setText("Last location: " + pacManLastLocationIndex);
        LastLevelLbl.setText("Last level: " + lastLevel);
        LastLivesLbl.setText("Last lives: " + lastLives);


        pacManStatsWindow.add(StatusLbl);
        pacManStatsWindow.add(NextMoveLbl);
        pacManStatsWindow.add(EarnedScoreLbl);
        pacManStatsWindow.add(LastScoreLbl);
        pacManStatsWindow.add(PacManLastLocationIndexLbl);
        pacManStatsWindow.add(LastLevelLbl);
        pacManStatsWindow.add(LastLivesLbl);
        pacManStatsWindow.pack();
        pacManStatsWindow.setVisible(true);
        pacManStatsWindow.repaint();
    }



}