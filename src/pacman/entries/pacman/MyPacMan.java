package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE moveToMake=MOVE.NEUTRAL;
	private int lastPacX = 50;

	public MOVE getMove(Game game, long timeDue)
	{
		//Locate closest pill
		int distanceToPill = 999999;
		int closestPillIndex = 0;
		for (int pillIndex: game.getActivePillsIndices()) {
			if(distanceToPill > game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pillIndex)){
				//Save index of the closest pill
				distanceToPill = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pillIndex);
				closestPillIndex = pillIndex;
			}
		}

		int[] indicesToTravel = game.getShortestPath(game.getPacmanCurrentNodeIndex(), closestPillIndex);
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


		//Check distance to each ghost.
		//Count all the distance together.
		return moveToMake;
	}
}