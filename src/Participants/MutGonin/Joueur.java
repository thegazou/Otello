package Participants.MutGonin;

import Othello.Move;

/**
 * Managage the player
 *
 * @author Ellenberger Patrick and Moll Adrian
 *
 */
public class Joueur extends Othello.Joueur
{

    private int depth;
    private int enemyID;
    private GameBoard gameBoard;

    private AI ourAI;

    public Joueur(int depth, int playerID)
    {
	super(depth, playerID);
	this.depth = depth;
	this.playerID = playerID;
	this.enemyID = 1 - playerID;
	this.gameBoard = new GameBoard();
	this.ourAI = new AI(playerID, depth, size * size);
    }

    /**
     * Method called every time the player has to play.
     * @param move  The last move of the enemy. Null if no move was done.
     * @return Our move.
     */
    @Override
    public Move nextPlay(Move move)
    {
	// Build the root of the algorithm tree.
	Node root = new Node(null);
	root.setEvaluation(ourAI.evaluationFunction(gameBoard, playerID, false));

	if (move != null)
	    {
	    //Add enemy coin to the gameboard
	    gameBoard.addCoin(move, enemyID);
	    }

	// Build the tree.
	ourAI.buildTree(gameBoard, root, playerID);

	// Now start the IA.
	// Get the best move (null if no move possible).
	Move ourMove = ourAI.getBestMove(gameBoard, root, depth, playerID);

	//Add player coin to the gameboard
	if (ourMove != null)
	    {
	    this.gameBoard.addCoin(ourMove, playerID);
	    } 
	return ourMove != null ? ourMove : null;
    }
}
