/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Participants.UnrealTeam;

import Othello.Move;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Nicolas Gonin, Horia Mut
 */
public class AI_V0
{

    // Attributes
    public static final int INF = Integer.MAX_VALUE;
    private int player;
    private int depth;

    public static final int MIN = -1;
    public static final int MAX = 1;

    /**
     * AI constructor.
     *
     * @param playerID
     * @param depth
     */
    public AI_V0(int playerID, int depth)
    {
	this.player = playerID;
	this.depth = depth;
    }

    public int evaluationFunction(GameBoard board, int playerID)
    {
	return board.getCoinCount(playerID);
    }

    public void buildTree(GameBoard gameBoard, Node root, int playerID)
    {
	// Fill the tree with our possible moves.
	ArrayList<Move> allOurPossibleMoves = gameBoard.getPossibleMoves(playerID);
	for (Iterator<Move> iMove = allOurPossibleMoves.iterator(); iMove.hasNext();)
	    {
	    Move next = iMove.next();
	    // Build the node.
	    Node nextMoveNode = new Node(next);
	    // Set it's data.
	    nextMoveNode.setEvaluation(evaluationFunction(gameBoard, playerID));
	    root.addChildNode(nextMoveNode);
	    }
    }

    public Move getBestMove(GameBoard gameBoard, Node root, int depth, int playerID)
    {
	return alphaBetaN(gameBoard, root, depth, MAX, root.getEvaluation(), playerID).getMove();
    }

    private Node alphaBetaN(GameBoard board, Node root, int depth, int minOrMax, int parentValue, int currentPlayer)
    {

	// Check if we have reached the maximum depth of if the root is at the end.
	if (depth == 0 || root.isLeaf())
	    {
            // Since we have reached the end, evaluate the actual node (root).
	    // Root should contain a move!
	    root.setEvaluation(evaluationFunction(board, player));
	    if (root.getMove() == null)
		{
		System.err.println("No move,what the fuck?");
		}
	    return root;
	    }

	int optVal = minOrMax * -INF;
	// No optimal operation (move) yet.
	Node optMove = new Node(null);

	ArrayList<Node> allPossibleMoves = root.getChildNodeList();

	// Check if we can do any moves.
	if (allPossibleMoves.isEmpty())
	    {
	    // If we can't, we skip our move.
	    optVal = alphaBetaN(board, root, depth - 1, -minOrMax, optVal, -currentPlayer).getEvaluation();
	    } else
	    {
	    while (!allPossibleMoves.isEmpty())
		{

		// Get the node containing the first possible move.
		Node actualMoveNode = allPossibleMoves.remove(0);

		// Now rebuild the game state, add the piece, then re-evaluate while substracting the depth.
		GameBoard gameBoardClone = board.clone();
		gameBoardClone.addCoin(actualMoveNode.getMove(), currentPlayer);

		// Once the coin has been added, do alpha beta on the new Board and minimize this time.
		actualMoveNode.setEvaluation(alphaBetaN(gameBoardClone, actualMoveNode, depth - 1, -minOrMax, optVal, currentPlayer).getEvaluation());
		int val = actualMoveNode.getEvaluation();
		if (val * minOrMax > optVal * minOrMax)
		    {
		    optVal = val;
		    optMove.setMove(actualMoveNode.getMove());
		    optMove.setEvaluation(optVal);
		    if (optVal * minOrMax > parentValue * minOrMax)
			{
			break;
			}
		    }
		}
	    }

	optMove.setEvaluation(optVal);
	return optMove;
    }

}
