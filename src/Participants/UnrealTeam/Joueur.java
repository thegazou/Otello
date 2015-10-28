package Participants.UnrealTeam;

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

    private AI_V0 ourAI;

    public Joueur(int depth, int playerID)
    {
	super(depth, playerID);
	this.depth = depth;
	this.playerID = playerID;
	this.enemyID = 1 - playerID;
	this.gameBoard = new GameBoard();
	this.ourAI = new AI_V0(playerID, depth);
    }

    /**
     * Method called every time the player has to play
     */
    @Override
    public Move nextPlay(Move move)
    {

	// Ici, vous devrez
	// - Mettre à jour votre représentation du jeu en fonction du coup joué par l'adversaire
	// - Décider quel coup jouer (alpha-beta!!)
	// - Remettre à jour votre représentation du jeu
	// - Retourner le coup choisi
	Move enemyMove = null;

	// Build the root of the algorithm tree.
	Node root = new Node(null);
	root.setEvaluation(ourAI.evaluationFunction(gameBoard, playerID));

	if (move != null)
	    {
	    //Add enemy coin to the gameboard
	    gameBoard.addCoin(move, enemyID);
	    enemyMove = move;

	    // The enemy had a valid move. Add it to the tree.
	    root.setMove(enemyMove);
	    root.setEvaluation(ourAI.evaluationFunction(gameBoard, enemyID));
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
